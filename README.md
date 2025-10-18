# Rot-ionary: A Slang Dictionary Microservice Application

A collaborative dictionary for modern slang terms like "yeet", "rizz", "unc", and more, featuring agentic AI-powered services, dictionary analytics, and a Wordle-style game called "Rotle".

## Rot-ionary Features

### AI Agent Capabilities

Rot-ionary's **Agentic AI Service** provides 3 specialised conversational agents, each with session-based memory (remembers last 20 messages):

#### Tag Suggestion Agent
- Analyses terms and suggests relevant tags (formality, context, usage type, cultural relevance)
- Can add tags directly to terms in the database using `addTagToTerm` tool
- Suggests tag categories such as gen-z, gaming, social-media, workplace, profanity etc.

#### Sentence Generation Agent  
- Creates customised example sentences showing how new age slang terms are used in real-world context
- Adapts to user preferences (tone, context, audience, number of examples)
- Can demonstrate various tones of examples such as formal, casual, humorous, sarcastic, etc.
- Can demonstrate contexts: social media, work, texting, conversation, gaming

#### Etymology Agent
- Explains word origins and evolution, focusing on internet/social media usage
- Discusses cultural context, usage patterns, and how terms spread
- Mentions first known usage, popularisation sources, and linguistic features
- Can dive deeper into related terms, timeline, or cultural significance

**Important**: All agents can automatically create new terms in the database, if they do not already exist, using the `createTerm` tool, but they must retrieve the desired term definition and username from the user first.

### Word of the Day System

The **Dictionary Analytics Service** automatically displays the Word of the Day, which updates every 5 seconds (made quicker for demonstration purposes), and is calculated by analysing what term has the most query events attached to them in the last hour.

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

## Prerequisites

- Java 21
- Maven 3.6+
- Apache Kafka
- Gemini API Key

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

**Base URL:** `http://localhost:8081/api/terms`

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
  }'
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8081/api/terms `
  -H "Content-Type: application/json" `
  -d '{\"word\": \"yeet\", \"createdBy\": \"user123\", \"tags\": [\"slang\", \"gen-z\", \"exclamation\"]}'
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
  }'
```

Windows (PowerShell):
```powershell
curl -X PUT http://localhost:8081/api/terms/1 `
  -H "Content-Type: application/json" `
  -d '{\"tags\": [\"slang\", \"gen-z\", \"exclamation\", \"gaming\"]}'
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

**Get all terms:**
```bash
curl "http://localhost:8081/api/terms"
```

**Get all terms but you can actually read them:**

Mac/Linux:
```bash
scripts/list-terms.sh
```

*This script formats the JSON response in a readable way, showing terms with their IDs, words, tags, and definitions.*

**Get term by ID:**
```bash
curl "http://localhost:8081/api/terms/(id)"
```
*Replace (id) with a valid Rot-ionary term id.*

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

Mac/Linux:
```bash
curl -X POST http://localhost:8081/api/terms/(id)/definitions \
  -H "Content-Type: application/json" \
  -d '{
    "meaning": "To throw something with force, often used as an exclamation of excitement",
    "createdBy": "user123"
  }'
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8081/api/terms/(id)/definitions `
  -H "Content-Type: application/json" `
  -d '{\"meaning\": \"To throw something with force, often used as an exclamation of excitement\", \"createdBy\": \"user123\"}'
```
*Replace (id) with a valid Rot-ionary term id.*

### Dictionary Analytics Service (Port 8082)

**Base URL:** `http://localhost:8082/api`

**Get current word of the day:**
```bash
curl "http://localhost:8082/api/wotd/current"
```

*Returns the most queried term from the last hour, updated every 5 seconds. Response includes the term ID, word, query count, and date.*

### Agentic AI Service (Port 8083)

**Base URL:** `http://localhost:8083/api/ai`

*Use the same `sessionId` to continue conversations. Each agent remembers the last 20 messages per session.*

**Tag Suggestion Agent** - Suggests and adds tags to terms:

Mac/Linux:
```bash
curl -G "http://localhost:8083/api/ai/tag-agent" \
  --data-urlencode "sessionId=1" \
  --data-urlencode "userMessage=I need tags for the term '(term)'"
```

Windows (PowerShell):
```powershell
curl -G "http://localhost:8083/api/ai/tag-agent" `
  --data-urlencode "sessionId=1" `
  --data-urlencode "userMessage=I need tags for the term '(term)'"
```
*Replace (term) with a term that is already in Rot-ionary's databases.*

**Sentence Generation Agent** - Creates customised example sentences:

Mac/Linux:
```bash
curl -G "http://localhost:8083/api/ai/sentence-agent" \
  --data-urlencode "sessionId=2" \
  --data-urlencode "userMessage=Generate 3 casual sentences for the term '(term)'"
```

Windows (PowerShell):
```powershell
curl -G "http://localhost:8083/api/ai/sentence-agent" `
  --data-urlencode "sessionId=2" `
  --data-urlencode "userMessage=Generate 3 casual sentences for the term '(term)'"
```
*Replace (term) with a term that is already in Rot-ionary's databases.*

**Etymology Agent** - Explains word origins and evolution:

Mac/Linux:
```bash
curl -G "http://localhost:8083/api/ai/etymology-agent" \
  --data-urlencode "sessionId=3" \
  --data-urlencode "userMessage=What is the etymology of the term '(term)'?"
```

Windows (PowerShell):
```powershell
curl -G "http://localhost:8083/api/ai/etymology-agent" `
  --data-urlencode "sessionId=3" `
  --data-urlencode "userMessage=What is the etymology of the term '(term)'?"
```
*Replace (term) with a term that is already in Rot-ionary's databases.*

### Rotle Game Service (Port 8084)

**Base URL:** `http://localhost:8084/api/game`

**Start a new game:**

Mac/Linux:
```bash
curl -X POST http://localhost:8084/api/game/start \
  -H "Content-Type: application/json" \
  -d '{
    "userSession": "player123"
  }'
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8084/api/game/start `
  -H "Content-Type: application/json" `
  -d '{\"userSession\": \"player123\"}'
```

**Make a guess in a game:**

Mac/Linux:
```bash
curl -X POST http://localhost:8084/api/game/(id)/guess \
  -H "Content-Type: application/json" \
  -d '{
    "guess": "hello"
  }'
```

Windows (PowerShell):
```powershell
curl -X POST http://localhost:8084/api/game/(id)/guess `
  -H "Content-Type: application/json" `
  -d '{\"guess\": \"hello\"}'
```
*Replace (id) with an active Rotle game id.*

**Get game state:**
```bash
curl "http://localhost:8084/api/game/(id)"
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