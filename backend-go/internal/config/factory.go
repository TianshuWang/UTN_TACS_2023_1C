package config

import (
	"backend-go/internal/core/controller"
	errors2 "backend-go/internal/core/domain/errors"
	"backend-go/internal/core/service"
	"backend-go/internal/repository"
	passwordValidator "github.com/go-passwd/validator"
	structValidator "github.com/go-playground/validator/v10"
	log "github.com/sirupsen/logrus"
	"os"
)

const (
	charsUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	charsLowercase = "abcdefghijklmnopqrstuvwxyz"
	digits         = "1234567890"
	charsSpacial   = "*_^"
)

var (
	mongoUri = os.Getenv("MONGODB_URI")
	sv       = *structValidator.New()
	pv       = *passwordValidator.New(
		passwordValidator.CommonPassword(nil),
		passwordValidator.ContainsAtLeast(charsUppercase, 1, errors2.ErrPasswordUppercase),
		passwordValidator.ContainsAtLeast(charsLowercase, 1, errors2.ErrPasswordLowercase),
		passwordValidator.ContainsAtLeast(digits, 1, errors2.ErrPasswordDigit),
		passwordValidator.ContainsAtLeast(charsSpacial, 1, errors2.ErrPasswordSpecialChar),
	)
)

func Init() *Initialization {
	logger := log.New()
	mongoRepo := repository.NewMongoRepository(mongoUri)
	userService := service.NewUserService(mongoRepo, logger)
	userController := controller.NewUserController(&sv, &pv, userService)
	eventService := service.NewEventService(mongoRepo, logger)
	eventController := controller.NewEventController(&sv, eventService)
	return &Initialization{mongoRepo, userService, userController, eventService, eventController}
}
