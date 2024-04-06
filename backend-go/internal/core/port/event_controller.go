package port

import "github.com/gin-gonic/gin"

type EventController interface {
	CreateEvent(ctxGin *gin.Context)
}
