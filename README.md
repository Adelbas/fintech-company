# Fintech-company
Microservice application
System architecture: https://miro.com/app/board/uXjVNlaDZos=/?share_link_id=20095406867
## Launch
1. `git clone -b develop-product-engine https://github.com/central-university-dev/fintech-company-Adelbas.git`
2. Go to project folder
3. To build jar type `./gradlew bootJar`
4. To run product-engine tests type `./gradlew :product-engine:test`
5. To start docker type `docker-compose up`
## Modules
- Product-engine module provides core functionality for working with agreements.
## Tech stack
* Spring Boot
* Postgres
* REST/gRPC
* Liquibase
* TestContainers
* MapStruct