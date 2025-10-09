#!/usr/bin/env bash
set -euo pipefail

BASE_URL=${BASE_URL:-http://localhost:8081}
curl -sS "$BASE_URL/api/terms/random"


