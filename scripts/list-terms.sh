#!/bin/bash

# Simple Terms List - Human-friendly format
# Shows term ID, definition, tags, creator, and timestamps

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

# Base URL for lexicon service
LEXICON_URL="http://localhost:8081"

# Function to show usage
show_usage() {
    echo -e "${CYAN}📝 Simple Terms List${NC}"
    echo "=================="
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo -e "${YELLOW}OPTIONS:${NC}"
    echo "  --limit N      - Show only first N terms (default: all)"
    echo "  --word WORD    - Search for specific word"
    echo "  --tag TAG      - Search by tag"
    echo "  --id ID        - Show specific term by ID"
    echo "  -h, --help     - Show this help"
    echo ""
    echo -e "${YELLOW}EXAMPLES:${NC}"
    echo "  $0                    # Show all terms"
    echo "  $0 --limit 10         # Show first 10 terms"
    echo "  $0 --word yeet        # Show specific word"
    echo "  $0 --tag gen-z        # Show terms with gen-z tag"
    echo "  $0 --id 5             # Show term with ID 5"
}

# Function to format timestamp
format_timestamp() {
    local timestamp="$1"
    # Convert ISO timestamp to readable format
    echo "$timestamp" | sed 's/T/ /' | sed 's/\.[0-9]*//' | sed 's/+00:00//'
}

# Function to get and display terms
show_terms() {
    local endpoint="$1"
    local word="$2"
    local tag="$3"
    local limit="$4"
    local id="$5"
    
    # Get data from API
    case "$endpoint" in
        "all")
            if [ -n "$limit" ]; then
                DATA=$(curl -s "$LEXICON_URL/api/terms" | jq ".[0:$limit]")
            else
                DATA=$(curl -s "$LEXICON_URL/api/terms")
            fi
            ;;
        "search")
            if [ -n "$word" ]; then
                DATA=$(curl -s "$LEXICON_URL/api/terms/search?word=$word" | jq '.')
            elif [ -n "$tag" ]; then
                DATA=$(curl -s "$LEXICON_URL/api/terms/search?tag=$tag" | jq '.')
            else
                echo -e "${RED}Error: Search requires --word or --tag parameter${NC}"
                exit 1
            fi
            ;;
        "id")
            if [ -n "$id" ]; then
                DATA=$(curl -s "$LEXICON_URL/api/terms/$id" | jq '.')
            else
                echo -e "${RED}Error: ID endpoint requires --id parameter${NC}"
                exit 1
            fi
            ;;
    esac
    
    # Check if data is empty
    if [ "$DATA" = "null" ] || [ "$DATA" = "[]" ] || [ -z "$DATA" ]; then
        echo -e "${YELLOW}⚠️  No terms found${NC}"
        exit 0
    fi
    
    # Display terms in human-friendly format
    if [ "$endpoint" = "id" ]; then
        # Single term format
        echo "$DATA" | jq -r '
            "\(.id) | \(.word)
   Definition: \(.definition.meaning // "No definition available")
   Tags: \(.tags | join(", "))
   Created by: \(.createdBy) on \(.createdAt | split("T")[0])
   Last modified: \(.updatedAt | split("T")[0])
   "'
    else
        # Multiple terms format
        echo "$DATA" | jq -r '.[] | 
            "\(.id) | \(.word)
   Definition: \(.definition.meaning // "No definition available")
   Tags: \(.tags | join(", "))
   Created by: \(.createdBy) on \(.createdAt | split("T")[0])
   Last modified: \(.updatedAt | split("T")[0])
   "'
    fi
}

# Parse command line arguments
ENDPOINT="all"
WORD=""
TAG=""
LIMIT=""
ID=""

while [[ $# -gt 0 ]]; do
    case $1 in
        --word)
            WORD="$2"
            ENDPOINT="search"
            shift 2
            ;;
        --tag)
            TAG="$2"
            ENDPOINT="search"
            shift 2
            ;;
        --limit)
            LIMIT="$2"
            shift 2
            ;;
        --id)
            ID="$2"
            ENDPOINT="id"
            shift 2
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Check if lexicon service is running
if ! curl -s "$LEXICON_URL/api/terms" > /dev/null; then
    echo -e "${RED}❌ Lexicon service is not running on $LEXICON_URL${NC}"
    echo "Please start the lexicon service first:"
    echo "cd lexicon-service && mvn spring-boot:run"
    exit 1
fi

# Show header
echo -e "${WHITE}📚 Rot-ionary Terms${NC}"
echo "=================="
echo ""

# Show terms
show_terms "$ENDPOINT" "$WORD" "$TAG" "$LIMIT" "$ID"
