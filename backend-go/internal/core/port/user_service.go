package port

import (
	"backend-go/internal/core/domain/entity"
)

type UserService interface {
	Register(user entity.User) (*entity.AuthResponse, error)

	Login(user entity.User) (*entity.AuthResponse, error)

	FindByUsername(username string) (*entity.User, error)

	Exists(user entity.User) error
}
