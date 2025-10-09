#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8081}

usage() { echo "Usage: $0 [--word w | --tag t | --id n]" >&2; exit 1; }

if [[ $# -lt 2 ]]; then usage; fi

case "$1" in
  --word) curl -sS "$BASE_URL/api/terms/search?word=$2" ;;
  --tag)  curl -sS "$BASE_URL/api/terms/search?tag=$2" ;;
  --id)   curl -sS "$BASE_URL/api/terms/$2" ;;
  *) usage ;;
esac


