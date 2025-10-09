#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8081}
TERM_ID=${1:-}
MEANING=${2:-}
EXAMPLE=${3:-}
CREATED_BY=${4:-cli}

if [[ -z "$TERM_ID" || -z "$MEANING" ]]; then
  echo "Usage: $0 <termId> <meaning> [example] [createdBy]" >&2
  exit 1
fi

DATA="{\"meaning\":\"$MEANING\",\"example\":\"$EXAMPLE\",\"createdBy\":\"$CREATED_BY\"}"

curl -sS -X POST "$BASE_URL/api/terms/$TERM_ID/definitions" \
  -H 'Content-Type: application/json' \
  -d "$DATA"


