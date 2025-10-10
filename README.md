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
- `term-queries`
- `word-of-the-day`
- `game-results`

## Running the Application

### 1. Install Dependencies

First, install the parent POM and shared domain:

```bash
./mvnw -q -N install
./mvnw -q -pl shared-domain install
```

### 2. Start Services

Start each microservice in separate terminals:

**Lexicon Service (Port 8081):**
```bash
./mvnw -q -f lexicon-service/pom.xml spring-boot:run
```

**Agentic AI Service (Port 8082):**
```bash
GEMINI_API_KEY=$GEMINI_API_KEY ./mvnw -q -f agentic-ai-service/pom.xml spring-boot:run
```

**Dictionary Patron Service (Port 8083):**
```bash
./mvnw -q -f dictionary-patron-service/pom.xml spring-boot:run
```

**Rotle Game Service (Port 8084):**
```bash
./mvnw -q -f rotle-game-service/pom.xml spring-boot:run
```

## API Usage Examples

### Lexicon Service (Port 8081)

**Add a new slang term:**
```bash
curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{
    "word": "yeet",
    "createdBy": "user123",
    "tags": ["slang", "gen-z", "exclamation"]
  }'
```

**Add a definition to a term:**
```bash
curl -X POST http://localhost:8081/api/terms/1/definitions \
  -H "Content-Type: application/json" \
  -d '{
    "meaning": "To throw something with force, often used as an exclamation of excitement",
    "createdBy": "user123"
  }'
```

**Search for terms:**
```bash
# Search by word
curl "http://localhost:8081/api/terms/search?word=yeet"

# Search by tag
curl "http://localhost:8081/api/terms/search?tag=gen-z"

# Get random term
curl "http://localhost:8081/api/terms/random"

# Get random 5-letter term (for Rotle game)
curl "http://localhost:8081/api/terms/random-five"
```

**Get term by ID:**
```bash
curl "http://localhost:8081/api/terms/1"
```

**Update term tags:**
```bash
curl -X PUT http://localhost:8081/api/terms/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tags": ["slang", "gen-z", "exclamation", "gaming"]
  }'
```

**Delete a term:**
```bash
curl -X DELETE http://localhost:8081/api/terms/1
```

### Agentic AI Service (Port 8082)

**Get example sentences:**
```bash
curl -X POST http://localhost:8082/api/ai/example-sentences \
  -H "Content-Type: application/json" \
  -d '{
    "term": "yeet"
  }'
```

**Get etymology:**
```bash
curl -X POST http://localhost:8082/api/ai/etymology \
  -H "Content-Type: application/json" \
  -d '{
    "term": "yeet"
  }'
```

**Suggest tags:**
```bash
curl -X POST http://localhost:8082/api/ai/suggest-tags \
  -H "Content-Type: application/json" \
  -d '{
    "word": "yeet"
  }'
```

### Dictionary Patron Service (Port 8083)

**Get word of the day:**
```bash
curl "http://localhost:8083/api/wotd/current"
```

**Get analytics:**
```bash
curl "http://localhost:8083/api/analytics/top"
```

### Rotle Game Service (Port 8084)

**Start a new game:**
```bash
curl -X POST http://localhost:8084/api/game/start \
  -H "Content-Type: application/json" \
  -d '{
    "userSession": "player123"
  }'
```

**Make a guess:**
```bash
curl -X POST http://localhost:8084/api/game/1/guess \
  -H "Content-Type: application/json" \
  -d '{
    "guess": "yeet"
  }'
```

**Get game state:**
```bash
curl "http://localhost:8084/api/game/1"
```

## Sample Data

Here are some example slang terms you can add to test the system:

```bash
# Add some 5-letter terms for Rotle
curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{"word": "yeet", "createdBy": "test", "tags": ["slang", "gen-z", "exclamation"]}'

curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{"word": "rizz", "createdBy": "test", "tags": ["slang", "gen-z", "noun"]}'

curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{"word": "unc", "createdBy": "test", "tags": ["slang", "gen-z", "adjective"]}'

curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{"word": "slay", "createdBy": "test", "tags": ["slang", "gen-z", "verb"]}'

curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{"word": "buss", "createdBy": "test", "tags": ["slang", "gen-z", "adjective"]}'
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