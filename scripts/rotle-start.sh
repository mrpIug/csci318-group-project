#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8084}
WORD=${1:-}
WORD_ID=${2:-}
SESSION=${3:-cli}
if [[ -z "$WORD" || -z "$WORD_ID" ]]; then
  echo "Usage: $0 <targetWord> <targetWordId> [userSession]" >&2
  exit 1
fi
curl -sS -X POST "$BASE_URL/api/game/start" \
  -H 'Content-Type: application/json' \
  -d "{\"targetWord\":\"$WORD\",\"targetWordId\":$WORD_ID,\"userSession\":\"$SESSION\"}"


