package entity

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type Event struct {
	Id              primitive.ObjectID `bson:"_id,omitempty"`
	Name            string             `bson:"name" validate:"required"`
	Description     string             `bson:"description"`
	Status          string             `bson:"status"`
	EventOptions    []interface{}      `bson:"event_options" validate:"required"`
	OwnerUser       User               `bson:"owner_user"`
	RegisteredUsers []interface{}      `bson:"registered_users"`
	CreateDate      time.Time          `bson:"create_date"`
}
