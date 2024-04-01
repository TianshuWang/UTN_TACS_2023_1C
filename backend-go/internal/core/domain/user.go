package domain

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type User struct {
	Id        primitive.ObjectID `bson:"_id, omitempty"`
	FirstName string             `bson:"first_name"`
	LastName  string             `bson:"last_name"`
	Username  string             `bson:"username"`
	Password  string             `bson:"password"`
}
