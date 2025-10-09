#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8083}
WORD=${1:-}
DESC=${2:-}
if [[ -z "$WORD" || -z "$DESC" ]]; then
  echo "Usage: $0 <word> <description>" >&2
  exit 1
fi
curl -sS -X POST "$BASE_URL/api/ai/suggest-tags" \
  -H 'Content-Type: application/json' \
  -d "{\"word\":\"$WORD\",\"description\":\"$DESC\"}"


