package controller

import (
	"backend-go/internal/core/domain"
	"backend-go/internal/core/service"
	"context"
	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"net/http"
)

var ctx = context.Background()

type UserController struct {
	userService *service.UserService
}

func NewUserController(userService *service.UserService) *UserController {
	return &UserController{
		userService: userService,
	}
}

// Register godoc
// @Summary      Register a user
// @Description
// @Tags         authentication
// @Accept       json
// @Produce      json
// @Param		 register body domain.User true	"Register user"
// @Create       201
// @Failure      400
// @Failure      404
// @Failure      500
// @Router       /auth/register [post]
func (u *UserController) Register(ctxGin *gin.Context) {
	var req = domain.User{}

	if err := ctxGin.Bind(&req); err != nil {
		domain.Response(ctxGin, http.StatusUnprocessableEntity, 422, nil, err.Error())
		return
	}

	req.Id = primitive.NewObjectID()
	if err := u.userService.Register(ctx, req); err != nil {
		domain.Response(ctxGin, http.StatusUnprocessableEntity, 422, nil, err.Error())
		return
	}

	res, err := u.userService.ReadUser(ctx, req.Id)
	if err != nil {
		domain.Response(ctxGin, http.StatusUnprocessableEntity, 422, nil, err.Error())
		return
	}
	domain.Response(ctxGin, http.StatusCreated, 201, res, "Register ok")
}
