#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8084}
GAME_ID=${1:-}
GUESS=${2:-}
if [[ -z "$GAME_ID" || -z "$GUESS" ]]; then
  echo "Usage: $0 <gameId> <guess>" >&2
  exit 1
fi
curl -sS -X POST "$BASE_URL/api/game/$GAME_ID/guess" \
  -H 'Content-Type: application/json' \
  -d "{\"guess\":\"$GUESS\"}"


