# Rot-ionary: AI-Powered Slang Dictionary Microservice Application

A collaborative dictionary for modern slang terms like "yeet", "rizz", "unc" and more, featuring AI-powered etymology analysis and a Wordle-style game called "Rotle".

## Features

- **Slang Dictionary**: Add, edit, and search modern slang terms with definitions and tags
- **AI Integration**: Get etymology explanations and example sentences using Google Gemini AI
- **Rotle Game**: Play a Wordle-style game using 5-letter slang terms
- **Real-time Analytics**: Track trending terms and word-of-the-day features
- **Event-Driven Architecture**: Microservices communicate via Apache Kafka

## Prerequisites

- Java 21
- Maven 3.6+
- Apache Kafka (running on localhost:9092)
- Google Gemini API Key

## Configuration

### Environment Variables

Set your Google Gemini API key:

```bash
export GEMINI_API_KEY="your-gemini-api-key-here"
```

### Kafka Setup

Ensure Apache Kafka is running on localhost:9092. The application will automatically create the required topics:
- `term.queried` - Term query events from lexicon service
- `wotd.updates` - Word of the day updates from stream processing
- `game.completed` - Game completion events from Rotle game service

## Running the Application

### 1. Install Dependencies

First, install the parent POM and shared domain:

```bash
./mvnw -q -N install
./mvnw -q -pl shared-domain install
```

### 2. Start Services

**Important**: Start Kafka first, then all services can be started in parallel.

**Start Kafka** (if not already running):
```bash
# Start Zookeeper and Kafka (adjust paths as needed)
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
```

**Start each microservice** in separate terminals:

**Lexicon Service (Port 8081):**
```bash
./mvnw -q -f lexicon-service/pom.xml spring-boot:run
```

**Agentic AI Service (Port 8083):**
```bash
GEMINI_API_KEY=$GEMINI_API_KEY ./mvnw -q -f agentic-ai-service/pom.xml spring-boot:run
```

**Dictionary Patron Service (Port 8082):**
```bash
./mvnw -q -f dictionary-patron-service/pom.xml spring-boot:run
```

**Rotle Game Service (Port 8084):**
```bash
./mvnw -q -f rotle-game-service/pom.xml spring-boot:run
```

## API Usage Examples

### Lexicon Service (Port 8081)

**Base URL:** `http://localhost:8081/api/terms`

#### Term Management

**Get all terms:**
```bash
GET /api/terms
curl "http://localhost:8081/api/terms"
```

**Get term by ID:**
```bash
GET /api/terms/{id}
curl "http://localhost:8081/api/terms/1"
```

**Create a new term:**
```bash
POST /api/terms
curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{
    "word": "yeet",
    "createdBy": "user123",
    "tags": ["slang", "gen-z", "exclamation"]
  }'
```

**Update term tags:**
```bash
PUT /api/terms/{id}
curl -X PUT http://localhost:8081/api/terms/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tags": ["slang", "gen-z", "exclamation", "gaming"]
  }'
```

**Delete a term:**
```bash
DELETE /api/terms/{id}
curl -X DELETE http://localhost:8081/api/terms/1
```

**Delete all terms:**
```bash
DELETE /api/terms
curl -X DELETE http://localhost:8081/api/terms
```

#### Search and Discovery

**Search by word:**
```bash
GET /api/terms/search?word={word}
curl "http://localhost:8081/api/terms/search?word=yeet"
```

**Search by tag:**
```bash
GET /api/terms/search?tag={tag}
curl "http://localhost:8081/api/terms/search?tag=gen-z"
```

**Get random term:**
```bash
GET /api/terms/random
curl "http://localhost:8081/api/terms/random"
```

**Get random 5-letter term (for Rotle game):**
```bash
GET /api/terms/random-five
curl "http://localhost:8081/api/terms/random-five"
```

#### Definitions

**Get definitions for a term:**
```bash
GET /api/terms/{id}/definitions
curl "http://localhost:8081/api/terms/1/definitions"
```

**Add definition to a term:**
```bash
POST /api/terms/{id}/definitions
curl -X POST http://localhost:8081/api/terms/1/definitions \
  -H "Content-Type: application/json" \
  -d '{
    "meaning": "To throw something with force, often used as an exclamation of excitement",
    "createdBy": "user123"
  }'
```

### Agentic AI Service (Port 8083)

**Base URL:** `http://localhost:8083/api/ai`

**Get example sentences:**
```bash
POST /api/ai/example-sentences
curl -X POST http://localhost:8083/api/ai/example-sentences \
  -H "Content-Type: application/json" \
  -d '{
    "term": "yeet"
  }'
```

**Get etymology analysis:**
```bash
POST /api/ai/etymology
curl -X POST http://localhost:8083/api/ai/etymology \
  -H "Content-Type: application/json" \
  -d '{
    "term": "yeet"
  }'
```

**Suggest tags for a word:**
```bash
POST /api/ai/suggest-tags
curl -X POST http://localhost:8083/api/ai/suggest-tags \
  -H "Content-Type: application/json" \
  -d '{
    "word": "yeet"
  }'
```

### Dictionary Patron Service (Port 8082)

**Base URL:** `http://localhost:8082/api`

**Get current word of the day:**
```bash
GET /api/wotd/current
curl "http://localhost:8082/api/wotd/current"
```

**Get top queried terms analytics:**
```bash
GET /api/analytics/top?window={window}&limit={limit}
curl "http://localhost:8082/api/analytics/top?window=24h&limit=10"
```

**Parameters:**
- `window`: Time window (e.g., "24h", "7d") - defaults to "24h"
- `limit`: Number of results to return - defaults to 5

### Rotle Game Service (Port 8084)

**Base URL:** `http://localhost:8084/api/game`

**Start a new game:**
```bash
POST /api/game/start
curl -X POST http://localhost:8084/api/game/start \
  -H "Content-Type: application/json" \
  -d '{
    "userSession": "player123"
  }'
```

**Make a guess in a game:**
```bash
POST /api/game/{id}/guess
curl -X POST http://localhost:8084/api/game/1/guess \
  -H "Content-Type: application/json" \
  -d '{
    "guess": "hello"
  }'
```

**Get game state:**
```bash
GET /api/game/{id}
curl "http://localhost:8084/api/game/1"
```


## Architecture

The application follows Domain-Driven Design principles with four bounded contexts:

1. **Lexicon Service**: Manages slang terms and definitions
2. **Agentic AI Service**: Provides AI-powered etymology and examples
3. **Dictionary Patron Service**: Handles analytics and trending terms
4. **Rotle Game Service**: Implements the Wordle-style game

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **H2 Database** (for local development)
- **Apache Kafka** (for event-driven communication)
- **Google Gemini AI** (for AI features)
- **LangChain4j** (for AI integration)
- **Maven** (for build management)

## Development

### Building the Project

```bash
./mvnw clean install
```

### Running Tests

```bash
./mvnw test
```