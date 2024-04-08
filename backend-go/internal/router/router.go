package router

import (
	"backend-go/internal/config"
	"backend-go/internal/core/middleware"
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
			auth.POST("/register", middleware.RateLimiterMiddleware(init.TokenBucketService), init.UserController.Register)
			auth.POST("/login", middleware.RateLimiterMiddleware(init.TokenBucketService), init.UserController.Login)
		}
		events := api.Group("/events")
		{
			events.POST("", middleware.RateLimiterMiddleware(init.TokenBucketService), middleware.AuthMiddleware(init.UserService), init.EventController.CreateEvent)
			events.GET("", middleware.RateLimiterMiddleware(init.TokenBucketService), middleware.AuthMiddleware(init.UserService), init.EventController.GetAllEvents)
			events.GET("/:id", middleware.RateLimiterMiddleware(init.TokenBucketService), middleware.AuthMiddleware(init.UserService), init.EventController.GetEventById)
			events.PATCH("/:id/user", middleware.RateLimiterMiddleware(init.TokenBucketService), middleware.AuthMiddleware(init.UserService), init.EventController.RegisterEvent)
		}
	}

	return router
}
