package service

import (
	"backend-go/internal/core/domain/entity"
	"backend-go/internal/repository"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

const (
	events       = "events"
	eventOptions = "event_options"
)

type EventService struct {
	repository *repository.MongoRepository
}

func NewEventService(repository *repository.MongoRepository) *EventService {
	return &EventService{
		repository: repository,
	}
}

func (s *EventService) CreateEvent(event entity.Event, user entity.User) (*entity.Event, error) {
	options := event.EventOptions
	for _, option := range options {
		o, _ := option.(entity.EventOption)
		o.Id = primitive.NewObjectID()
		o.EventName = event.Name
		o.UpdateTime = time.Now()
	}

	err := s.repository.SaveAll(eventOptions, options)
	if err != nil {
		return nil, err
	}

	event.Id = primitive.NewObjectID()
	event.Status = "VOTE_PENDING"
	event.EventOptions = options
	event.CreateDate = time.Now()
	event.OwnerUser = user
	event.RegisteredUsers = make([]interface{}, 0)
	if err := s.repository.Create(events, event); err != nil {
		return nil, err
	}

	return &event, nil
}
