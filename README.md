# Kafka Microservices Project

## Services
1. **orders-service** - Handles order creation and management
2. **payments-service** - Processes payments for orders
3. **notifications-service** - Sends notifications to users

## Architecture
- **Communication**: Apache Kafka
- **Database**: MongoDB (one instance per service)
- **Orchestration**: Docker Compose (Day 2)