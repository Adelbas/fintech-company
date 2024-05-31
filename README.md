# Fintech-company
Microservice application  
System architecture: https://miro.com/app/board/uXjVNlaDZos=/?share_link_id=20095406867
## Launch
1. `git clone https://github.com/Adelbas/fintech-company.git`
2. Go to project folder
3. To build jar type `./gradlew bootJar`
4. To run tests type `./gradlew test`
5. To start docker type `docker-compose up`
## Modules
- Api module handles interactions with client-facing web forms and mobile applications. It serves as a centralized service for managing the client-side of the system. 
- Product-engine module provides core functionality for client credits, their payment schedules, and the status of each payment.
- Origination module responsible for receiving online credit applications from the API, processing applications through scoring, and, upon approval, executing account replenishment operations for clients via the payment-gate module.
- Scoring module conducts client checks and makes decisions regarding whether to approve credit for the client.
- Payment-gate module handles the disbursement of funds to clients and also accepts payments through a Payment Provider.
- DWH module stores raw data regarding the statuses of applications and agreements on a daily basis.
## Tech stack
* Spring Boot
* Kafka
* REST/gRPC
* OpenAPI
* Postgres
* Liquibase
* TestContainers
* MapStruct