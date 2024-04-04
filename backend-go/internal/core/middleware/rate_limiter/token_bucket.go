package rate_limiter

import (
	"math"
	"sync"
	"time"
)

type TokenBucket struct {
	rate                int64
	maxTokens           int64
	currentTokens       int64
	lastRefillTimestamp time.Time
	mutex               sync.Mutex
}

func NewTokenBucket(rate int64, maxTokens int64) *TokenBucket {
	return &TokenBucket{
		rate:                rate,
		maxTokens:           maxTokens,
		lastRefillTimestamp: time.Now(),
		currentTokens:       maxTokens,
	}
}

func (tb *TokenBucket) IsRequestAllowed(tokens int64) bool {
	tb.mutex.Lock()
	defer tb.mutex.Unlock()
	tb.refill()
	if tb.currentTokens >= tokens {
		tb.currentTokens = tb.currentTokens - tokens
		return true
	}
	return false
}

func (tb *TokenBucket) refill() {
	now := time.Now()
	end := time.Since(tb.lastRefillTimestamp)
	tokensToAdd := (end.Nanoseconds() * tb.rate) / 1000000000
	tb.currentTokens = int64(math.Min(float64(tb.currentTokens+tokensToAdd), float64(tb.maxTokens)))
	tb.lastRefillTimestamp = now
}
