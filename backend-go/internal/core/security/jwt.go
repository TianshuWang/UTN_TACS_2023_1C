package security

import (
	"backend-go/internal/core/domain/entity"
	"github.com/golang-jwt/jwt"
	"os"
	"time"
)

var secretKey = []byte(os.Getenv("SECRET_KEY"))

type Claims struct {
	Username string
	jwt.StandardClaims
}

func GenerateToken(user entity.User) (string, error) {
	expireTime := time.Now().Add(7 * 24 * time.Hour)
	claims := &Claims{
		Username: user.Username,
		StandardClaims: jwt.StandardClaims{
			ExpiresAt: expireTime.Unix(),
			IssuedAt:  time.Now().Unix(),
			Subject:   user.Username,
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString(secretKey)
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

func ParseToken(tokenString string) (*jwt.Token, *Claims, error) {
	claims := &Claims{}
	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (i interface{}, err error) {
		return secretKey, nil
	})
	return token, claims, err
}
