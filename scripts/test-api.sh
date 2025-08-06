#!/bin/bash
set -e

echo "🧪 Testing DocuMind AI API endpoints..."

# Base URL
BASE_URL="http://localhost:8080/api"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local expected_status=$4

    echo -e "${YELLOW}Testing: $method $endpoint${NC}"

    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "%{http_code}" "$BASE_URL$endpoint")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$BASE_URL$endpoint")
    elif [ "$method" = "PUT" ]; then
        response=$(curl -s -w "%{http_code}" -X PUT -H "Content-Type: application/json" -d "$data" "$BASE_URL$endpoint")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "%{http_code}" -X DELETE "$BASE_URL$endpoint")
    fi

    status_code="${response: -3}"
    body="${response%???}"

    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN}✅ SUCCESS: $status_code${NC}"
    else
        echo -e "${RED}❌ FAILED: Expected $expected_status, got $status_code${NC}"
        echo "Response: $body"
    fi
    echo ""
}

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 10

# Health checks
echo "🏥 Health Checks:"
test_endpoint "GET" "/health" "" "200"
test_endpoint "GET" "/health/ready" "" "200"

# User Service Tests
echo "👤 User Service Tests:"
test_endpoint "POST" "/users/register" '{"username":"testuser","email":"test@example.com","password":"password123"}' "201"
test_endpoint "POST" "/users/login" '{"email":"test@example.com","password":"password123"}' "200"
test_endpoint "GET" "/users/profile" "" "200"

# Document Service Tests
echo "📄 Document Service Tests:"
test_endpoint "GET" "/documents" "" "200"
test_endpoint "POST" "/documents" '{"name":"test-document.pdf","type":"PDF","size":1024}' "201"

# Processing Service Tests
echo "⚙️ Processing Service Tests:"
test_endpoint "GET" "/processing/status" "" "200"
test_endpoint "GET" "/processing/queue" "" "200"

# Analytics Service Tests
echo "📊 Analytics Service Tests:"
test_endpoint "GET" "/analytics/dashboard" "" "200"
test_endpoint "GET" "/analytics/stats" "" "200"

# AI Service Tests
echo "🤖 AI Service Tests:"
echo -e "${YELLOW}Testing OCR Service (port 8085)${NC}"
curl -s -w "%{http_code}\n" "http://localhost:8085/health" | tail -1

echo -e "${YELLOW}Testing NLP Service (port 8086)${NC}"
curl -s -w "%{http_code}\n" "http://localhost:8086/health" | tail -1

echo -e "${YELLOW}Testing Classification Service (port 8087)${NC}"
curl -s -w "%{http_code}\n" "http://localhost:8087/health" | tail -1

echo "✅ API testing complete!"
