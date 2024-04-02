package config

import (
	"backend-go/internal/core/port"
)

type Initialization struct {
	MongoRepo      port.MongoRepository
	UserService    port.UserService
	UserController port.UserController
}

func NewInitialization(mongoRepo port.MongoRepository, userService port.UserService, userController port.UserController) *Initialization {
	return &Initialization{
		MongoRepo:      mongoRepo,
		UserService:    userService,
		UserController: userController,
	}
}
