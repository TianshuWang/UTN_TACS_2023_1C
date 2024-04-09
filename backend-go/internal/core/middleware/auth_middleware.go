package middleware

import (
	"backend-go/internal/core/port/service"
	"backend-go/internal/core/security"
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

const (
	key    = "Authorization"
	prefix = "Bearer"
)

func AuthMiddleware(userService service.UserService) gin.HandlerFunc {
	return func(ctx *gin.Context) {
		tokenString := ctx.GetHeader(key)
		if tokenString == "" || !strings.HasPrefix(tokenString, prefix) {
			ctx.JSON(http.StatusUnauthorized, gin.H{"message": "Unauthorized"})
			ctx.Abort()
			return
		}

		tokenString = tokenString[7:]
		token, claims, err := security.ParseToken(tokenString)
		if err != nil || !token.Valid {
			ctx.JSON(http.StatusUnauthorized, gin.H{"message": "Unauthorized"})
			ctx.Abort()
			return
		}
		user, err := userService.FindByUsername(claims.Username)
		if err != nil {
			ctx.JSON(http.StatusUnauthorized, gin.H{"message": "Unauthorized"})
			ctx.Abort()
			return
		}
		user.Password = ""
		ctx.Set("user", user)
		ctx.Next()
	}
}
