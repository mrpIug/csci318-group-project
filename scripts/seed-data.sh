#!/bin/bash

# Seed test data for Rot-ionary application
# This script adds sample slang terms with definitions and tags

echo "🌱 Seeding test data for Rot-ionary..."

# Base URL for lexicon service
LEXICON_URL="http://localhost:8081"

# Function to add a term
add_term() {
    local word="$1"
    local created_by="$2"
    local tags="$3"
    
    echo "Adding term: $word"
    curl -s -X POST "$LEXICON_URL/api/terms" \
        -H "Content-Type: application/json" \
        -d "{
            \"word\": \"$word\",
            \"createdBy\": \"$created_by\",
            \"tags\": $tags
        }" > /dev/null
    
    # Wait a moment for the term to be created
    sleep 0.5
}

# Function to add a definition
add_definition() {
    local term_id="$1"
    local meaning="$2"
    local created_by="$3"
    
    echo "Adding definition for term ID $term_id"
    curl -s -X POST "$LEXICON_URL/api/terms/$term_id/definitions" \
        -H "Content-Type: application/json" \
        -d "{
            \"meaning\": \"$meaning\",
            \"createdBy\": \"$created_by\"
        }" > /dev/null
    
    sleep 0.5
}

# Check if lexicon service is running
echo "Checking if lexicon service is running..."
if ! curl -s "$LEXICON_URL/api/terms" > /dev/null; then
    echo "❌ Lexicon service is not running on $LEXICON_URL"
    echo "Please start the lexicon service first:"
    echo "./mvnw -q -f lexicon-service/pom.xml spring-boot:run"
    exit 1
fi

echo "✅ Lexicon service is running"

# Add 5-letter terms (for Rotle game)
echo "Adding 5-letter slang terms for Rotle game..."

add_term "yeet" "testuser" '["slang", "gen-z", "exclamation", "gaming"]'
add_term "rizz" "testuser" '["slang", "gen-z", "noun", "social-media"]'
add_term "unc" "testuser" '["slang", "gen-z", "adjective", "informal"]'
add_term "slay" "testuser" '["slang", "gen-z", "verb", "compliment"]'
add_term "buss" "testuser" '["slang", "gen-z", "adjective", "compliment"]'
add_term "cap" "testuser" '["slang", "gen-z", "noun", "lie"]'
add_term "bet" "testuser" '["slang", "gen-z", "exclamation", "agreement"]'
add_term "flex" "testuser" '["slang", "gen-z", "verb", "show-off"]'
add_term "mood" "testuser" '["slang", "gen-z", "noun", "feeling"]'
add_term "vibe" "testuser" '["slang", "gen-z", "noun", "atmosphere"]'

# Add some non-5-letter terms for variety
echo "Adding additional slang terms..."

add_term "noob" "testuser" '["slang", "gaming", "noun", "beginner"]'
add_term "salty" "testuser" '["slang", "gaming", "adjective", "upset"]'
add_term "based" "testuser" '["slang", "internet", "adjective", "cool"]'
add_term "cringe" "testuser" '["slang", "internet", "adjective", "embarrassing"]'
add_term "stan" "testuser" '["slang", "social-media", "verb", "fan"]'

# Add definitions for some terms
echo "Adding definitions..."

# Get the first few terms and add definitions
TERMS=$(curl -s "$LEXICON_URL/api/terms" | jq -r '.[0:5] | .[] | "\(.id):\(.word)"')

for term_info in $TERMS; do
    IFS=':' read -r term_id term_word <<< "$term_info"
    
    case $term_word in
        "yeet")
            add_definition "$term_id" "To throw something with force, often used as an exclamation of excitement or energy" "testuser"
            ;;
        "rizz")
            add_definition "$term_id" "Charisma or charm, especially in romantic or social situations" "testuser"
            ;;
        "unc")
            add_definition "$term_id" "Short for 'uncle', used to describe someone who is cool or admirable" "testuser"
            ;;
        "slay")
            add_definition "$term_id" "To do something exceptionally well or look amazing" "testuser"
            ;;
        "buss")
            add_definition "$term_id" "Something that is really good or impressive" "testuser"
            ;;
    esac
done

echo "✅ Test data seeding complete!"
echo ""
echo "You can now:"
echo "1. Search for terms: curl \"$LEXICON_URL/api/terms/search?word=yeet\""
echo "2. Get random term: curl \"$LEXICON_URL/api/terms/random\""
echo "3. Get random 5-letter term: curl \"$LEXICON_URL/api/terms/random-five\""
echo "4. Start a Rotle game: curl -X POST http://localhost:8084/api/game/start -H \"Content-Type: application/json\" -d '{\"userSession\": \"test\"}'"
echo ""
echo "Total terms added: 15 (10 five-letter terms for Rotle + 5 additional terms)"
