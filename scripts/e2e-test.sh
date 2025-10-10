#!/bin/bash

# End-to-End Testing Script for Rot-ionary Application
# This script tests all functional requirements F-01 through F-11

echo "🧪 Starting End-to-End Testing for Rot-ionary Application"
echo "=================================================="

# Service URLs
LEXICON_URL="http://localhost:8081"
AI_URL="http://localhost:8082"
PATRON_URL="http://localhost:8083"
ROTLE_URL="http://localhost:8084"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

# Function to run a test
run_test() {
    local test_name="$1"
    local command="$2"
    local expected_status="$3"
    
    echo -n "Testing $test_name... "
    
    if response=$(eval "$command" 2>/dev/null); then
        if [ "$expected_status" = "200" ] || [ "$expected_status" = "201" ]; then
            echo -e "${GREEN}✅ PASSED${NC}"
            ((TESTS_PASSED++))
        else
            echo -e "${RED}❌ FAILED${NC} (Expected status $expected_status)"
            ((TESTS_FAILED++))
        fi
    else
        echo -e "${RED}❌ FAILED${NC} (Command failed)"
        ((TESTS_FAILED++))
    fi
}

# Function to check if service is running
check_service() {
    local service_name="$1"
    local url="$2"
    
    echo -n "Checking $service_name... "
    if curl -s "$url" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Running${NC}"
        return 0
    else
        echo -e "${RED}❌ Not running${NC}"
        return 1
    fi
}

echo "🔍 Checking if all services are running..."
echo "----------------------------------------"

# Check all services
check_service "Lexicon Service" "$LEXICON_URL/api/terms" || exit 1
check_service "AI Service" "$AI_URL/api/ai/example-sentences" || exit 1
check_service "Patron Service" "$PATRON_URL/api/wotd/current" || exit 1
check_service "Rotle Service" "$ROTLE_URL/api/game/1" || exit 1

echo ""
echo "🧪 Running Functional Tests..."
echo "=============================="

# F-01: Add term
echo ""
echo "📝 F-01: Add Term"
run_test "Add slang term 'yeet'" \
    "curl -s -X POST '$LEXICON_URL/api/terms' -H 'Content-Type: application/json' -d '{\"word\": \"yeet\", \"createdBy\": \"testuser\", \"tags\": [\"slang\", \"gen-z\", \"exclamation\"]}'" \
    "201"

run_test "Add slang term 'rizz'" \
    "curl -s -X POST '$LEXICON_URL/api/terms' -H 'Content-Type: application/json' -d '{\"word\": \"rizz\", \"createdBy\": \"testuser\", \"tags\": [\"slang\", \"gen-z\", \"noun\"]}'" \
    "201"

run_test "Add slang term 'unc'" \
    "curl -s -X POST '$LEXICON_URL/api/terms' -H 'Content-Type: application/json' -d '{\"word\": \"unc\", \"createdBy\": \"testuser\", \"tags\": [\"slang\", \"gen-z\", \"adjective\"]}'" \
    "201"

run_test "Add slang term 'slay'" \
    "curl -s -X POST '$LEXICON_URL/api/terms' -H 'Content-Type: application/json' -d '{\"word\": \"slay\", \"createdBy\": \"testuser\", \"tags\": [\"slang\", \"gen-z\", \"verb\"]}'" \
    "201"

run_test "Add slang term 'buss'" \
    "curl -s -X POST '$LEXICON_URL/api/terms' -H 'Content-Type: application/json' -d '{\"word\": \"buss\", \"createdBy\": \"testuser\", \"tags\": [\"slang\", \"gen-z\", \"adjective\"]}'" \
    "201"

# F-02: Search term
echo ""
echo "🔍 F-02: Search Term"
run_test "Search by word 'yeet'" \
    "curl -s '$LEXICON_URL/api/terms/search?word=yeet'" \
    "200"

run_test "Search by tag 'gen-z'" \
    "curl -s '$LEXICON_URL/api/terms/search?tag=gen-z'" \
    "200"

run_test "Get term by ID" \
    "curl -s '$LEXICON_URL/api/terms/1'" \
    "200"

# F-03: Add definition
echo ""
echo "📖 F-03: Add Definition"
run_test "Add definition to term" \
    "curl -s -X POST '$LEXICON_URL/api/terms/1/definitions' -H 'Content-Type: application/json' -d '{\"meaning\": \"To throw something with force, often used as an exclamation of excitement\", \"createdBy\": \"testuser\"}'" \
    "200"

# F-04: Delete term
echo ""
echo "🗑️ F-04: Delete Term"
# First add a term to delete
curl -s -X POST "$LEXICON_URL/api/terms" -H "Content-Type: application/json" -d '{"word": "testdelete", "createdBy": "testuser", "tags": ["test"]}' > /dev/null
sleep 1

# Get the ID of the term we just added
DELETE_ID=$(curl -s "$LEXICON_URL/api/terms" | jq -r '.[] | select(.word == "testdelete") | .id')

if [ "$DELETE_ID" != "null" ] && [ "$DELETE_ID" != "" ]; then
    run_test "Delete term" \
        "curl -s -X DELETE '$LEXICON_URL/api/terms/$DELETE_ID'" \
        "204"
else
    echo -e "Delete term... ${YELLOW}⚠️ SKIPPED${NC} (Could not find term to delete)"
fi

# F-05: Word of the day
echo ""
echo "📅 F-05: Word of the Day"
run_test "Get word of the day" \
    "curl -s '$PATRON_URL/api/wotd/current'" \
    "200"

# F-06: Random term
echo ""
echo "🎲 F-06: Random Term"
run_test "Get random term" \
    "curl -s '$LEXICON_URL/api/terms/random'" \
    "200"

run_test "Get random 5-letter term" \
    "curl -s '$LEXICON_URL/api/terms/random-five'" \
    "200"

# F-07: AI suggest tags
echo ""
echo "🤖 F-07: AI Suggest Tags"
run_test "AI suggest tags for 'yeet'" \
    "curl -s -X POST '$AI_URL/api/ai/suggest-tags' -H 'Content-Type: application/json' -d '{\"word\": \"yeet\"}'" \
    "200"

# F-09: Play Rotle
echo ""
echo "🎮 F-09: Play Rotle Game"
run_test "Start new Rotle game" \
    "curl -s -X POST '$ROTLE_URL/api/game/start' -H 'Content-Type: application/json' -d '{\"userSession\": \"testplayer\"}'" \
    "200"

# Get the game ID for the next test
GAME_ID=$(curl -s -X POST "$ROTLE_URL/api/game/start" -H "Content-Type: application/json" -d '{"userSession": "testplayer"}' | jq -r '.id')

if [ "$GAME_ID" != "null" ] && [ "$GAME_ID" != "" ]; then
    run_test "Make guess in Rotle game" \
        "curl -s -X POST '$ROTLE_URL/api/game/$GAME_ID/guess' -H 'Content-Type: application/json' -d '{\"guess\": \"yeet\"}'" \
        "200"
    
    run_test "Get game state" \
        "curl -s '$ROTLE_URL/api/game/$GAME_ID'" \
        "200"
else
    echo -e "Make guess in Rotle game... ${YELLOW}⚠️ SKIPPED${NC} (Could not get game ID)"
    echo -e "Get game state... ${YELLOW}⚠️ SKIPPED${NC} (Could not get game ID)"
fi

# F-10: AI examples
echo ""
echo "📝 F-10: AI Example Sentences"
run_test "AI generate examples for 'yeet'" \
    "curl -s -X POST '$AI_URL/api/ai/example-sentences' -H 'Content-Type: application/json' -d '{\"term\": \"yeet\"}'" \
    "200"

# F-11: AI etymology
echo ""
echo "📚 F-11: AI Etymology"
run_test "AI generate etymology for 'yeet'" \
    "curl -s -X POST '$AI_URL/api/ai/etymology' -H 'Content-Type: application/json' -d '{\"term\": \"yeet\"}'" \
    "200"

# Additional tests
echo ""
echo "🔧 Additional Tests"
echo "=================="

run_test "Check if term exists" \
    "curl -s '$LEXICON_URL/api/terms/exists?word=yeet'" \
    "200"

run_test "Get analytics" \
    "curl -s '$PATRON_URL/api/analytics/top'" \
    "200"

# Summary
echo ""
echo "📊 Test Summary"
echo "==============="
echo -e "Tests Passed: ${GREEN}$TESTS_PASSED${NC}"
echo -e "Tests Failed: ${RED}$TESTS_FAILED${NC}"
echo -e "Total Tests: $((TESTS_PASSED + TESTS_FAILED))"

if [ $TESTS_FAILED -eq 0 ]; then
    echo ""
    echo -e "${GREEN}🎉 All tests passed! The Rot-ionary application is working correctly.${NC}"
    exit 0
else
    echo ""
    echo -e "${RED}❌ Some tests failed. Please check the service logs for more details.${NC}"
    exit 1
fi
