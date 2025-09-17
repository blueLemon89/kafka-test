# Kafka Microservices Project

This project demonstrates microservices communication using Apache Kafka as a message broker. It consists of two Spring Boot services:

- **Order Service** (Producer): Creates orders and publishes them to Kafka
- **Inventory Service** (Consumer): Consumes order events and processes inventory

## Project Structure

```
kafka-microservices/
├── docker-compose.yml          # Kafka infrastructure
├── order-service/              # Producer service
│   ├── src/main/java/com/example/orderservice/
│   │   ├── OrderServiceApplication.java
│   │   ├── controller/OrderController.java
│   │   ├── service/OrderService.java
│   │   ├── producer/OrderProducer.java
│   │   ├── config/KafkaProducerConfig.java
│   │   └── model/Order.java
│   ├── src/main/resources/application.yml
│   └── pom.xml
└── inventory-service/           # Consumer service
    ├── src/main/java/com/example/inventoryservice/
    │   ├── InventoryServiceApplication.java
    │   ├── controller/InventoryController.java
    │   ├── service/InventoryService.java
    │   ├── consumer/OrderConsumer.java
    │   ├── config/KafkaConsumerConfig.java
    │   └── model/Order.java
    ├── src/main/resources/application.yml
    └── pom.xml
```

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose

## Getting Started

### 1. Start Kafka Infrastructure

```bash
cd kafka-microservices
docker-compose up -d
```

This will start:
- Zookeeper (port 2181)
- Kafka (port 9092)
- Kafka UI (port 8080) - Web interface to monitor Kafka

### 2. Start the Services

**Terminal 1 - Order Service:**
```bash
cd order-service
mvn spring-boot:run
```
Order Service will run on port 8081.

**Terminal 2 - Inventory Service:**
```bash
cd inventory-service
mvn spring-boot:run
```
Inventory Service will run on port 8082.

### 3. Test the Application

**Create an order:**
```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "product-1",
    "quantity": 5,
    "customerEmail": "customer@example.com"
  }'
```

**Check service health:**
```bash
curl http://localhost:8081/api/orders/health
curl http://localhost:8082/api/inventory/health
```

## Kafka Concepts Demonstrated

### 1. **Producer Configuration**
- **Location**: `order-service/src/main/java/com/example/orderservice/config/KafkaProducerConfig.java`
- **Key Concepts**:
  - `BOOTSTRAP_SERVERS_CONFIG`: Kafka cluster connection
  - `KEY_SERIALIZER_CLASS_CONFIG`: How to serialize message keys
  - `VALUE_SERIALIZER_CLASS_CONFIG`: How to serialize message values (JSON)
  - `ACKS_CONFIG`: Message acknowledgment level ("all" for strongest durability)
  - `ENABLE_IDEMPOTENCE_CONFIG`: Prevents duplicate messages

### 2. **Consumer Configuration**
- **Location**: `inventory-service/src/main/java/com/example/inventoryservice/config/KafkaConsumerConfig.java`
- **Key Concepts**:
  - `GROUP_ID_CONFIG`: Consumer group for load balancing
  - `AUTO_OFFSET_RESET_CONFIG`: Where to start reading ("earliest" or "latest")
  - `ENABLE_AUTO_COMMIT_CONFIG`: Manual vs automatic offset commits
  - `JsonDeserializer.TRUSTED_PACKAGES`: Security for JSON deserialization

### 3. **Message Publishing**
- **Location**: `order-service/src/main/java/com/example/orderservice/producer/OrderProducer.java`
- **Key Concepts**:
  - `KafkaTemplate`: Spring abstraction for sending messages
  - Asynchronous publishing with callbacks
  - Message key (orderId) for partitioning
  - Error handling and logging

### 4. **Message Consumption**
- **Location**: `inventory-service/src/main/java/com/example/inventoryservice/consumer/OrderConsumer.java`
- **Key Concepts**:
  - `@KafkaListener`: Annotation-based message consumption
  - Manual acknowledgment for better control
  - Access to message metadata (topic, partition, offset)
  - Error handling

## Kafka Features Explored

1. **Topics & Partitions**: Messages are published to the "order-events" topic
2. **Consumer Groups**: Inventory service uses "inventory-service-group"
3. **Serialization**: JSON serialization for complex objects
4. **Acknowledgments**: Manual acknowledgment for guaranteed processing
5. **Idempotency**: Producer idempotence to prevent duplicates
6. **Durability**: "all" acknowledgment for maximum durability

## Monitoring

### Kafka UI
Visit http://localhost:8080 to:
- View topics and their messages
- Monitor consumer groups and lag
- See partition details and offsets

### Application Logs
Both services provide detailed logging of:
- Message publishing/consumption
- Kafka metadata (topic, partition, offset)
- Processing status and errors

## Test Scenarios

1. **Basic Flow**: Send order → See inventory processing
2. **Multiple Orders**: Send several orders quickly
3. **Invalid Products**: Try ordering non-existent products
4. **High Quantity**: Order more than available stock
5. **Service Restart**: Stop/start services to see message replay

## Sample Test Data

```bash
# Valid orders
curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{"productId": "product-1", "quantity": 10, "customerEmail": "test1@example.com"}'
curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{"productId": "product-2", "quantity": 5, "customerEmail": "test2@example.com"}'

# Invalid product
curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{"productId": "product-999", "quantity": 1, "customerEmail": "test3@example.com"}'

# Insufficient stock
curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{"productId": "product-1", "quantity": 200, "customerEmail": "test4@example.com"}'
```

## Cleanup

```bash
# Stop services (Ctrl+C in each terminal)
# Stop Kafka infrastructure
docker-compose down
```

## Key Learning Points

1. **Asynchronous Communication**: Services communicate without direct API calls
2. **Decoupling**: Services can be developed, deployed, and scaled independently
3. **Reliability**: Messages are persisted and can be replayed
4. **Scalability**: Multiple consumer instances can process messages in parallel
5. **Monitoring**: Rich metadata helps with debugging and monitoring

This project provides a solid foundation for understanding Kafka in microservices architecture!