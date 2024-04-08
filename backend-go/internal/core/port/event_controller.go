package port

import "github.com/gin-gonic/gin"

type EventController interface {
	CreateEvent(ctxGin *gin.Context)
	GetAllEvents(ctxGin *gin.Context)
	GetEventById(ctxGin *gin.Context)
	RegisterEvent(ctxGin *gin.Context)
}
