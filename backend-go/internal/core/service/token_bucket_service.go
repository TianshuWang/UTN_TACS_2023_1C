package service

import (
	"backend-go/internal/core/domain/rate_limiter"
	"backend-go/internal/repository"
	"errors"
	"fmt"
	"github.com/go-redis/redis"
	"math"
	"strconv"
	"time"
)

var (
	rulesMap = map[string]rate_limiter.Rule{
		"user": {
			MaxTokens: 1,
			Rate:      5,
		},
	}
)

type TokenBucketService struct {
	repository *repository.RedisRepository
}

func NewTokenBucketService(repo *repository.RedisRepository) *TokenBucketService {
	return &TokenBucketService{
		repository: repo,
	}
}

func (s *TokenBucketService) GetBucket(identifier string, userType string) (*rate_limiter.TokenBucket, error) {
	value, err := s.repository.GetValue(identifier)
	if errors.Is(err, redis.Nil) {
		m := map[string]interface{}{
			"Rate":                rulesMap[userType].Rate,
			"MaxTokens":           rulesMap[userType].MaxTokens,
			"CurrentTokens":       rulesMap[userType].MaxTokens,
			"LastRefillTimestamp": time.Now(),
		}
		fmt.Printf("map: %+v\n", m)
		if err := s.repository.SetKeyValue(identifier, m); err != nil {
			return nil, err
		}
		value, err = s.repository.GetValue(identifier)
		if err != nil {
			return nil, err
		}
		fmt.Printf("value: %+v\n", value)
	} else if err != nil {
		return nil, err
	}

	rate, _ := strconv.ParseInt(value["rate"], 10, 64)
	maxTokens, _ := strconv.ParseInt(value["MaxTokens"], 10, 64)
	currentTokens, _ := strconv.ParseInt(value["CurrentTokens"], 10, 64)
	lastRefillTimestamp, _ := time.Parse(time.RFC3339, value["LastRefillTimestamp"])

	return &rate_limiter.TokenBucket{
		Identifier:          identifier,
		Rate:                rate,
		MaxTokens:           maxTokens,
		CurrentTokens:       currentTokens,
		LastRefillTimestamp: lastRefillTimestamp,
	}, nil
}

func (s *TokenBucketService) IsRequestAllowed(tb *rate_limiter.TokenBucket, tokens int64) bool {
	tb.Mutex.Lock()
	defer tb.Mutex.Unlock()
	s.refill(tb)
	if tb.CurrentTokens >= tokens {
		tb.CurrentTokens = tb.CurrentTokens - tokens
		m := map[string]interface{}{
			"Rate":                tb.Rate,
			"MaxTokens":           tb.MaxTokens,
			"CurrentTokens":       tb.MaxTokens,
			"LastRefillTimestamp": tb.LastRefillTimestamp,
		}
		s.repository.SetKeyValue(tb.Identifier, m)
		return true
	}
	m := map[string]interface{}{
		"Rate":                tb.Rate,
		"MaxTokens":           tb.MaxTokens,
		"CurrentTokens":       tb.MaxTokens,
		"LastRefillTimestamp": tb.LastRefillTimestamp,
	}
	s.repository.SetKeyValue(tb.Identifier, m)
	return false
}

func (s *TokenBucketService) refill(tb *rate_limiter.TokenBucket) {
	now := time.Now()
	end := time.Since(tb.LastRefillTimestamp)
	tokensToAdd := (end.Nanoseconds() * tb.Rate) / 1000000000
	tb.CurrentTokens = int64(math.Min(float64(tb.CurrentTokens+tokensToAdd), float64(tb.MaxTokens)))
	tb.LastRefillTimestamp = now
}
