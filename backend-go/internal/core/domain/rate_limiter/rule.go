package rate_limiter

type Rule struct {
	MaxTokens int64
	Rate      int64
}
