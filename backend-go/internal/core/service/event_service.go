package service

import (
	"backend-go/internal/core/domain/constant"
	"backend-go/internal/core/domain/entity"
	errors2 "backend-go/internal/core/domain/errors"
	"backend-go/internal/repository"
	"github.com/mitchellh/mapstructure"
	log "github.com/sirupsen/logrus"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"slices"
	"time"
)

const (
	Events       = "Events"
	EventOptions = "event_options"
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
	ops, err := s.saveEventOptions(event)
	if err != nil {
		return nil, err
	}

	event.Id = primitive.NewObjectID()
	event.Status = constant.VotePending
	event.CreateDate = time.Now()
	event.OwnerUser = user
	event.EventOptions = ops
	event.RegisteredUsers = []entity.User{}
	if err := s.repository.Create(Events, event); err != nil {
		return nil, err
	}

	var savedEvent entity.Event
	if err := s.repository.Find(Events, bson.M{"_id": event.Id}, &savedEvent); err != nil {
		return nil, err
	}
	return &savedEvent, nil
}

func (s *EventService) GetAllEvents() ([]entity.Event, error) {
	var results []entity.Event
	events, err := s.repository.FindAll(Events, bson.D{{}}, results)
	if err != nil {
		return nil, err
	}

	var eventsResponse []entity.Event
	if err := mapstructure.Decode(events, &eventsResponse); err != nil {
		return nil, err
	}
	return eventsResponse, nil
}

func (s *EventService) GetEventById(id primitive.ObjectID) (*entity.Event, error) {
	var event entity.Event
	if err := s.repository.Find(Events, bson.M{"_id": id}, &event); err != nil {
		return nil, errors2.ErrEventNotExists
	}
	return &event, nil
}

func (s *EventService) RegisterEvent(id primitive.ObjectID, user entity.User) (*entity.Event, error) {
	var event entity.Event
	if err := s.repository.Find(Events, bson.M{"_id": id}, &event); err != nil {
		return nil, errors2.ErrEventNotExists
	}

	if slices.Contains(event.RegisteredUsers, user) {
		return nil, errors2.ErrUserAlreadyRegistered
	}

	event.RegisteredUsers = append(event.RegisteredUsers, user)
	if err := s.repository.Update(Events, bson.M{"_id": event.Id}, bson.M{"$set": event}); err != nil {
		return nil, err
	}
	return &event, nil
}

func (s *EventService) ChangeEventStatus(id primitive.ObjectID, status string) (*entity.Event, error) {
	var event entity.Event
	if err := s.repository.Find(Events, bson.M{"_id": id}, &event); err != nil {
		return nil, errors2.ErrEventNotExists
	}

	event.Status = status
	if err := s.repository.Update(Events, bson.M{"_id": event.Id}, bson.M{"$set": event}); err != nil {
		return nil, err
	}
	return &event, nil
}

func (s *EventService) VoteEventOption(eventId primitive.ObjectID, eventOptionId primitive.ObjectID) (*entity.Event, error) {
	var event entity.Event
	if err := s.repository.Find(Events, bson.M{"_id": eventId}, &event); err != nil {
		return nil, errors2.ErrEventNotExists
	}
	var eventOption entity.EventOption
	if err := s.repository.Find(EventOptions, bson.M{"_id": eventOptionId}, &eventOption); err != nil {
		return nil, errors2.ErrEventOptionNotExists
	}

	if event.Status == constant.VoteClosed {
		return nil, errors2.ErrEventVoteClosed
	}

	eventOption.VoteQuantity += 1
	eventOption.UpdateTime = time.Now()
	if err := s.repository.Update(EventOptions, bson.M{"_id": eventOption.Id}, bson.M{"$set": eventOption}); err != nil {
		return nil, err
	}
	if err := s.repository.Find(Events, bson.M{"_id": event.Id}, &event); err != nil {
		return nil, errors2.ErrEventNotExists
	}
	return &event, nil
}

func (s *EventService) saveEventOptions(event entity.Event) ([]entity.EventOption, error) {
	ops := make([]entity.EventOption, len(event.EventOptions))
	for i, option := range event.EventOptions {
		option.Id = primitive.NewObjectID()
		option.EventName = event.Name
		option.UpdateTime = time.Now()
		option.VoteQuantity = 0

		if err := s.repository.Create(EventOptions, option); err != nil {
			return nil, err
		}

		if err := s.repository.Find(EventOptions, bson.M{"_id": option.Id}, &ops[i]); err != nil {
			return nil, err
		}
	}
	return ops, nil
}
