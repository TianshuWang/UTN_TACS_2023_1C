package repository

type RedisRepository interface {
	SetHMSet(key string, value map[string]interface{}) error

	GetHMSetAll(key string) (map[string]string, error)
}
