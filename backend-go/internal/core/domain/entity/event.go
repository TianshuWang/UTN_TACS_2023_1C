package entity

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type Event struct {
	Id              primitive.ObjectID `json:"id,omitempty" bson:"_id,omitempty"`
	Name            string             `bson:"name" validate:"required"`
	Description     string             `json:"description,omitempty" bson:"description,omitempty"`
	Status          string             `json:"status,omitempty" bson:"status,omitempty"`
	EventOptions    []EventOption      `bson:"event_options,omitempty" validate:"required"`
	OwnerUser       User               `json:"owner_user,omitempty" bson:"owner_user,omitempty"`
	RegisteredUsers []User             `json:"registered_users,omitempty" bson:"registered_users,omitempty"`
	CreateDate      time.Time          `json:"create_date,omitempty" bson:"create_date,omitempty"`
}
