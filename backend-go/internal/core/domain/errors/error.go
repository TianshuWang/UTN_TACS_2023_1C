package errors

var (
	ErrUserAlreadyExists   = New("user already exists")
	ErrUserNotExists       = New("user not exists")
	ErrPasswordIsWrong     = New("password is wrong")
	ErrPasswordUppercase   = New("the password must contains at least one upper-case character")
	ErrPasswordLowercase   = New("the password must contains at least one lower-case character")
	ErrPasswordDigit       = New("the password must contains at least one digit")
	ErrPasswordSpecialChar = New("the password must contains at least one spacial character")
)

type Err interface {
	Error() string
}

type err struct {
	Message string `json:"message"`
}

func (e *err) Error() string {
	return e.Message
}

func New(message string) Err {
	return &err{Message: message}
}
