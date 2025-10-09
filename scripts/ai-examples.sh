#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8083}

TERM=${1:-}
if [[ -z "$TERM" ]]; then
  echo "Usage: $0 <term>" >&2
  exit 1
fi

curl -sS -X POST "$BASE_URL/api/ai/example-sentences" \
  -H 'Content-Type: application/json' \
  -d "{\"term\":\"$TERM\"}"


