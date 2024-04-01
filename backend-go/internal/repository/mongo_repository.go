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

func (m *MongoRepository) Create(ctx context.Context, collectionName string, model interface{}) (*mongo.InsertOneResult, error) {
	collection := m.MongoDb.Collection(collectionName)

	res, err := collection.InsertOne(ctx, model)
	if err != nil {
		return nil, err
	}
	return res, nil
}

func (m *MongoRepository) Read(ctx context.Context, collectionName string, filter interface{}, result interface{}) error {
	collection := m.MongoDb.Collection(collectionName)

	err := collection.FindOne(ctx, filter).Decode(result)
	if err != nil {
		return err
	}

	return nil
}

func (m *MongoRepository) Update(ctx context.Context, collectionName string, filter interface{}, update interface{}) error {
	collection := m.MongoDb.Collection(collectionName)

	_, err := collection.UpdateOne(ctx, filter, update)
	if err != nil {
		return err
	}

	return nil
}

func (m *MongoRepository) Delete(ctx context.Context, collectionName string, filter interface{}) error {
	collection := m.MongoDb.Collection(collectionName)
	_, err := collection.DeleteOne(ctx, filter)
	if err != nil {
		return err
	}

	return nil
}
