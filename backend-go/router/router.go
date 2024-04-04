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
			auth.POST("/register", init.UserController.Register)
			auth.POST("/login", middleware.AuthMiddleware(init.UserService), init.UserController.Login)
		}
	}

	return router
}
