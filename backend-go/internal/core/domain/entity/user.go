package entity

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type User struct {
	Id        primitive.ObjectID `json:"id,omitempty" bson:"_id,omitempty"`
	FirstName string             `json:"first_name,omitempty" bson:"first_name,omitempty"`
	LastName  string             `json:"last_name,omitempty" bson:"last_name,omitempty"`
	Username  string             `json:"username,omitempty" bson:"username,omitempty" binding:"required,email"`
	Password  string             `json:"password,omitempty" bson:"password,omitempty" binding:"required"`
}
