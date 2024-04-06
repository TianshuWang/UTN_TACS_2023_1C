package service

import (
	"backend-go/internal/core/domain/constant"
	"backend-go/internal/core/domain/entity"
	"backend-go/internal/repository"
	"fmt"
	log "github.com/sirupsen/logrus"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

const (
	events       = "events"
	eventOptions = "event_options"
)

type EventService struct {
	repository *repository.MongoRepository
	logger     *log.Logger
}

func NewEventService(repository *repository.MongoRepository, logger *log.Logger) *EventService {
	return &EventService{
		repository: repository,
		logger:     logger,
	}
}

func (s *EventService) CreateEvent(event entity.Event, user entity.User) (*entity.Event, error) {
	var ops []entity.EventOption
	for _, option := range event.EventOptions {
		fmt.Printf("op: %+v\n", option)
		option.Id = primitive.NewObjectID()
		option.EventName = event.Name
		option.UpdateTime = time.Now()
		err := s.repository.Create(eventOptions, option)
		if err != nil {
			return nil, err
		}
		var op entity.EventOption
		if err := s.repository.Find(eventOptions, bson.M{"_id": option.Id}, &op); err != nil {
			return nil, err
		}
		ops = append(ops, op)
	}

	event.Id = primitive.NewObjectID()
	event.Status = constant.VotePending
	event.CreateDate = time.Now()
	event.OwnerUser = user
	event.EventOptions = ops
	if err := s.repository.Create(events, event); err != nil {
		return nil, err
	}

	var savedEvent entity.Event
	if err := s.repository.Find(events, bson.M{"_id": event.Id}, &savedEvent); err != nil {
		return nil, err
	}
	return &savedEvent, nil
}
