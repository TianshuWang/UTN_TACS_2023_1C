package controller

import (
	"github.com/gin-gonic/gin"
)

type UserController interface {
	Register(ctxGin *gin.Context)
	Login(ctxGin *gin.Context)
}
