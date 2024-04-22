package entity

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type EventOption struct {
	Id           primitive.ObjectID `json:"id,omitempty" bson:"_id,omitempty"`
	DateTime     string             `json:"date_time,omitempty" bson:"date_time,omitempty" binding:"required"`
	VoteQuantity int64              `json:"vote_quantity,omitempty" bson:"vote_quantity,omitempty"`
	UpdateTime   time.Time          `json:"update_time,omitempty" bson:"update_time,omitempty"`
	EventName    string             `json:"event_name,omitempty" bson:"event_name,omitempty"`
}
