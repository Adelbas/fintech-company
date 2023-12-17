# Fintech-company
Microservice application
## Launch
1. `git clone https://github.com/Adelbas/fintech-company.git`
2. Go to project folder
3. To build jar type `./gradlew bootJar`
4. To run product-engine tests type `./gradlew test`
5. To start docker type `docker-compose up`
## Modules
- Product-engine module provides core functionality for working with agreements.
- Origination module provides applications processing and product origination
- Scoring module provides calculating scoring points for clients
- Api module provides any api requests and responses processing
## Tech stack
* Spring Boot
* Postgres
* REST/gRPC
* Liquibase
* TestContainers
* MapStruct
