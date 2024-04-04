package main

import (
	_ "backend-go/docs"
	"backend-go/internal/config"
	"backend-go/router"
	"fmt"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"os"
)

var port = os.Getenv("PORT")

// @title           Swagger Example API
// @version         1.0
// @description     This is a sample server celler server.
// @termsOfService  http://swagger.io/terms/

// @contact.name   API Support
// @contact.url    http://www.swagger.io/support
// @contact.email  support@swagger.io

// @license.name  Apache 2.0
// @license.url   http://www.apache.org/licenses/LICENSE-2.0.html

// @host      localhost:8080
// @BasePath

//	@securityDefinitions.apikey	Bearer
//	@in							header
//	@name						Authorization
//	@description				Description for what is this security definition being used

// @externalDocs.description  OpenAPI
// @externalDocs.url          https://swagger.io/resources/open-api/
func main() {
	init := config.Init()
	app := router.Init(init)
	app.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))
	err := app.Run(fmt.Sprintf(":%s", port))
	if err != nil {
		return
	}
}
