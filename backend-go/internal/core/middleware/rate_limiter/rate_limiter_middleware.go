package rate_limiter

import (
	"crypto/md5"
	"fmt"
	"github.com/gin-gonic/gin"
	"math/big"
	"net/http"
)

func RateLimiterMiddleware(ctx *gin.Context) {
	var userType string
	if val, ok := ctx.Get("user-type"); ok {
		userType = val.(string)
	}
	if userType == "" {
		userType = "user"
	}

	tokenBucket := GetBucket(GetClientIdentifier(ctx), userType)
	if !tokenBucket.IsRequestAllowed(5) {
		ctx.AbortWithStatusJSON(http.StatusTooManyRequests, gin.H{"message": "Try again after sometime"})
		return
	}

	ctx.Next()

}

func GetClientIdentifier(ctx *gin.Context) string {
	ip := ctx.ClientIP()
	url := ctx.Request.URL.Path
	data := fmt.Sprintf("%s-%s", ip, url)
	h := md5.Sum([]byte(data))
	hash := new(big.Int).SetBytes(h[:]).Text(62)
	return hash
}
