#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8081}
ID=${1:-}
if [[ -z "$ID" ]]; then
  echo "Usage: $0 <id>" >&2
  exit 1
fi
curl -sS -X DELETE "$BASE_URL/api/terms/$ID"


