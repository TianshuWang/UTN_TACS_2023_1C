package config

import (
	"backend-go/internal/core/port"
)

type Initialization struct {
	MongoRepo          port.MongoRepository
	RedisRepo          port.RedisRepository
	TokenBucketService port.TokenBucketService
	UserService        port.UserService
	UserController     port.UserController
	EventService       port.EventService
	EventController    port.EventController
}

func NewInitialization(
	mongoRepo port.MongoRepository,
	redisRepo port.RedisRepository,
	tokenBucketService port.TokenBucketService,
	userService port.UserService,
	userController port.UserController,
	eventService port.EventService,
	eventController port.EventController) *Initialization {
	return &Initialization{
		MongoRepo:          mongoRepo,
		RedisRepo:          redisRepo,
		TokenBucketService: tokenBucketService,
		UserService:        userService,
		UserController:     userController,
		EventService:       eventService,
		EventController:    eventController,
	}
}
