# Banking Microservices Application

## 📚 Documentation

- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Detailed architecture documentation, design patterns, and technology decisions
- **[DEPLOYMENT.md](DEPLOYMENT.md)** - Complete deployment guide for development, Docker, and production environments

## Architecture Overview

- **Customer Service**: Manages Person and Customer entities (Port 8080)
- **Account Service**: Manages Account, Movement (Transaction), and Reports (Port 8081)
- **Communication**: Asynchronous via Apache Kafka
- **Framework**: Spring WebFlux (Reactive)
- **Database**: MySQL (separate databases per service)

## Prerequisites

- Java 17+
- Docker and Docker Compose
- Gradle 7.6+

## Getting Started

### 1. Start Infrastructure

```bash
docker-compose up -d
```

This will start:
- Zookeeper (Port 2181)
- Kafka (Port 9092)
- MySQL for Customer Service (Port 3306)
- MySQL for Account Service (Port 3307)

### 2. Access APIs

- Customer Service: http://localhost:8080
- Account Service: http://localhost:8081
- Swagger UI:
  - Customer Service: http://localhost:8080/swagger-ui.html
  - Account Service: http://localhost:8081/swagger-ui.html

## API Endpoints

### Customer Service

- `GET /api/v1/customers/{identification}` - Get customer
- `POST /api/v1/customers` - Create customer
- `PUT /api/v1/customers/{identification}` - Update customer
- `DELETE /api/v1/customers/{identification}` - Delete customer

### Account Service

- `GET /api/v1/accounts/{number}` - Get account
- `POST /api/v1/accounts` - Create account
- `PUT /api/v1/accounts/{number}` - Update account status
- `DELETE /api/v1/accounts/{number}` - Delete account

- `GET /api/v1/movements/{id}` - Get movement
- `POST /api/v1/movements` - Create movement (deposit/withdraw)

- `GET /reports/{client-id}?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Generate account statement report

## Kafka Topics

- `customer.created` - Published when a customer is created
- `customer.updated` - Published when a customer is updated
- `customer.deleted` - Published when a customer is deleted
- `account.created` - Published when an account is created
- `movement.created` - Published when a movement is created


## Docker Deployment

Build and run with Docker Compose:
```bash
docker-compose up --build
```

For detailed deployment instructions, see [DEPLOYMENT.md](DEPLOYMENT.md).

## Project Structure

```
nttdata/
├── customer-service/     # Customer and Person microservice
├── account-service/      # Account, Movement, and Reports microservice
├── common/              # Shared DTOs and events
└── docker-compose.yml   # Infrastructure orchestration
```

## Notes

- The application uses eventual consistency between microservices via Kafka
- Customer Service publishes events when customers are created/updated/deleted
- Account Service consumes customer events to maintain local cache
- All endpoints are reactive (Mono/Flux)
- Proper error handling with meaningful HTTP status codes

