#!/bin/bash

# Script to play 3 Rotle games with random usernames and generate GameCompletedEvent data for analytics

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

ROTLE_URL="http://localhost:8084"

# Sample adjectives for usernames
ADJECTIVES=(
    "cool" "epic" "awesome" "rad" "sick" "fire" "lit" "dope" "fresh" "swag"
    "savage" "woke" "based" "poggers" "toxic" "cringe" "iconic" "legendary" "goat" "boss"
    "alpha" "beta" "sigma" "omega" "vibe" "energy" "aura" "presence" "mood" "feeling"
)

# Sample nouns for usernames
NOUNS=(
    "player" "gamer" "pro" "master" "legend" "boss" "king" "queen" "star" "hero"
    "chief" "alpha" "beta" "sigma" "omega" "vibe" "energy" "aura" "presence" "swag"
    "drip" "style" "fashion" "look" "aesthetic" "mood" "feeling" "emotion" "attitude" "personality"
)

# Sample 5-letter words for guesses
GUESSES=(
    "HELLO" "WORLD" "GAMES" "PLAYS" "WORDS" "SLANG" "TERMS" "COOL" "FIRE" "LIT"
    "VIBES" "MOODS" "SWAG" "DRIP" "STYLE" "AESTH" "TREND" "HYPE" "BUZZ" "ENERGY"
    "POWER" "FORCE" "MIGHT" "STRONG" "BRAVE" "SMART" "QUICK" "FAST" "SLOW" "CALM"
    "PEACE" "LOVE" "HOPE" "DREAM" "GOALS" "PLANS" "IDEAS" "THOUGHTS" "MINDS" "HEARTS"
    "SOULS" "SPIRITS" "GHOSTS" "ANGELS" "DEMONS" "HEROES" "VILLAINS" "FRIENDS" "ENEMIES" "RIVALS"
)

echo -e "${BLUE}Starting Rotle Game Testing Script${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

# Function to generate random username
generate_username() {
    adj="${ADJECTIVES[$RANDOM % ${#ADJECTIVES[@]}]}"
    noun="${NOUNS[$RANDOM % ${#NOUNS[@]}]}"
    num=$((RANDOM % 999 + 1))
    echo "${adj}${noun}${num}"
}

# Function to play a single game
play_game() {
    local username=$1
    local game_num=$2
    
    echo -e "${YELLOW}Game $game_num - Username: $username${NC}"
    echo -e "${YELLOW}----------------------------------------${NC}"
    
    # Start a new game
    echo "Starting new game..."
    game_response=$(curl -s -X POST "$ROTLE_URL/api/game/start" \
        -H "Content-Type: application/json" \
        -d "{\"userSession\": \"$username\"}")
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to start game. Is the Rotle service running on port 8084?${NC}"
        return 1
    fi
    
    # Extract game ID from response
    game_id=$(echo "$game_response" | jq -r '.id // empty')
    if [ -z "$game_id" ]; then
        echo -e "${RED}Failed to get game ID from response: $game_response${NC}"
        return 1
    fi
    
    echo -e "${GREEN}Game started with ID: $game_id${NC}"
    
    # Get the target word (for debugging - normally you wouldn't know this)
    game_state=$(curl -s "$ROTLE_URL/api/game/$game_id")
    target_word=$(echo "$game_state" | jq -r '.targetWord // empty')
    echo "🎯 Target word: $target_word"
    
    # Make 6 random guesses (to simulate a full game)
    echo "Making guesses..."
    for attempt in {1..6}; do
        # Generate a random 5-letter word guess
        guess="${GUESSES[$RANDOM % ${#GUESSES[@]}]}"
        echo "  Attempt $attempt: $guess"
        
        guess_response=$(curl -s -X POST "$ROTLE_URL/api/game/$game_id/guess" \
            -H "Content-Type: application/json" \
            -d "{\"guess\": \"$guess\"}")
        
        # Check if game is over
        if echo "$guess_response" | jq -e '.won // .gameOver' > /dev/null 2>&1; then
            won=$(echo "$guess_response" | jq -r '.won // false')
            if [ "$won" = "true" ]; then
                echo -e "${GREEN}Won the game!${NC}"
            else
                echo -e "${RED}Game over - didn't win${NC}"
            fi
            break
        fi
        
        # Small delay between guesses
        sleep 0.5
    done
    
    echo -e "${GREEN}Game $game_num completed${NC}"
    return 0
}

# Check if Rotle service is running
echo -e "${BLUE}Checking if Rotle service is running...${NC}"
if ! curl -s "$ROTLE_URL/api/game/health" > /dev/null 2>&1; then
    echo -e "${RED}Rotle service is not running on port 8084${NC}"
    echo "Please start it with: ./mvnw -q -f rotle-game-service/pom.xml spring-boot:run"
    exit 1
fi
echo -e "${GREEN}Rotle service is running${NC}"

# Play 3 games
echo ""
echo -e "${BLUE}Playing 3 Rotle games...${NC}"
for i in {1..3}; do
    username=$(generate_username)
    play_game "$username" $i
    
    # Small delay between games
    if [ $i -lt 3 ]; then
        echo -e "${YELLOW}Waiting 2 seconds before next game...${NC}"
        sleep 2
    fi
done

echo ""
echo -e "${GREEN}All games completed${NC}"
echo -e "${BLUE}Check the analytics dashboard: curl -s 'http://localhost:8082/api/game-stats/dashboard' | jq .${NC}"
echo -e "${BLUE}=====================================${NC}"