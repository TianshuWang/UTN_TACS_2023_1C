package service

import (
	"backend-go/internal/core/domain/rate_limiter"
	"backend-go/internal/repository"
	"errors"
	"github.com/go-redis/redis"
	"log"
	"math"
	"strconv"
	"time"
)

const (
	layoutTimezone = "2006-01-02T15:04:05.000Z"
)

var (
	rulesMap = map[string]rate_limiter.Rule{
		"user": {
			MaxTokens: 5,
			Rate:      1,
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
	value, err := s.repository.GetHMSetAll(identifier)
	if errors.Is(err, redis.Nil) {
		s.initTokenBucket(identifier, userType)
		value, err = s.repository.GetHMSetAll(identifier)
		if err != nil {
			return nil, err
		}
	} else if err != nil {
		return nil, err
	}
	rate, _ := strconv.ParseInt(value["Rate"], 10, 64)
	maxTokens, _ := strconv.ParseInt(value["MaxTokens"], 10, 64)
	currentTokens, _ := strconv.ParseInt(value["CurrentTokens"], 10, 64)
	lastRefillTimestamp, _ := time.Parse(layoutTimezone, value["LastRefillTimestamp"])

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
		tb.CurrentTokens -= tokens
		s.updateTokenBucket(tb)
		return true
	}
	s.updateTokenBucket(tb)
	return false
}

func (s *TokenBucketService) refill(tb *rate_limiter.TokenBucket) {
	now := time.Now()
	end := time.Since(tb.LastRefillTimestamp)
	tokensToAdd := (end.Nanoseconds() * tb.Rate) / 1000000000
	tb.CurrentTokens = int64(math.Min(float64(tb.CurrentTokens+tokensToAdd), float64(tb.MaxTokens)))
	tb.LastRefillTimestamp = now
}

func (s *TokenBucketService) initTokenBucket(identifier string, userType string) {
	tb := &rate_limiter.TokenBucket{
		Rate:                rulesMap[userType].Rate,
		MaxTokens:           rulesMap[userType].MaxTokens,
		CurrentTokens:       rulesMap[userType].MaxTokens,
		LastRefillTimestamp: time.Now(),
	}
	s.setTokenBucket(identifier, tb)
}

func (s *TokenBucketService) updateTokenBucket(tb *rate_limiter.TokenBucket) {
	s.setTokenBucket(tb.Identifier, tb)
}

func (s *TokenBucketService) setTokenBucket(identifier string, tb *rate_limiter.TokenBucket) {
	m := map[string]interface{}{
		"Rate":                tb.Rate,
		"MaxTokens":           tb.MaxTokens,
		"CurrentTokens":       tb.CurrentTokens,
		"LastRefillTimestamp": tb.LastRefillTimestamp.Format(layoutTimezone),
	}

	if err := s.repository.SetHMSet(identifier, m); err != nil {
		log.Fatal(err.Error())
	}
}
