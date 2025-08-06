#!/bin/bash
set -e

echo "🔨 Building all DocuMind AI services..."

# Build Java services
echo "☕ Building Java services..."
mvn clean install -DskipTests

# Build Docker images for Java services
echo "🐳 Building Java service Docker images..."
docker build -f services/api-gateway/Dockerfile -t documind/api-gateway:latest .
docker build -f services/user-service/Dockerfile -t documind/user-service:latest .
docker build -f services/document-service/Dockerfile -t documind/document-service:latest .
docker build -f services/processing-service/Dockerfile -t documind/processing-service:latest .
docker build -f services/analytics-service/Dockerfile -t documind/analytics-service:latest .

# Build Docker images for AI services
echo "🤖 Building AI service Docker images..."
docker build -f services/ai-services/ocr-service/Dockerfile -t documind/ocr-service:latest services/ai-services/ocr-service/
docker build -f services/ai-services/nlp-service/Dockerfile -t documind/nlp-service:latest services/ai-services/nlp-service/
docker build -f services/ai-services/classification-service/Dockerfile -t documind/classification-service:latest services/ai-services/classification-service/

echo "✅ All services built successfully!"

# Show built images
echo "📦 Built Docker images:"
docker images | grep documind
