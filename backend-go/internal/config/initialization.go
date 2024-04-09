package config

import (
	"backend-go/internal/core/port/controller"
	"backend-go/internal/core/port/repository"
	"backend-go/internal/core/port/service"
)

type Initialization struct {
	MongoRepo          repository.MongoRepository
	RedisRepo          repository.RedisRepository
	TokenBucketService service.TokenBucketService
	UserService        service.UserService
	UserController     controller.UserController
	EventService       service.EventService
	EventController    controller.EventController
}
