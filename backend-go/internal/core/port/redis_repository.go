package port

type RedisRepository interface {
	SetKeyValue(key string, value map[string]interface{}) error

	GetValue(key string) (map[string]string, error)

	DeleteValue(key string) error
}
