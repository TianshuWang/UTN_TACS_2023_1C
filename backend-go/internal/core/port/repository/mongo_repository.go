package repository

type MongoRepository interface {
	Create(collectionName string, model interface{}) error

	SaveAll(collectionName string, models []interface{}) error

	Find(collectionName string, filter interface{}, result interface{}) error

	FindAll(collectionName string, filter interface{}, results interface{}) (interface{}, error)

	Update(collectionName string, filter interface{}, update interface{}) error

	Delete(collectionName string, filter interface{}) error
}
