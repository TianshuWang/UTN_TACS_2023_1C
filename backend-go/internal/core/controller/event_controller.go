package controller

import (
	"backend-go/internal/core/domain/entity"
	"backend-go/internal/core/service"
	"github.com/gin-gonic/gin"
	structValidator "github.com/go-playground/validator/v10"
	"github.com/mitchellh/mapstructure"
	"net/http"
)

type EventController struct {
	eventService    *service.EventService
	structValidator *structValidator.Validate
}

func NewEventController(structValidator *structValidator.Validate, eventService *service.EventService) *EventController {
	return &EventController{
		structValidator: structValidator,
		eventService:    eventService,
	}
}

// CreateEvent godoc
// @Summary      Create an event
// @Description
// @Tags         events
// @Accept       json
// @Produce      json
// @Param		 request body entity.Event true "request body"
// @Created      201
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events [post]
// @Security 	 Bearer
func (c *EventController) CreateEvent(ctxGin *gin.Context) {
	var req = entity.Event{}

	if err := ctxGin.Bind(&req); err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": "Gin: " + err.Error()})
		return
	}
	if err := c.structValidator.StructExcept(req, "OwnerUser.Username", "OwnerUser.Password"); err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	user, ok := ctxGin.Get("user")
	if !ok {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": "user not found"})
		return
	}

	var currentUser entity.User
	err := mapstructure.Decode(user, &currentUser)
	if err != nil {
		ctxGin.JSON(http.StatusInternalServerError, gin.H{"message": err.Error()})
		return
	}
	eventResponse, err := c.eventService.CreateEvent(req, currentUser)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusCreated, eventResponse)
}
