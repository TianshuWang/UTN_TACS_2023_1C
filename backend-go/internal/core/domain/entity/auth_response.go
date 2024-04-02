package entity

type AuthResponse struct {
	FirstName   string `json:"first_name"`
	LastName    string `json:"last_name"`
	Username    string `json:"username"`
	AccessToken string `json:"access_token"`
}
