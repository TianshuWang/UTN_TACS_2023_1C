package rate_limiter

import (
	"sync"
	"time"
)

type TokenBucket struct {
	Identifier          string
	Rate                int64
	MaxTokens           int64
	CurrentTokens       int64
	LastRefillTimestamp time.Time
	Mutex               sync.Mutex
}
