package repository

import (
	"context"
	"fmt"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"sync"
)

var (
	onceRepo     sync.Once
	repoInstance *MongoRepository
	ctx          = context.Background()
)

type MongoRepository struct {
	MongoDb *mongo.Database
}

func NewMongoRepository(uri string) *MongoRepository {
	onceRepo.Do(func() {
		client, err := getConnection(uri)
		db := client.Database("TP")
		if err != nil {
			fmt.Printf("Failed to connect to database: %s \n", err)
		}
		repoInstance = &MongoRepository{
			MongoDb: db,
		}
	})
	return repoInstance
}

func (m *MongoRepository) Create(collectionName string, model interface{}) error {
	collection := m.MongoDb.Collection(collectionName)

	_, err := collection.InsertOne(ctx, model)
	if err != nil {
		return err
	}
	return nil
}

func (m *MongoRepository) SaveAll(collectionName string, models []interface{}) error {
	collection := m.MongoDb.Collection(collectionName)

	_, err := collection.InsertMany(ctx, models)
	if err != nil {
		return err
	}
	return nil
}

func (m *MongoRepository) Find(collectionName string, filter interface{}, result interface{}) error {
	collection := m.MongoDb.Collection(collectionName)

	err := collection.FindOne(ctx, filter).Decode(result)
	if err != nil {
		return err
	}

	return nil
}

func (m *MongoRepository) Update(collectionName string, filter interface{}, update interface{}) error {
	collection := m.MongoDb.Collection(collectionName)

	_, err := collection.UpdateOne(ctx, filter, update)
	if err != nil {
		return err
	}

	return nil
}

func (m *MongoRepository) Delete(collectionName string, filter interface{}) error {
	collection := m.MongoDb.Collection(collectionName)
	_, err := collection.DeleteOne(ctx, filter)
	if err != nil {
		return err
	}

	return nil
}

func getConnection(uri string) (*mongo.Client, error) {
	clientOptions := options.Client().ApplyURI(uri)
	client, err := mongo.Connect(context.Background(), clientOptions)

	if err != nil {
		log.Fatal(err)
	}

	err = client.Ping(context.Background(), nil)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("Successfully connected to MongoDB")
	return client, nil
}
