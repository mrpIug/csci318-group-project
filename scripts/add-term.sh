#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8081}

WORD=${1:-}
DESC=${2:-}
CREATED_BY=${3:-cli}
TAGS=${4:-}

if [[ -z "$WORD" || -z "$DESC" ]]; then
  echo "Usage: BASE_URL=... $0 <word> <description> [createdBy] [comma,separated,tags]" >&2
  exit 1
fi

if [[ -n "$TAGS" ]]; then
  TAGS_JSON=$(printf '"%s"' $(echo "$TAGS" | sed 's/,/" "'/g))
  TAGS_FIELD=", \"tags\": [${TAGS_JSON}]"
else
  TAGS_FIELD=""
fi

curl -sS -X POST "$BASE_URL/api/terms" \
  -H 'Content-Type: application/json' \
  -d "{\"word\": \"$WORD\", \"description\": \"$DESC\", \"createdBy\": \"$CREATED_BY\"$TAGS_FIELD}"


