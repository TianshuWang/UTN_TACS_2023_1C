package router

import (
	"backend-go/internal/config"
	"backend-go/internal/core/middleware"
	"backend-go/internal/core/middleware/rate_limiter"
	"github.com/gin-gonic/gin"
)

func Init(init *config.Initialization) *gin.Engine {
	router := gin.New()
	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	api := router.Group("/v1")
	{
		auth := api.Group("/auth")
		{
			auth.POST("/register", rate_limiter.RateLimiterMiddleware, init.UserController.Register)
			auth.POST("/login", rate_limiter.RateLimiterMiddleware, init.UserController.Login)
		}
		events := api.Group("/events")
		{
			events.POST("", rate_limiter.RateLimiterMiddleware, middleware.AuthMiddleware(init.UserService), init.EventController.CreateEvent)
		}
	}

	return router
}
