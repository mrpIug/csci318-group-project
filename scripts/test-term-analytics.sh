#!/bin/bash

# Script to add a random term and query it 5 times to generate TermQueriedEvent data for analytics

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

LEXICON_URL="http://localhost:8081"

# Sample slang prefixes
PREFIXES=(
    "yeet" "rizz" "unc" "bet" "cap" "no" "fr" "ong" "bussin" "slay"
    "fire" "lit" "dope" "fresh" "swag" "savage" "woke" "based" "poggers" "toxic"
    "cringe" "iconic" "legendary" "goat" "boss" "chief" "alpha" "beta" "sigma" "omega"
)

# Sample suffixes
SUFFIXES=(
    "ing" "er" "ed" "ly" "ness" "tion" "ment" "able" "ful" "less"
    "ism" "ist" "ive" "ous" "ful" "less" "like" "wise" "ward" "ways"
)

# Sample base words
WORDS=(
    "fire" "drip" "vibe" "mood" "energy" "flex" "clout" "swag" "drip" "hype"
    "power" "force" "might" "strength" "brave" "smart" "quick" "fast" "slow" "calm"
    "peace" "love" "hope" "dream" "goals" "plans" "ideas" "thoughts" "minds" "hearts"
    "souls" "spirits" "ghosts" "angels" "demons" "heroes" "villains" "friends" "enemies" "rivals"
)

# Sample definitions
DEFINITIONS=(
    "A modern slang term expressing excitement or approval"
    "Used to describe something that's really cool or impressive"
    "A trendy way to say something is amazing or awesome"
    "Contemporary slang for something that's fire or lit"
    "A popular term among young people for something great"
    "Modern vernacular for expressing positive feelings"
    "A hip way to describe something that's really good"
    "Contemporary slang meaning something is excellent"
    "A trendy term for something that's absolutely fantastic"
    "Modern way to say something is really impressive"
    "A cool expression used by the younger generation"
    "Slang term that's become popular on social media"
    "A hip way to describe something awesome"
    "Contemporary term for something that's really good"
    "Modern slang for expressing approval or excitement"
    "A trendy way to say something is cool or impressive"
    "Popular term among Gen Z for something great"
    "Contemporary slang for something that's fire"
    "Modern way to express positive feelings"
    "A hip term for something that's really awesome"
)

# Sample tags
TAGS=(
    "slang" "gen-z" "social-media" "gaming" "workplace" "casual" "trending" "modern" "youth" "internet"
    "cool" "awesome" "fire" "lit" "vibes" "energy" "mood" "feeling" "emotion" "attitude"
    "personality" "character" "style" "fashion" "aesthetic" "look" "appearance" "behavior" "action" "expression"
    "reaction" "response" "interaction" "relationship" "romance" "friendship" "social" "community" "culture" "subculture"
    "group" "team" "squad" "family" "friends" "peers" "generation" "teen" "adult" "mature"
    "young" "old" "new" "fresh" "original" "creative" "innovative" "unique" "special" "different"
    "unusual" "rare" "common" "popular" "viral" "hot" "amazing" "incredible" "fantastic" "excellent"
    "outstanding" "remarkable" "impressive" "notable" "significant" "important" "relevant" "useful" "helpful" "funny"
    "humorous" "comedy" "joke" "laugh" "smile" "happy" "joy" "pleasure" "enjoyment" "excitement"
    "enthusiasm" "passion" "power" "strength" "force" "intensity" "emotion" "feeling" "mood" "energy"
)

# Sample creators
CREATORS=(
    "testuser" "slangmaster" "genzuser" "internetkid" "gamerpro" "socialbutterfly" "trendsetter" "coolkid" "fireuser" "litperson"
    "vibemaster" "energydude" "moodyuser" "feelingperson" "attitudeuser" "personalitypro" "characterfan" "stylequeen" "fashionista" "aestheticfan"
    "lookmaster" "appearancepro" "behavioruser" "actionfan" "expressionist" "reactionpro" "responseuser" "interactionfan" "relationshipuser" "romancepro"
    "friendshipfan" "socialuser" "communitypro" "culturefan" "subcultureuser" "groupmember" "teamplayer" "squadmember" "familyfan" "frienduser"
    "peerpro" "generationuser" "youthfan" "teenuser" "adultpro" "matureuser" "youngfan" "olduser" "newpro" "freshuser"
    "originalfan" "creativeuser" "innovativepro" "uniquefan" "specialuser" "differentpro" "unusualfan" "rareuser" "commonpro" "popularfan"
    "trendinguser" "viralpro" "hotfan" "cooluser" "awesomepro" "amazingfan" "incredibleuser" "fantasticpro" "excellentfan" "outstandinguser"
    "remarkablepro" "impressivefan" "notableuser" "significantpro" "importantfan" "relevantuser" "usefulpro" "helpfulfan" "funnyuser" "humorouspro"
    "comedyfan" "jokeuser" "laughpro" "smilefan" "happyuser" "joypro" "pleasurefan" "enjoymentuser" "excitementpro" "enthusiasmfan"
    "passionuser" "energypro" "powerfan" "strengthuser" "forcepro" "intensityfan" "passionuser" "emotionpro" "feelingfan" "mooduser"
)

echo -e "${BLUE}Starting Term Testing Script${NC}"
echo -e "${BLUE}===============================${NC}"
echo ""

# Function to generate random slang term
generate_term() {
    prefix="${PREFIXES[$RANDOM % ${#PREFIXES[@]}]}"
    suffix="${SUFFIXES[$RANDOM % ${#SUFFIXES[@]}]}"
    word="${WORDS[$RANDOM % ${#WORDS[@]}]}"
    
    # Randomly choose format
    case $((RANDOM % 3)) in
        0) echo "${prefix}${suffix}" ;;
        1) echo "${word}${suffix}" ;;
        2) echo "${prefix}${word}" ;;
    esac
}

# Function to generate random definition
generate_definition() {
    echo "${DEFINITIONS[$RANDOM % ${#DEFINITIONS[@]}]}"
}

# Function to generate random tags
generate_tags() {
    num_tags=$((RANDOM % 3 + 2))  # 2-4 tags
    
    tags=()
    for ((i=0; i<num_tags; i++)); do
        tag="${TAGS[$RANDOM % ${#TAGS[@]}]}"
        # Avoid duplicates
        if [[ ! " ${tags[@]} " =~ " ${tag} " ]]; then
            tags+=("$tag")
        fi
    done
    
    # Convert array to JSON array format
    printf '%s\n' "${tags[@]}" | jq -R . | jq -s .
}

# Check if Lexicon service is running
echo -e "${BLUE}Checking if Lexicon service is running...${NC}"
if ! curl -s "$LEXICON_URL/api/terms" > /dev/null 2>&1; then
    echo -e "${RED}Lexicon service is not running on port 8081${NC}"
    echo "Please start it with: ./mvnw -q -f lexicon-service/pom.xml spring-boot:run"
    exit 1
fi
echo -e "${GREEN}Lexicon service is running${NC}"

# Generate random term data
term_word=$(generate_term)
definition=$(generate_definition)
tags=$(generate_tags)
username="${CREATORS[$RANDOM % ${#CREATORS[@]}]}$((RANDOM % 1000))"

echo ""
echo -e "${YELLOW}Creating new term...${NC}"
echo "Word: $term_word"
echo "Definition: $definition"
echo "Tags: $tags"
echo "Created by: $username"

# Create the term
echo ""
echo -e "${BLUE}Adding term to lexicon...${NC}"
create_response=$(curl -s -X POST "$LEXICON_URL/api/terms" \
    -H "Content-Type: application/json" \
    -d "{
        \"word\": \"$term_word\",
        \"createdBy\": \"$username\",
        \"tags\": $tags
    }")

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to create term${NC}"
    exit 1
fi

# Extract term ID
term_id=$(echo "$create_response" | jq -r '.id // empty')
if [ -z "$term_id" ]; then
    echo -e "${RED}Failed to get term ID from response: $create_response${NC}"
    exit 1
fi

echo -e "${GREEN}Term created with ID: $term_id${NC}"

# Add definition to the term
echo ""
echo -e "${BLUE}Adding definition to term...${NC}"
definition_response=$(curl -s -X POST "$LEXICON_URL/api/terms/$term_id/definitions" \
    -H "Content-Type: application/json" \
    -d "{
        \"meaning\": \"$definition\",
        \"createdBy\": \"$username\"
    }")

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to add definition${NC}"
    exit 1
fi

echo -e "${GREEN}Definition added successfully${NC}"

# Query the term 5 times
echo ""
echo -e "${BLUE}Querying term 5 times to generate analytics data...${NC}"
for i in {1..5}; do
    echo -e "${YELLOW}Query $i: Searching for '$term_word'${NC}"
    
    # Search by word
    search_response=$(curl -s "$LEXICON_URL/api/terms/search?word=$term_word")
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Search successful${NC}"
    else
        echo -e "${RED}Search failed${NC}"
    fi
    
    # Small delay between queries
    sleep 0.5
done

echo ""
echo -e "${GREEN}Term testing completed${NC}"
echo -e "${BLUE}===============================${NC}"