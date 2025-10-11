# Rot-ionary: Slang Dictionary Microservice Application

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

Ensure Apache Kafka is running with these commands (run in kafka folder):
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
./bin/kafka-server-start.sh ./config/server.properties

Rot-ionary will automatically create the required topics:
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
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
```

**Start each microservice** in separate terminals:

**Lexicon Service (Port 8081):**
```bash
./mvnw -q -f lexicon-service/pom.xml spring-boot:run
```

**Dictionary Analytics Service (Port 8082):**
```bash
./mvnw -q -f dictionary-analytics-service/pom.xml spring-boot:run
```

**Agentic AI Service (Port 8083):**
```bash
./mvnw -q -f agentic-ai-service/pom.xml spring-boot:run
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
**Get all terms but you can actually read them:**
```bash
scripts/list-terms.sh
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

**Create 100 random terms:**
```bash
scripts/add-100-terms.sh
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

Rot-ionary's AI services provides three conversational agents with session-based memory. Each agent can have back-and-forth conversations with users.

**Tag Suggestion Agent** - Suggests and adds tags to terms:
```bash
GET /api/ai/tag-agent?sessionId={id}&userMessage={message}
curl -G "http://localhost:8083/api/ai/tag-agent" \
  --data-urlencode "sessionId=1" \
  --data-urlencode "userMessage=I need tags for the term 'yeet'"
```

**Sentence Generation Agent** - Creates customized example sentences:
```bash
GET /api/ai/sentence-agent?sessionId={id}&userMessage={message}
curl -G "http://localhost:8083/api/ai/sentence-agent" \
  --data-urlencode "sessionId=2" \
  --data-urlencode "userMessage=Generate 3 casual sentences for 'lit'"
```

**Etymology Agent** - Explains word origins and evolution:
```bash
GET /api/ai/etymology-agent?sessionId={id}&userMessage={message}
curl -G "http://localhost:8083/api/ai/etymology-agent" \
  --data-urlencode "sessionId=3" \
  --data-urlencode "userMessage=What's the etymology of 'sus'?"
```

**Note:** Use the same `sessionId` to continue a conversation. Each agent remembers the last 20 messages per session.

### Dictionary Analytics Service (Port 8082)

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

### Building the Project

```bash
./mvnw clean install
```

### Kill specific microservices
```bash
lsof -ti:<ms port> | xargs kill -9
```

### Kill all microservices
```bash
pkill -f "spring-boot"
```