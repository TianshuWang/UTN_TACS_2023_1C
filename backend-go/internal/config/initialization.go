package config

import (
	"backend-go/internal/core/port"
)

type Initialization struct {
	MongoRepo       port.MongoRepository
	UserService     port.UserService
	UserController  port.UserController
	EventService    port.EventService
	EventController port.EventController
}

func NewInitialization(
	mongoRepo port.MongoRepository,
	userService port.UserService,
	userController port.UserController,
	eventService port.EventService,
	eventController port.EventController) *Initialization {
	return &Initialization{
		MongoRepo:       mongoRepo,
		UserService:     userService,
		UserController:  userController,
		EventService:    eventService,
		EventController: eventController,
	}
}
