package controller

import (
	"backend-go/internal/core/domain/entity"
	"backend-go/internal/core/service"
	"github.com/gin-gonic/gin"
	passwordValidator "github.com/go-passwd/validator"
	structValidator "github.com/go-playground/validator/v10"
	"net/http"
)

type UserController struct {
	structValidator   *structValidator.Validate
	passwordValidator *passwordValidator.Validator
	userService       *service.UserService
}

func NewUserController(structValidator *structValidator.Validate, passwordValidator *passwordValidator.Validator, userService *service.UserService) *UserController {
	return &UserController{
		structValidator:   structValidator,
		passwordValidator: passwordValidator,
		userService:       userService,
	}
}

// Register godoc
// @Summary      Register a user
// @Description
// @Tags         authentication
// @Accept       json
// @Produce      json
// @Param		 request body domain.User true "request body"
// @Created      201
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/auth/register [post]
func (c *UserController) Register(ctxGin *gin.Context) {
	var req = entity.User{}

	if err := ctxGin.Bind(&req); err != nil {
		ctxGin.JSON(http.StatusUnprocessableEntity, gin.H{"message": err.Error()})
		return
	}
	if err := c.structValidator.Struct(req); err != nil {
		ctxGin.JSON(http.StatusUnprocessableEntity, gin.H{"message": err.Error()})
		return
	}

	if err := c.passwordValidator.Validate(req.Password); err != nil {
		ctxGin.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}

	authResponse, err := c.userService.Register(req)
	if err != nil {
		ctxGin.JSON(http.StatusUnprocessableEntity, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusCreated, authResponse)
}

// Login godoc
// @Summary      Login a user
// @Description
// @Tags         authentication
// @Accept       json
// @Produce      json
// @Param		 request body domain.User true "request body"
// @Created      201
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /v1/auth/login [post]
func (c *UserController) Login(ctxGin *gin.Context) {
	var req = entity.User{}

	if err := ctxGin.Bind(&req); err != nil {
		ctxGin.JSON(http.StatusUnprocessableEntity, gin.H{"message": err.Error()})
		return
	}
	if err := c.structValidator.Struct(req); err != nil {
		ctxGin.JSON(http.StatusUnprocessableEntity, gin.H{"message": err.Error()})
		return
	}

	authResponse, err := c.userService.Login(req)
	if err != nil {
		ctxGin.JSON(http.StatusUnprocessableEntity, gin.H{"message": err.Error()})
		return
	}

	ctxGin.JSON(http.StatusOK, authResponse)
}
