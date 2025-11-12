# Kafka Microservices Project

A microservices architecture project demonstrating event-driven communication using Apache Kafka, Spring Boot, and MongoDB.

---

## 📋 Table of Contents
- [Services Overview](#services-overview)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [API Endpoints](#api-endpoints)
- [Monitoring](#monitoring)
- [Project Status](#project-status)

---

## 🎯 Services Overview

### 1. **orders-service** (Port: 8082)
- Handles order creation and management
- Exposes REST API for order operations
- Produces Kafka events when orders are created
- Database: `ordersdb` (MongoDB on port 27017)

### 2. **payments-service** (Port: 8083)
- Processes payments for orders
- Consumes order events from Kafka
- Produces payment status events
- Database: `paymentsdb` (MongoDB on port 27018)

### 3. **notifications-service** (Port: 8084)
- Sends notifications to users
- Consumes payment events from Kafka
- Database: `notificationsdb` (MongoDB on port 27019)

---

## 🛠 Technology Stack

- **Java 17**
- **Spring Boot 3.2.x**
- **Apache Kafka** - Event streaming platform
- **MongoDB** - NoSQL database (separate instance per service)
- **Docker & Docker Compose** - Container orchestration
- **Lombok** - Reduce boilerplate code
- **Spring Boot Actuator** - Health checks and monitoring
- **Maven** - Build tool

---

## 🏗 Architecture

```
┌─────────────────┐
│  orders-service │ (REST API)
│   Port: 8082    │
└────────┬────────┘
         │ produces events
         ▼
    ┌────────┐
    │  Kafka │ (Event Bus)
    └───┬────┘
        │ consumes events
        ├────────────────────┐
        ▼                    ▼
┌──────────────────┐  ┌─────────────────────┐
│ payments-service │  │ notifications-service│
│   Port: 8083     │  │     Port: 8084       │
└──────────────────┘  └─────────────────────┘

Each service has its own MongoDB instance
```

---

## ✅ Prerequisites

Before running this project, ensure you have:

- ✅ **Java 17** or higher installed
- ✅ **Maven 3.6+** installed
- ✅ **Docker Desktop** installed and running
- ✅ **MongoDB Compass** (optional, for database management)
- ✅ **Git** installed

**Verify installations:**
```bash
java -version
mvn -version
docker --version
docker-compose --version
```

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/2000ujwal/kafka-microservices-project.git
cd kafka-microservices-project
```

### 2. Start Infrastructure (Kafka + MongoDB)

```bash
# Start all containers in detached mode
docker-compose up -d

# Verify all containers are running
docker-compose ps
```

**Expected containers:**
- ✅ zookeeper (port 2181)
- ✅ kafka (ports 9092, 29092)
- ✅ mongodb-orders (port 27017)
- ✅ mongodb-payments (port 27018)
- ✅ mongodb-notifications (port 27019)

### 3. Build All Services

```bash
# Build all services
mvn clean install

# Or build individually
cd orders-services && mvn clean install && cd ..
cd payment-services && mvn clean install && cd ..
cd notifications-service && mvn clean install && cd ..
```

---

## 🏃 Running the Project

### Option 1: Run Each Service Individually (Recommended for Development)

**Terminal 1 - Orders Service:**
```bash
cd orders-services
mvn spring-boot:run
```

**Terminal 2 - Payments Service:**
```bash
cd payment-services
mvn spring-boot:run
```

**Terminal 3 - Notifications Service:**
```bash
cd notifications-service
mvn spring-boot:run
```

### Option 2: Run as JAR Files

```bash
# Build JARs
mvn clean package

# Run services
java -jar orders-services/target/orders-service-0.0.1-SNAPSHOT.jar
java -jar payment-services/target/payments-service-0.0.1-SNAPSHOT.jar
java -jar notifications-service/target/notifications-service-0.0.1-SNAPSHOT.jar
```

---

## 📡 API Endpoints

### Orders Service (Port 8082)

#### Create Order
```bash
POST http://localhost:8082/api/orders
Content-Type: application/json

{
  "productName": "Laptop",
  "quantity": 1,
  "price": 999.99
}
```

#### Get All Orders
```bash
GET http://localhost:8082/api/orders
```

#### Get Order by ID
```bash
GET http://localhost:8082/api/orders/{id}
```

### Health Checks

- Orders Service: http://localhost:8082/actuator/health
- Payments Service: http://localhost:8083/actuator/health
- Notifications Service: http://localhost:8084/actuator/health

---

## 🔍 Monitoring

### MongoDB Compass Connections

Connect to each service's database using MongoDB Compass:

**Orders Database:**
```
mongodb://admin:admin123@localhost:27017/ordersdb?authSource=admin
```

**Payments Database:**
```
mongodb://admin:admin123@localhost:27018/paymentsdb?authSource=admin
```

**Notifications Database:**
```
mongodb://admin:admin123@localhost:27019/notificationsdb?authSource=admin
```

### Docker Logs

```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f kafka
docker-compose logs -f mongodb-orders
```

### Spring Boot Actuator

Access detailed health information:
- http://localhost:8082/actuator/info
- http://localhost:8082/actuator/metrics

---

## 📊 Project Status

### ✅ Day 1 Complete (Project Setup)
- [x] Git repository initialized
- [x] 3 Spring Boot microservices created via Spring Initializr
- [x] All dependencies configured (Kafka, MongoDB, Lombok, Actuator)
- [x] All services compile successfully
- [x] Initial commit and push to GitHub

### ✅ Day 2 Complete (Infrastructure & Configuration)
- [x] Docker Compose setup with Kafka + ZooKeeper
- [x] 3 separate MongoDB instances configured
- [x] application.yml configured for all services
- [x] Services connected to MongoDB and Kafka
- [x] Health checks working via Actuator
- [x] Basic Order entity and REST controller created

### 🚧 Day 3 (In Progress - Domain Implementation)
- [ ] Complete domain models for all services
- [ ] MongoDB repositories for all entities
- [ ] Kafka topic configuration
- [ ] Kafka producers implementation
- [ ] Kafka consumers implementation
- [ ] End-to-end event flow: Order → Payment → Notification

### 📅 Future Enhancements
- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Service Discovery (Eureka)
- [ ] Distributed Tracing (Zipkin)
- [ ] Circuit Breaker (Resilience4j)
- [ ] API Documentation (Swagger/OpenAPI)
- [ ] Unit and Integration Tests
- [ ] Docker images for services
- [ ] Kubernetes deployment

---

## 🛑 Stopping the Project

### Stop Spring Boot Services
Press `Ctrl+C` in each terminal running the services.

### Stop Docker Containers
```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

---

## 🐛 Troubleshooting

### Services Won't Start

**Check if ports are available:**
```bash
# Windows
netstat -ano | findstr :8082
netstat -ano | findstr :27017

# Mac/Linux
lsof -i :8082
lsof -i :27017
```

**Solution:** Stop conflicting services or change ports in `application.yml`

### MongoDB Connection Failed

**Check if MongoDB containers are running:**
```bash
docker-compose ps
docker-compose logs mongodb-orders
```

**Solution:** Restart Docker containers
```bash
docker-compose restart mongodb-orders
```

### Kafka Connection Failed

**Check if Kafka is healthy:**
```bash
docker-compose logs kafka
docker-compose exec kafka kafka-topics --list --bootstrap-server localhost:9092
```

**Solution:** Restart Kafka and ZooKeeper
```bash
docker-compose restart zookeeper kafka
```

### Build Failures

**Clean Maven cache:**
```bash
mvn clean install -U
```

**Delete Maven repository:**
```bash
# Windows
rmdir /s %USERPROFILE%\.m2\repository

# Mac/Linux
rm -rf ~/.m2/repository
```

---

## 📚 Useful Commands

### Maven Commands
```bash
mvn clean                    # Clean build artifacts
mvn compile                  # Compile source code
mvn test                     # Run tests
mvn package                  # Create JAR files
mvn spring-boot:run         # Run Spring Boot application
mvn dependency:tree         # View dependency tree
```

### Docker Commands
```bash
docker-compose up -d         # Start all services
docker-compose down          # Stop all services
docker-compose ps            # List running containers
docker-compose logs -f       # Follow logs
docker-compose restart       # Restart services
docker system prune -a       # Clean up Docker
```

### Git Commands
```bash
git status                   # Check status
git add .                    # Stage all changes
git commit -m "message"     # Commit changes
git push origin main        # Push to GitHub
git pull origin main        # Pull latest changes
```

---

## 👥 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 License

This project is created for learning purposes.

---

## 📧 Contact

- **GitHub:** [@2000ujwal](https://github.com/2000ujwal)
- **Repository:** [kafka-microservices-project](https://github.com/2000ujwal/kafka-microservices-project)

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- Apache Kafka Documentation
- MongoDB Documentation
- Docker Documentation

---

**Last Updated:** Day 2 - Infrastructure Setup Complete ✅