package entity

type AuthResponse struct {
	FirstName   string `json:"first_name,omitempty"`
	LastName    string `json:"last_name,omitempty"`
	Username    string `json:"username"`
	AccessToken string `json:"access_token"`
}
