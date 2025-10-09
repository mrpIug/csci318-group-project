#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8081}
ID=${1:-}
DESC=${2:-}
TAGS=${3:-}

if [[ -z "$ID" ]]; then
  echo "Usage: $0 <id> [newDescription] [comma,separated,tags]" >&2
  exit 1
fi

DESC_FIELD=""
if [[ -n "$DESC" ]]; then
  DESC_FIELD=", \"description\": \"$DESC\""
fi

TAGS_FIELD=""
if [[ -n "$TAGS" ]]; then
  TAGS_JSON=$(printf '"%s"' $(echo "$TAGS" | sed 's/,/" "'/g))
  TAGS_FIELD=", \"tags\": [${TAGS_JSON}]"
fi

curl -sS -X PUT "$BASE_URL/api/terms/$ID" \
  -H 'Content-Type: application/json' \
  -d "{${DESC_FIELD#", "}${TAGS_FIELD}}"


