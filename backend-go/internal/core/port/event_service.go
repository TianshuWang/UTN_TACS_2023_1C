package port

import (
	"backend-go/internal/core/domain/entity"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type EventService interface {
	CreateEvent(event entity.Event, user entity.User) (*entity.Event, error)
	GetAllEvents() ([]entity.Event, error)
	GetEventById(id primitive.ObjectID) (*entity.Event, error)
	RegisterEvent(id primitive.ObjectID, user entity.User) (*entity.Event, error)
}
