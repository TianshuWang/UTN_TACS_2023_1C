package service

import (
	"backend-go/internal/core/domain"
	"backend-go/internal/repository"
	"context"
	"github.com/go-playground/validator/v10"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type UserService struct {
	validator  *validator.Validate
	repository *repository.MongoRepository
}

func NewUserService(validator *validator.Validate, repository *repository.MongoRepository) *UserService {
	return &UserService{
		validator:  validator,
		repository: repository,
	}
}

func (u *UserService) Register(ctx context.Context, user domain.User) error {
	if err := u.validator.StructCtx(ctx, user); err != nil {
		return err
	}
	_, err := u.repository.Create(ctx, "users", &user)
	if err != nil {
		return err
	}
	return nil
}

func (u *UserService) ReadUser(ctx context.Context, id primitive.ObjectID) (*domain.User, error) {
	var readUser domain.User
	err := u.repository.Read(ctx, "users", bson.M{"_id": id}, &readUser)
	if err != nil {
		return nil, err
	}
	return &readUser, nil
}
