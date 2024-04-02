package middleware

import (
	"backend-go/internal/core/security"
	"backend-go/internal/core/service"
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

func AuthMiddleware(userService *service.UserService) gin.HandlerFunc {
	return func(ctx *gin.Context) {
		tokenString := ctx.GetHeader("Authorization")

		if tokenString == "" || !strings.HasPrefix(tokenString, "Bearer") {
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
		if err == nil {
			ctx.JSON(http.StatusUnauthorized, gin.H{"message": "Unauthorized"})
			ctx.Abort()
			return
		}

		ctx.Set("user", user)
		ctx.Next()
	}
}
