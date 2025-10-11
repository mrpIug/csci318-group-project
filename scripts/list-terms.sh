#!/bin/bash

# List all terms with term ID, definition, tags, creator, and timestamps

LEXICON_URL="http://localhost:8081"

DATA=$(curl -sf "$LEXICON_URL/api/terms")
if [ $? -ne 0 ] || [ -z "$DATA" ] || [ "$DATA" = "null" ] || [ "$DATA" = "[]" ]; then
    echo "No terms found or Lexicon service unavailable."
    exit 1
fi

echo "Rot-ionary Terms"
echo "=================="
echo

echo "$DATA" | jq -r '.[] | 
"\(.id) | \(.word)
Definition: \(.definition.meaning // "No definition available")
Tags: \(.tags | join(", "))
Created by: \(.createdBy) on \(.createdAt | split("T")[0])
Last modified: \(.updatedAt | split("T")[0])
"'
