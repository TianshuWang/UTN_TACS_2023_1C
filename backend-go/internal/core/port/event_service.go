package port

import "backend-go/internal/core/domain/entity"

type EventService interface {
	CreateEvent(event entity.Event, user entity.User) (*entity.Event, error)
}
