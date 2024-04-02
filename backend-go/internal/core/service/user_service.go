package service

import (
	"backend-go/internal/core/domain/entity"
	errors2 "backend-go/internal/core/domain/errors"
	"backend-go/internal/core/security"
	"backend-go/internal/repository"
	log "github.com/sirupsen/logrus"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"golang.org/x/crypto/bcrypt"
)

const (
	users = "users"
)

type UserService struct {
	repository *repository.MongoRepository
	logger     *log.Logger
}

func NewUserService(repository *repository.MongoRepository, logger *log.Logger) *UserService {
	return &UserService{
		repository: repository,
		logger:     logger,
	}
}

func (s *UserService) Register(user entity.User) (*entity.AuthResponse, error) {
	if err := s.Exists(user); err != nil {
		s.logger.Info("User already exists")
		return nil, err
	}

	hashPassword, err := bcrypt.GenerateFromPassword([]byte(user.Password), bcrypt.DefaultCost)
	if err != nil {
		return nil, err
	}

	user.Id = primitive.NewObjectID()
	user.Password = string(hashPassword)
	if err := s.repository.Create(users, user); err != nil {
		return nil, err
	}

	token, err := security.GenerateToken(user)
	if err != nil {
		return nil, err
	}

	return &entity.AuthResponse{
		FirstName:   user.FirstName,
		LastName:    user.LastName,
		Username:    user.Username,
		AccessToken: token,
	}, nil
}

func (s *UserService) Login(user entity.User) (*entity.AuthResponse, error) {
	savedUser, err := s.FindByUsername(user.Username)
	if err != nil {
		return nil, errors2.ErrUserNotExists
	}
	s.logger.Infof("User found: %+v", savedUser)
	if err := bcrypt.CompareHashAndPassword([]byte(savedUser.Password), []byte(user.Password)); err != nil {
		return nil, errors2.ErrPasswordIsWrong
	}

	token, err := security.GenerateToken(user)
	if err != nil {
		return nil, err
	}

	return &entity.AuthResponse{
		FirstName:   savedUser.FirstName,
		LastName:    savedUser.LastName,
		Username:    savedUser.Username,
		AccessToken: token,
	}, nil
}

func (s *UserService) FindByUsername(username string) (*entity.User, error) {
	var readUser entity.User
	err := s.repository.Find(users, bson.M{"username": username}, &readUser)
	if err != nil {
		return nil, err
	}
	return &readUser, nil
}

func (s *UserService) Exists(user entity.User) error {
	savedUser, err := s.FindByUsername(user.Username)
	if savedUser != nil && err == nil {
		return errors2.ErrUserAlreadyExists
	}
	return nil
}
