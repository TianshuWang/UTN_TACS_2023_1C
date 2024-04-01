package domain

import (
	"github.com/gin-gonic/gin"
)

func Response(ctx *gin.Context, httpStatus int, code int, data interface{}, msg string) {
	ctx.JSON(httpStatus, gin.H{"code": code, "data": data, "message": msg})
}
