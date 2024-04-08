package port

import (
	"backend-go/internal/core/domain/rate_limiter"
)

type TokenBucketService interface {
	GetBucket(identifier string, userType string) (*rate_limiter.TokenBucket, error)
	IsRequestAllowed(tb *rate_limiter.TokenBucket, tokens int64) bool
}
