# Rot-ionary: A Slang Dictionary Microservice Application

A collaborative dictionary for modern slang terms like "yeet", "rizz", "unc", and more, featuring agentic AI-powered services, dictionary analytics, and a Wordle-style game called "Rotle".

## Rot-ionary's Core Features

### AI Agent Capabilities

Rot-ionary's **Agentic AI Service** provides a **hierarchical multi-agent system** featuring a coordinator agent that manages 3 specialised worker agents, each with session-based memory (remembers last 20 messages). These are described below:

#### Coordinator Agent
- Main entry point for all AI requests (unless you specifically ask for the specialised agents)
- Handles term searches, creation, and lookups
- Automatically determines whether to handle requests directly or consult specialists using `CoordinatorTools`

#### Tag Suggestion Agent (Specialist)
- Analyses terms and suggests relevant tags (formality, context, usage type, cultural relevance)
- Can add tags directly to terms in the database using `addTagToTerm` tool
- Suggests tag categories such as gen-z, gaming, social-media, workplace, profanity, etc.
- Delegated to by coordinator when users need help categorising or organising terms

#### Sentence Generation Agent (Specialist)
- Creates customised example sentences showing how new age slang terms are used in real-world contexts
- Takes user preferences (tone, context, audience, number of examples) into account
- Delegated to by coordinator when users want example sentences or usage demonstrations

#### Etymology Agent (Specialist)
- Explains term origins and evolution, focusing on internet/social media usage
- Discusses cultural context, usage patterns, and how terms spread
- Mentions first known usage, popularisation sources, and linguistic features
- Can dive deeper into related terms, timeline, or cultural significance
- Delegated to by coordinator when users ask about term origins or etymology

**Note**: All agents can automatically create new terms in the database if they don't exist, using the `createTerm` tool, but they must first retrieve the desired term definition and username from the user.

### Analytics System

#### Word of the Day

The **Dictionary Analytics Service** automatically displays the Word of the Day, which updates in real-time using event-driven architecture and Kafka stream processing. Rot-ionary processes `TermQueriedEvent` as they arrive from the Lexicon Service and immediately updates the Word of the Day based on the most queried term.

#### Rotle Dashboard

The **Dictionary Analytics Service** provides both historical and real-time game analytics for Rotle through database event querying and stream processing. When Rot-ionary users complete Rotle games, `GameCompletedEvent` is published and processed to generate insights of:

- Total Rotle games played
- Average Rotle game win rate
- Average attempts to finish a Rotle game
- Target words chosen most frequently by Rotle
- Most recently completed Rotle games
- Rot-ionary-wide player performance rankings and statistics  

### Rotle Mechanics

**Rotle** is a Wordle-style game using 5-letter slang terms, rather than legitimate english terms like normal Wordle. It works like this:

- **Target Words**: Each target word chosen per game is 5 letters long, and also must already be in the Rot-ionary's database
- **Game Rules**: 
  - You get 6 attempts to guess the target word
  - Each guess you make must be exactly 5 letters long, just like the target word
  - The game ends when you either guess the target word correctly or run out of attempts
- **Feedback System**: To assist with guessing the target word correctly, you receive a guess result for every guess attempt you make. It comes in  format `XXXXX` and has either one of these 3 letters in each position meaning various things:
  - **C** (Correct): Letter is in the word and in the correct position
  - **P** (Present): Letter is in the word but in the wrong position  
  - **A** (Absent): Letter is not in the word at all

So if your game's target word is `griddy` and you make a guess `gronk`, you will receive guess feedback `CCAAA`.

## Core Dependencies

| Dependency | Purpose |
|------------|---------|
| **Spring Boot** | Main application framework |
| **Spring Web** | RESTful API endpoints |
| **Spring Data JPA** | Database persistence layer |
| **H2 Database** | In-memory database |
| **Spring Cloud Stream** | Event-driven framework |
| **Spring Cloud Stream Kafka** | Kafka binder for Spring Cloud Stream |
| **Kafka Streams** | Stream processing |
| **Spring Kafka** | Direct Kafka integration |
| **LangChain4j** | Hierarchical multi-agent system orchestration with ReAct prompting |
| **Spring Boot Actuator** | Debugging and logging within Rot-ionary |
| **Google AI Gemini** | LLM provider for Rot-ionary's AI agents |
## Configuration

### Environment Variables

#### Mac/Linux
Set your Gemini API key in your current terminal session:

```bash
export GEMINI_API_KEY="your-gemini-api-key-here"
```

#### Windows (PowerShell)
Set your Gemini API key in your current PowerShell session:

```powershell
$env:GEMINI_API_KEY = "your-gemini-api-key-here"
```

### Kafka Setup

#### Mac/Linux

In Kafka root folder, clear zookeeper data directory first if zookeeper isn't starting:

```bash
rm -rf /tmp/zookeeper
```

Ensure Kafka is running with these commands:

```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
```bash
./bin/kafka-server-start.sh ./config/server.properties
```

#### Windows (PowerShell)

In Kafka root folder, clear zookeeper data directory first if zookeeper isn't starting:

```powershell
Remove-Item -Recurse -Force C:\tmp\zookeeper -ErrorAction SilentlyContinue
```

Ensure Kafka is running with these commands:

```powershell
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
```powershell
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

## Running the Application

### 1. Install Dependencies

#### Mac/Linux

First, install the parent POM and shared domain:

```bash
./mvnw -q -N install
```
```bash
./mvnw -q -pl shared-domain install
```

#### Windows (PowerShell)

First, install the parent POM and shared domain:

```powershell
.\mvnw.cmd -q -N install
```
```powershell
.\mvnw.cmd -q -pl shared-domain install
```

### 2. Start Services

*Start Kafka first, then all microservices can be started in parallel.*

#### Mac/Linux

Start Kafka (if not already running):
```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
```bash
./bin/kafka-server-start.sh ./config/server.properties
```

Start each microservice in the Rot-ionary root folder, each in a separate terminal:

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

#### Windows (PowerShell)

**Start Kafka** (if not already running):
```powershell
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
```powershell
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

**Start each microservice** in separate terminals:

**Lexicon Service (Port 8081):**
```powershell
.\mvnw.cmd -q -f lexicon-service/pom.xml spring-boot:run
```

**Dictionary Analytics Service (Port 8082):**
```powershell
.\mvnw.cmd -q -f dictionary-analytics-service/pom.xml spring-boot:run
```

**Agentic AI Service (Port 8083):**
```powershell
.\mvnw.cmd -q -f agentic-ai-service/pom.xml spring-boot:run
```

**Rotle Game Service (Port 8084):**
```powershell
.\mvnw.cmd -q -f rotle-game-service/pom.xml spring-boot:run
```

## API Usage Examples

### Lexicon Service (Port 8081)

#### Term Management

**Create a new term:**

Mac/Linux:
```bash
curl -X POST http://localhost:8081/api/terms \
  -H "Content-Type: application/json" \
  -d '{
    "word": "yeet",
    "createdBy": "user123",
    "tags": ["slang", "gen-z", "exclamation"]
  }' | jq
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8081/api/terms `
  -H "Content-Type: application/json" `
  -d '{\"word\": \"yeet\", \"createdBy\": \"user123\", \"tags\": [\"slang\", \"gen-z\", \"exclamation\"]}' | jq
```

**Create 100 random terms:**

Mac/Linux:
```bash
scripts/add-100-terms.sh
```

*This script generates 100 random slang terms with definitions and tags to populate the database for testing.*

**Update term tags:**

Mac/Linux:
```bash
curl -X PUT http://localhost:8081/api/terms/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tags": ["slang", "gen-z", "exclamation", "gaming"]
  }' | jq
```

Windows (PowerShell):
```powershell
curl -X PUT http://localhost:8081/api/terms/1 `
  -H "Content-Type: application/json" `
  -d '{\"tags\": [\"slang\", \"gen-z\", \"exclamation\", \"gaming\"]}' | jq
```

**Delete a term:**
```bash
curl -X DELETE http://localhost:8081/api/terms/(id) | jq
```
*Replace (id) with a valid Rot-ionary term id.*

**Delete all terms:**
```bash
curl -X DELETE http://localhost:8081/api/terms | jq
```

#### Search and Discovery

**Get all terms:**
```bash
curl "http://localhost:8081/api/terms" | jq
```

**Get term by ID:**
```bash
curl "http://localhost:8081/api/terms/(id)" | jq
```
*Replace (id) with a valid Rot-ionary term id.*

**Search by word:**
```bash
curl "http://localhost:8081/api/terms/search?word=yeet" | jq
```

**Search by tag:**
```bash
curl "http://localhost:8081/api/terms/search?tag=gen-z" | jq
```

**Get random term:**
```bash
curl "http://localhost:8081/api/terms/random" | jq
```

**Get random 5-letter term (see valid terms for rotle):**
```bash
curl "http://localhost:8081/api/terms/random-five" | jq
```

#### Definitions

**Get definitions for a term:**
```bash
curl "http://localhost:8081/api/terms/(id)/definitions" | jq
```
*Replace (id) with a valid Rot-ionary term id.*

**Add definition to a term:**

Mac/Linux:
```bash
curl -X POST http://localhost:8081/api/terms/(id)/definitions \
  -H "Content-Type: application/json" \
  -d '{
    "meaning": "To throw something with force, often used as an exclamation of excitement",
    "createdBy": "user123"
  }' | jq
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8081/api/terms/(id)/definitions `
  -H "Content-Type: application/json" `
  -d '{\"meaning\": \"To throw something with force, often used as an exclamation of excitement\", \"createdBy\": \"user123\"}' | jq
```
*Replace (id) with a valid Rot-ionary term id.*

### Dictionary Analytics Service (Port 8082)

#### Word of the Day Analytics

**Get word of the day:**
```bash
curl "http://localhost:8082/api/wotd/realtime" | jq
```

**WOTD Analytics test script:**

Mac/Linux:
```bash
scripts/test-term-analytics.sh
```

*This script creates a random slang term, adds a definition, and queries it 5 times to generate analytics data for testing Word of the Day.*

#### Game Analytics

**Get Rotle game real-time analytics (30 second window):**

```bash
curl "http://localhost:8082/api/game-stats/dashboard/realtime" | jq
```

**Get Rotle game all-time analytics:**

```bash
curl "http://localhost:8082/api/game-stats/dashboard/historical" | jq
```

**Rotle Game Analytics test script:**

Mac/Linux:
```bash
scripts/play-rotle-games.sh
```

*This script plays 3 complete Rotle games with random usernames to generate game analytics data for testing Rotle's analytics.*

### Agentic AI Service (Port 8083)

*Replace (term) with a term that is already in Rot-ionary's database, or one that isn't to see the `createTerm` tool usage.*

*Use the same `sessionId` to continue conversations. Each agent remembers the last 20 messages per session.*

Mac/Linux:

**Etymology questions - automatically delegated to EtymologyAgent**:

```bash
curl -G "http://localhost:8083/api/ai/chat" \
  --data-urlencode "sessionId=user1" \
  --data-urlencode "userMessage=What is the etymology of '(term)'?"
```

**Example sentences - automatically delegated to SentenceGenerationAgent**:

```bash
curl -G "http://localhost:8083/api/ai/chat" \
  --data-urlencode "sessionId=user1" \
  --data-urlencode "userMessage=Give me 3 casual examples using '(term)'"
```

**Tag suggestions - automatically delegated to Tag Specialist**:
```bash
curl -G "http://localhost:8083/api/ai/chat" \
  --data-urlencode "sessionId=user1" \
  --data-urlencode "userMessage=What tags should '(term)' have?"
```

**Database operations - handled directly by coordinator**:

```bash
curl -G "http://localhost:8083/api/ai/chat" \
  --data-urlencode "sessionId=user1" \
  --data-urlencode "userMessage=Search for the term '(term)'"
```

Windows (PowerShell):

**Etymology questions**:

```powershell
curl -G "http://localhost:8083/api/ai/chat" `
  --data-urlencode "sessionId=user1" `
  --data-urlencode "userMessage=What is the etymology of '(term)'?"
```

**Example sentences**:

```powershell
curl -G "http://localhost:8083/api/ai/chat" `
  --data-urlencode "sessionId=user1" `
  --data-urlencode "userMessage=Give me 3 casual examples using '(term)'"
```

**Tag suggestions**:

```powershell
curl -G "http://localhost:8083/api/ai/chat" `
  --data-urlencode "sessionId=user1" `
  --data-urlencode "userMessage=What tags should '(term)' have?"
```

**Database operations**:

```powershell
curl -G "http://localhost:8083/api/ai/chat" `
  --data-urlencode "sessionId=user1" `
  --data-urlencode "userMessage=Search for the term '(term)'"
```

### Rotle Game Service (Port 8084)

**Start a new game:**

Mac/Linux:
```bash
curl -X POST http://localhost:8084/api/game/start \
  -H "Content-Type: application/json" \
  -d '{
    "userSession": "player123"
  }' | jq
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8084/api/game/start `
  -H "Content-Type: application/json" `
  -d '{\"userSession\": \"player123\"}' | jq
```

**Make a guess in a game:**

Mac/Linux:
```bash
curl -X POST http://localhost:8084/api/game/(id)/guess \
  -H "Content-Type: application/json" \
  -d '{
    "guess": "hello"
  }' | jq
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8084/api/game/(id)/guess `
  -H "Content-Type: application/json" `
  -d '{\"guess\": \"hello\"}' | jq
```
*Replace (id) with an active Rotle game id.*

**Get game state:**
```bash
curl "http://localhost:8084/api/game/(id)" | jq
```
*Replace (id) with an existing Rotle game id.*

## Technical Things

### Building the Project

#### Mac/Linux
```bash
./mvnw clean install
```

#### Windows (PowerShell)
```powershell
.\mvnw.cmd clean install
```

### Kill Specific Microservices

#### Mac/Linux
```bash
lsof -ti:(ms port) | xargs kill -9
```

#### Windows (PowerShell)
```powershell
Get-NetTCPConnection -LocalPort (ms port) -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique | ForEach-Object { Stop-Process -Id $_ -Force }
```

*Replace (ms port) with an active port.*

**Ports for reference:**
- Lexicon - 8081
- Dictionary Analytics - 8082
- Agentic AI - 8083
- Rotle - 8084

### Kill All Microservices

#### Mac/Linux
```bash
pkill -f "spring-boot"
```

#### Windows (PowerShell)
```powershell
Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -like "*spring-boot*" } | Stop-Process -Force
``` 