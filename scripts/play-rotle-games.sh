#!/bin/bash

# Script to play 3 Rotle games with a different target word for each game but the same username across all 3 games

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

# Sample words for guesses
GUESSES=(
    "HELLO" "WORLD" "GAMES" "PLAYS" "WORDS" "SLANG" "TERMS" "VIBES" "MOODS" "DRIPS"
    "STYLE" "TREND" "HYPES" "POWER" "FORCE" "MIGHT" "BRAVE" "SMART" "QUICK" "PEACE"
    "DREAM" "GOALS" "PLANS" "MINDS" "SOULS" "ANGEL" "DEMON" "FIRES" "QUEEN" "PROUD"
    "CAUSE" "LOGIC" "CHILL" "FRESH" "THINK" "TRICK" "WOVEN" "TIGER" "BLAZE" "EVENT"
    "POINT" "CRAVE" "STARS" "HEART" "GIANT" "MAGIC" "NOBLE" "FAITH" "GRACE" "GLORY"
)

echo -e "${BLUE}Starting Rotle Game Testing Script${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

# Generate random username
generate_username() {
    adj="${ADJECTIVES[$RANDOM % ${#ADJECTIVES[@]}]}"
    noun="${NOUNS[$RANDOM % ${#NOUNS[@]}]}"
    num=$((RANDOM % 999 + 1))
    echo "${adj}${noun}${num}"
}

# Play a single game
play_game() {
    local username=$1
    local game_num=$2
    local word_index=$3

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
    
    # Generate 6 unique random guesses
    guesses_for_game=()
    used_indices=()
    while [ "${#guesses_for_game[@]}" -lt 6 ]; do
        r=$((RANDOM % ${#GUESSES[@]}))
        if [[ ! " ${used_indices[@]} " =~ " $r " ]]; then
            used_indices+=("$r")
            guesses_for_game+=("${GUESSES[$r]}")
        fi
    done

    echo "Making guesses..."

    won=0
    game_over=0
    for attempt in {1..6}; do
        # Stop if game is already over
        if [ $game_over -eq 1 ]; then
            break
        fi

        guess="${guesses_for_game[$((attempt-1))]}"
        
        # Validate guess length
        if [ "${#guess}" -ne 5 ]; then
            echo -e "${RED}  Error: Guess '$guess' is not 5 characters (${#guess} chars)${NC}"
            continue
        fi

        echo "  Attempt $attempt: $guess"

        guess_response=$(curl -s -X POST "$ROTLE_URL/api/game/$game_id/guess" \
            -H "Content-Type: application/json" \
            -d "{\"guess\": \"$guess\"}")

        # Check for API errors
        if echo "$guess_response" | jq -e '.error' > /dev/null 2>&1; then
            error_msg=$(echo "$guess_response" | jq -r '.error // .message // "Unknown error"')
            echo -e "${RED}    Error: $error_msg${NC}"
            continue
        fi

        # Check if game is won
        won_val=$(echo "$guess_response" | jq -r '.won // false')
        game_over_val=$(echo "$guess_response" | jq -r '.gameOver // false')
        
        if [ "$won_val" = "true" ]; then
            echo -e "${GREEN}    ✓ Won the game in $attempt attempt(s)!${NC}"
            won=1
            game_over=1
        elif [ "$game_over_val" = "true" ]; then
            echo -e "${RED}    ✗ Game over - didn't win${NC}"
            game_over=1
        fi

        sleep 0.5
    done

    if [ $won -eq 0 ] && [ $game_over -eq 0 ]; then
        echo -e "${RED}  Reached maximum 6 attempts without winning${NC}"
    fi

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

# Pick a random username for all 3 games
username=$(generate_username)

# Pick 3 different words for the 3 games
total_words=${#GUESSES[@]}
if [ "$total_words" -lt 3 ]; then
    echo -e "${RED}Not enough words in GUESSES array to run this script${NC}"
    exit 1
fi

used_word_indices=()
while [ "${#used_word_indices[@]}" -lt 3 ]; do
    idx=$((RANDOM % total_words))
    if [[ ! " ${used_word_indices[@]} " =~ " $idx " ]]; then
        used_word_indices+=("$idx")
    fi
done

# Play 3 games total with the same username, each using a different picked word
echo ""
echo -e "${BLUE}Playing 3 Rotle games with the same username: ${username}${NC}"
for i in {1..3}; do
    word_index=${used_word_indices[$((i-1))]}
    play_game "$username" $i $word_index

    if [ $i -lt 3 ]; then
        echo -e "${YELLOW}Waiting 2 seconds before next game...${NC}"
        sleep 2
    fi
done

echo ""
echo -e "${GREEN}All games completed${NC}"
echo -e "${BLUE}=====================================${NC}"