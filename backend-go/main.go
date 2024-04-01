package main

import (
	"github.com/Bancar/uala-core/create-correlated-transaction-aws-lambda/internal/factory"
	"log"
)

func main() {
	f := factory.New()

	if err := f.Run(); err != nil {
		log.Fatal(err)
	}
}
