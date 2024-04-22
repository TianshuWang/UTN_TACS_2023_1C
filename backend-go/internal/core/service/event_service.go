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
	Events = "Events"
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
	event.Id = primitive.NewObjectID()
	event.Status = constant.VotePending
	event.CreateDate = time.Now()
	event.OwnerUser = user
	event.EventOptions = s.getEventOptions(event)
	event.RegisteredUsers = []entity.User{}
	if err := s.repository.Create(Events, event); err != nil {
		return nil, err
	}

	return &event, nil
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

func (s *EventService) ChangeEventStatus(id primitive.ObjectID, user entity.User, status string) (*entity.Event, error) {
	var event entity.Event
	if err := s.repository.Find(Events, bson.M{"_id": id}, &event); err != nil {
		return nil, errors2.ErrEventNotExists
	}

	eventUser := event.OwnerUser
	if eventUser != user {
		return nil, errors2.ErrUserNotAllowToChangeEvent
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
	if event.Status == constant.VoteClosed {
		return nil, errors2.ErrEventVoteClosed
	}

	eventOption, err := s.findEventOptionById(eventOptionId, event.EventOptions)
	if err != nil {
		return nil, err
	}

	for i, op := range event.EventOptions {
		if op.Id == eventOptionId {
			event.EventOptions[i] = eventOption
		}
		break
	}
	if err := s.repository.Update(Events, bson.M{"_id": eventId}, bson.M{"$set": event}); err != nil {
		return nil, err
	}
	return &event, nil
}

func (s *EventService) getEventOptions(event entity.Event) []entity.EventOption {
	ops := make([]entity.EventOption, len(event.EventOptions))
	for i, option := range event.EventOptions {
		ops[i] = entity.EventOption{
			Id:           primitive.NewObjectID(),
			EventName:    event.Name,
			DateTime:     option.DateTime,
			UpdateTime:   time.Now(),
			VoteQuantity: 0,
		}
	}
	return ops
}

func (s *EventService) findEventOptionById(eventOptionId primitive.ObjectID, options []entity.EventOption) (entity.EventOption, error) {
	index := slices.IndexFunc(options, func(op entity.EventOption) bool { return op.Id == eventOptionId })
	if index == -1 || &options[index] == nil {
		return entity.EventOption{}, errors2.ErrEventOptionNotBelongsToEvent
	}
	eventOption := options[index]
	eventOption.VoteQuantity++
	eventOption.UpdateTime = time.Now()
	return eventOption, nil
}
