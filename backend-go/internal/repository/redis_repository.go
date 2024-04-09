package repository

import (
	"errors"
	"github.com/go-redis/redis"
	"sync"
)

var (
	onceRepoRedis sync.Once
	redisInstance *RedisRepository
)

type RedisRepository struct {
	RedisClient *redis.Client
}

func NewRedisRepository(address string) *RedisRepository {
	onceRepoRedis.Do(func() {
		client := getClient(address)
		redisInstance = &RedisRepository{
			RedisClient: client,
		}
	})
	return redisInstance
}

func getClient(address string) *redis.Client {
	return redis.NewClient(&redis.Options{
		Addr:     address,
		Password: "",
		DB:       0,
	})
}

func (r *RedisRepository) SetHMSet(key string, value map[string]interface{}) error {
	if err := r.RedisClient.HMSet(key, value).Err(); err != nil {
		return err
	}
	return nil
}

func (r *RedisRepository) GetHMSetAll(key string) (map[string]string, error) {
	val, err := r.RedisClient.HGetAll(key).Result()
	switch {
	case errors.Is(err, redis.Nil), val == nil, len(val) == 0:
		return nil, redis.Nil
	case err != nil:
		return nil, err
	}
	return val, nil
}
