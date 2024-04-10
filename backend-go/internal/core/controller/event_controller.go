package controller

import (
	"backend-go/internal/core/domain/entity"
	"backend-go/internal/core/service"
	"fmt"
	"github.com/gin-gonic/gin"
	structValidator "github.com/go-playground/validator/v10"
	"github.com/mitchellh/mapstructure"
	"go.mongodb.org/mongo-driver/bson/primitive"
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
// @Success      200 {object} entity.Event
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events [post]
// @Security 	 Bearer
func (c *EventController) CreateEvent(ctxGin *gin.Context) {
	var req = entity.Event{}

	if err := ctxGin.Bind(&req); err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}
	if err := c.structValidator.StructExcept(req, "OwnerUser.Username", "OwnerUser.Password"); err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	var currentUser entity.User
	getCurrentUser(ctxGin, &currentUser)

	eventResponse, err := c.eventService.CreateEvent(req, currentUser)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusOK, eventResponse)
}

// GetAllEvents godoc
// @Summary      Get all events
// @Description
// @Tags         events
// @Accept       json
// @Produce      json
// @Success      200 {array} entity.Event
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events [get]
// @Security 	 Bearer
func (c *EventController) GetAllEvents(ctxGin *gin.Context) {
	events, err := c.eventService.GetAllEvents()
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusCreated, events)
}

// GetEventById godoc
// @Summary      Get event by id
// @Description
// @Tags         events
// @Accept       json
// @Produce      json
// @Param		 id path string true  "event id"
// @Success      200 {object} entity.Event
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events/{id} [get]
// @Security 	 Bearer
func (c *EventController) GetEventById(ctxGin *gin.Context) {
	id := ctxGin.Param("id")
	objectId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}
	event, err := c.eventService.GetEventById(objectId)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusOK, event)
}

// RegisterEvent godoc
// @Summary      Register a user to event
// @Description
// @Tags         events
// @Accept       json
// @Produce      json
// @Param		 id path string true  "event id"
// @Success      200 {object} entity.Event
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events/{id}/user [patch]
// @Security 	 Bearer
func (c *EventController) RegisterEvent(ctxGin *gin.Context) {
	id := ctxGin.Param("id")
	objectId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	var currentUser entity.User
	getCurrentUser(ctxGin, &currentUser)
	event, err := c.eventService.RegisterEvent(objectId, currentUser)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusOK, event)
}

// ChangeEventStatus godoc
// @Summary      Change a event status
// @Description
// @Tags         events
// @Accept       json
// @Produce      json
// @Param		 id path string true "event id"
// @Param		 status query string true "status"
// @Success      200 {object} entity.Event
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events/{id} [patch]
// @Security 	 Bearer
func (c *EventController) ChangeEventStatus(ctxGin *gin.Context) {
	id := ctxGin.Param("id")
	objectId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}
	status := ctxGin.Query("status")
	event, err := c.eventService.ChangeEventStatus(objectId, status)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusOK, event)
}

// VoteEventOption godoc
// @Summary      Vote an event option
// @Description
// @Tags         events
// @Accept       json
// @Produce      json
// @Param		 event_id path string true "event id"
// @Param		 option_id path string true "event option id"
// @Success      200 {object} entity.Event
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/events/vote/{event_id}/options/{option_id} [patch]
// @Security 	 Bearer
func (c *EventController) VoteEventOption(ctxGin *gin.Context) {
	eid := ctxGin.Param("event_id")
	eventId, err := primitive.ObjectIDFromHex(eid)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}
	oid := ctxGin.Param("option_id")
	eventOptionId, err := primitive.ObjectIDFromHex(oid)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}
	event, err := c.eventService.VoteEventOption(eventId, eventOptionId)
	if err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusOK, event)
}

func getCurrentUser(ctxGin *gin.Context, currentUser *entity.User) {
	user, ok := ctxGin.Get("user")
	if !ok {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": "user not found"})
		return
	}
	if err := mapstructure.Decode(user, &currentUser); err != nil {
		ctxGin.JSON(http.StatusInternalServerError, gin.H{"message": err.Error()})
		return
	}
	fmt.Printf("Current user: %+v\n", currentUser)
}
