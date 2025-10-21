#!/bin/bash

# Simple script to add 100 random terms to  Rot-ionary to populate the database for testing

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

LEXICON_URL="http://localhost:8081"

# Sample slang words
WORDS=(
    "yeet" "cap" "bussin" "sus" "bet" "slay" "periodt" "noob" "salty" "ghosted"
    "flex" "vibe" "stan" "tea" "snatched" "fire" "mood" "lowkey" "highkey" "simp"
    "rizz" "main" "slaps" "vibes" "goals" "mood" "lit" "fam" "squad" "bae"
    "thirsty" "clout" "flex" "hype" "savage" "woke" "cringe" "based" "poggers" "noob"
    "toxic" "cancel" "ship" "stan" "fangirl" "fangirl" "fangirl" "fangirl" "fangirl" "fangirl"
    "spill" "tea" "shade" "read" "drag" "serve" "slay" "queen" "king" "legend"
    "iconic" "epic" "legendary" "goat" "boss" "chief" "alpha" "beta" "sigma" "omega"
    "vibe" "energy" "aura" "presence" "swag" "drip" "style" "fashion" "look" "aesthetic"
    "mood" "feeling" "emotion" "sentiment" "attitude" "personality" "character" "trait" "quality" "feature"
    "trend" "fad" "craze" "hype" "buzz" "hype" "excitement" "enthusiasm" "passion" "energy"
)

# Sample definitions
DEFINITIONS=(
    "To throw something with force, often used as an exclamation of excitement"
    "A lie or false statement; 'no cap' means 'no lie'"
    "Something that tastes really good or is excellent"
    "Suspicious or questionable"
    "Agreement or confirmation, like 'okay' or 'sure'"
    "To do something exceptionally well"
    "Emphatic way to end a statement, like 'period'"
    "A beginner or inexperienced person"
    "Being upset or bitter about something"
    "To be ignored or cut off by someone"
    "To show off or boast about something"
    "A feeling or atmosphere"
    "To be a big fan of someone or something"
    "Gossip or inside information"
    "Looking really good or on point"
    "Something that's really good or cool"
    "Relating to or expressing a current feeling"
    "Secretly or quietly"
    "Obviously or openly"
    "Someone who does too much for someone they like"
    "Charisma or charm, especially in romantic contexts"
    "Your primary romantic interest or favorite"
    "Something that sounds really good"
    "Good feelings or atmosphere"
    "Something to aspire to or admire"
    "A current emotional state or feeling"
    "Something exciting or excellent"
    "Close friends or family"
    "A group of close friends"
    "Term of endearment for a romantic partner"
    "Desperately wanting attention or validation"
    "Social influence or popularity"
    "To show off or boast"
    "Excitement or enthusiasm"
    "Ruthless or brutally honest"
    "Aware of social issues and injustices"
    "Cool or admirable"
    "Expression of excitement or approval"
    "A beginner or inexperienced person"
    "Harmful or negative behavior"
    "To stop supporting or following someone"
    "A romantic relationship between two people"
    "To be a devoted fan of someone"
    "To be a devoted fan of someone"
    "To be a devoted fan of someone"
    "To be a devoted fan of someone"
    "To be a devoted fan of someone"
    "To be a devoted fan of someone"
    "To reveal gossip or secrets"
    "Gossip or inside information"
    "Subtle criticism or insult"
    "To criticize or insult someone harshly"
    "To criticize someone severely"
    "To look amazing or perform excellently"
    "To do something exceptionally well"
    "A powerful or admirable woman"
    "A powerful or admirable man"
    "Someone who is legendary or iconic"
    "Something that is iconic or memorable"
    "Something that is impressive or grand"
    "Something that is legendary or iconic"
    "Greatest of all time"
    "Someone in charge or very skilled"
    "The leader or most important person"
    "A dominant personality type"
    "A follower personality type"
    "A lone wolf personality type"
    "A mysterious personality type"
    "A feeling or atmosphere"
    "The power or force someone gives off"
    "The energy someone radiates"
    "The way someone carries themselves"
    "The way someone presents themselves"
    "Cool style or confidence"
    "Really good style or fashion"
    "Personal style or fashion sense"
    "A particular look or appearance"
    "A particular visual style or theme"
    "A current emotional state"
    "An emotional state or feeling"
    "A strong feeling or emotion"
    "A particular attitude or feeling"
    "A particular way of thinking or feeling"
    "A particular characteristic or quality"
    "A particular aspect of someone's personality"
    "A particular feature or characteristic"
    "A particular quality or attribute"
    "A popular trend or style"
    "A temporary popular trend"
    "A sudden popular trend"
    "Excitement or enthusiasm about something"
    "Excitement or buzz around something"
    "Excitement or enthusiasm"
    "Strong enthusiasm or passion"
    "Strong emotional energy"
)

# Sample tags
TAGS=(
    "slang" "gen-z" "internet" "gaming" "social-media" "trending" "popular" "cool" "awesome" "fire"
    "lit" "vibes" "energy" "mood" "feeling" "emotion" "attitude" "personality" "character" "style"
    "fashion" "aesthetic" "look" "appearance" "behavior" "action" "expression" "reaction" "response" "interaction"
    "relationship" "romance" "friendship" "social" "community" "culture" "subculture" "group" "team" "squad"
    "family" "friends" "peers" "generation" "youth" "teen" "adult" "mature" "young" "old"
    "new" "fresh" "original" "creative" "innovative" "unique" "special" "different" "unusual" "rare"
    "common" "popular" "trending" "viral" "hot" "cool" "awesome" "amazing" "incredible" "fantastic"
    "excellent" "outstanding" "remarkable" "impressive" "notable" "significant" "important" "relevant" "useful" "helpful"
    "funny" "humorous" "comedy" "joke" "laugh" "smile" "happy" "joy" "pleasure" "enjoyment"
    "excitement" "enthusiasm" "passion" "energy" "power" "strength" "force" "intensity" "passion" "emotion"
)

# Sample creators
CREATORS=(
    "user123" "slangmaster" "genzuser" "internetkid" "gamerpro" "socialbutterfly" "trendsetter" "coolkid" "fireuser" "litperson"
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

echo -e "${BLUE} Adding 100 random terms to Rot-ionary...${NC}"
echo ""

# Add 100 terms
for i in {1..100}; do
    # Pick random word, definition, tags, and creator
    word="${WORDS[$((RANDOM % ${#WORDS[@]}))]}"
    definition="${DEFINITIONS[$((RANDOM % ${#DEFINITIONS[@]}))]}"
    creator="${CREATORS[$((RANDOM % ${#CREATORS[@]}))]}"
    
    # Pick 2-4 random tags
    num_tags=$((RANDOM % 3 + 2))
    selected_tags=()
    for j in $(seq 1 $num_tags); do
        tag="${TAGS[$((RANDOM % ${#TAGS[@]}))]}"
        selected_tags+=("\"$tag\"")
    done
    
    # Create JSON payload
    json_payload="{
        \"word\": \"$word\",
        \"createdBy\": \"$creator\",
        \"tags\": [$(IFS=,; echo "${selected_tags[*]}")]
    }"
    
    # Create term
    response=$(curl -s -X POST "$LEXICON_URL/api/terms" \
        -H "Content-Type: application/json" \
        -d "$json_payload")
    
    term_id=$(echo "$response" | jq -r '.id // empty')
    
    if [ -n "$term_id" ] && [ "$term_id" != "null" ]; then
        # Add definition
        definition_payload="{
            \"meaning\": \"$definition\",
            \"createdBy\": \"$creator\"
        }"
        
        curl -s -X POST "$LEXICON_URL/api/terms/$term_id/definitions" \
            -H "Content-Type: application/json" \
            -d "$definition_payload" > /dev/null
        
        echo -e "${GREEN}Added term $i/100: '$word' (ID: $term_id)${NC}"
    else
        echo -e "${RED}Failed to add term $i/100: '$word'${NC}"
    fi
    
done

echo ""
echo -e "${GREEN}Successfully added 100 terms to Rot-ionary!${NC}"
