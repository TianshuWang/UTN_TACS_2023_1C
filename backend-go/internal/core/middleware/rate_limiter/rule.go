package rate_limiter

type Rule struct {
	MaxTokens int64
	Rate      int64
}

var (
	clientBucketMap = make(map[string]*TokenBucket)
	rulesMap        = map[string]Rule{
		"user": {
			MaxTokens: 1,
			Rate:      5,
		},
	}
)

func GetBucket(identifier string, userType string) *TokenBucket {
	if clientBucketMap[identifier] == nil {
		clientBucketMap[identifier] = NewTokenBucket(rulesMap[userType].MaxTokens, rulesMap[userType].Rate)
	}
	return clientBucketMap[identifier]
}
