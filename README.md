# Rot-ionary: Slang Dictionary Microservice Application

A collaborative dictionary for modern slang terms like "yeet", "rizz", "unc", and more, featuring agentic AI-powered features, analytics, and a Wordle-style game called "Rotle".

## Prerequisites

- Java 21
- Maven 3.6+
- Apache Kafka
- Gemini API Key

## Configuration

### Environment Variables

Set your Gemini API key in your current terminal session to interact with the AI microservice:

```bash
export GEMINI_API_KEY="your-gemini-api-key-here"
```

### Kafka Setup

Ensure Apache Kafka is running with these commands (run in kafka folder):
```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
```bash
./bin/kafka-server-start.sh ./config/server.properties
```

## Running the Application

### 1. Install Dependencies

First, install the parent POM and shared domain:

```bash
./mvnw -q -N install
./mvnw -q -pl shared-domain install
```

### 2. Start Services

*Start Kafka first, then all services can be started in parallel.*

**Start Kafka** (if not already running):
```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
```bash
./bin/kafka-server-start.sh ./config/server.properties
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
curl "http://localhost:8081/api/terms"
```
**Get all terms but you can actually read them:**
```bash
scripts/list-terms.sh
```

**Get term by ID:**
```bash
curl "http://localhost:8081/api/terms/(id)"
```
*Replace (id) with a valid Rot-ionary term id.*

**Create a new term:**
```bash
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
curl -X PUT http://localhost:8081/api/terms/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tags": ["slang", "gen-z", "exclamation", "gaming"]
  }'
```

**Delete a term:**
```bash
curl -X DELETE http://localhost:8081/api/terms/(id)
```
*Replace (id) with a valid Rot-ionary term id.*


**Delete all terms:**
```bash
curl -X DELETE http://localhost:8081/api/terms
```

#### Search and Discovery

**Search by word:**
```bash
curl "http://localhost:8081/api/terms/search?word=yeet"
```

**Search by tag:**
```bash
curl "http://localhost:8081/api/terms/search?tag=gen-z"
```

**Get random term:**
```bash
curl "http://localhost:8081/api/terms/random"
```

**Get random 5-letter term (see valid terms for rotle):**
```bash
curl "http://localhost:8081/api/terms/random-five"
```

#### Definitions

**Get definitions for a term:**
```bash
curl "http://localhost:8081/api/terms/(id)/definitions"
```
*Replace (id) with a valid Rot-ionary term id.*


**Add definition to a term:**
```bash
curl -X POST http://localhost:8081/api/terms/(id)/definitions \
  -H "Content-Type: application/json" \
  -d '{
    "meaning": "To throw something with force, often used as an exclamation of excitement",
    "createdBy": "user123"
  }'
```
*Replace (id) with a valid Rot-ionary term id.*

### Dictionary Analytics Service (Port 8082)

**Base URL:** `http://localhost:8082/api`

**Get current word of the day:**
```bash
curl "http://localhost:8082/api/wotd/current"
```

### Agentic AI Service (Port 8083)

**Base URL:** `http://localhost:8083/api/ai`

Rot-ionary's AI services provide three conversational agents with session-based memory. 

**Note**: Each service needs the target term to be in the Rot-ionary database already - If the term is absent, each agent can utilise the createTerm tool to add the desired word to the database automatically, provided they are given the word's definition and the user's username.

**Tag Suggestion Agent** - Suggests and adds tags to terms:
```bash
curl -G "http://localhost:8083/api/ai/tag-agent" \
  --data-urlencode "sessionId=1" \
  --data-urlencode "userMessage=I need tags for the term '(term)'"
```
*Replace (term) with a term that is already in Rot-ionary's databases.*


**Sentence Generation Agent** - Creates customized example sentences:
```bash
curl -G "http://localhost:8083/api/ai/sentence-agent" \
  --data-urlencode "sessionId=2" \
  --data-urlencode "userMessage=Generate 3 casual sentences for the term '(term)'"
```
*Replace (term) with a term that is already in Rot-ionary's databases.*

**Etymology Agent** - Explains word origins and evolution:
```bash
curl -G "http://localhost:8083/api/ai/etymology-agent" \
  --data-urlencode "sessionId=3" \
  --data-urlencode "userMessage=What is the etymology of the term '(term)'?"
```
*Replace (term) with a term that is already in Rot-ionary's databases.*

**Note:** Use the same `sessionId` to continue a conversation. Each agent remembers the last 20 messages per session.

### Rotle Game Service (Port 8084)

**Base URL:** `http://localhost:8084/api/game`

**Start a new game:**
```bash
curl -X POST http://localhost:8084/api/game/start \
  -H "Content-Type: application/json" \
  -d '{
    "userSession": "player123"
  }'
```

**Make a guess in a game:**
```bash
curl -X POST http://localhost:8084/api/game/(id)/guess \
  -H "Content-Type: application/json" \
  -d '{
    "guess": "hello"
  }'
```
*Replace (id) with an active Rotle game id.*

Each guess includes the attempt details and updated `availableLetters` list.


**Get game state:**
```bash
curl "http://localhost:8084/api/game/(id)"
```
*Replace (id) with an existing Rotle game id.*

## Technical Things

### Building the Project

```bash
./mvnw clean install
```

### Kill specific microservices
```bash
lsof -ti:(ms port) | xargs kill -9
```
*Replace (ms port) with an active port.*

Ports for reference: 
- Lexicon - 8081
- Dictionary Analytics - 8082
- Agentic AI - 8083
- Rotle Game - 8084

### Kill all microservices
```bash
pkill -f "spring-boot"
```