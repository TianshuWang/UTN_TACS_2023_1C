package entity

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type EventOption struct {
	Id           primitive.ObjectID `bson:"_id,omitempty"`
	DateTime     time.Time          `bson:"date_time" validate:"required"`
	VoteQuantity int64              `bson:"vote_quantity"`
	UpdateTime   time.Time          `bson:"update_time"`
	EventName    string             `bson:"event_name"`
}
