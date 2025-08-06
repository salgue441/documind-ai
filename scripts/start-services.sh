#!/bin/bash
set -e

echo "🚀 Starting all DocuMind AI services..."

# Start infrastructure
echo "🐳 Starting infrastructure services..."
docker-compose up -d

# Wait for infrastructure to be ready
echo "⏳ Waiting for infrastructure..."
sleep 20

# Build and start Java services
echo "☕ Building and starting Java services..."
mvn clean install -DskipTests

# Start Java services in background
echo "🌐 Starting API Gateway..."
cd services/api-gateway && mvn spring-boot:run -Dspring-boot.run.profiles=dev &
GATEWAY_PID=$!

echo "👤 Starting User Service..."
cd ../user-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8081 &
USER_PID=$!

echo "📄 Starting Document Service..."
cd ../document-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8082 &
DOC_PID=$!

echo "⚙️ Starting Processing Service..."
cd ../processing-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8083 &
PROC_PID=$!

echo "📊 Starting Analytics Service..."
cd ../analytics-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8084 &
ANALYTICS_PID=$!

# Go back to root
cd ../../

# Activate Python environment and start AI services
echo "🤖 Starting AI services..."
source venv/bin/activate

echo "🔍 Starting OCR Service..."
cd services/ai-services/ocr-service && uvicorn main:app --host 0.0.0.0 --port 8085 &
OCR_PID=$!

echo "🧠 Starting NLP Service..."
cd ../nlp-service && uvicorn main:app --host 0.0.0.0 --port 8086 &
NLP_PID=$!

echo "🏷️ Starting Classification Service..."
cd ../classification-service && uvicorn main:app --host 0.0.0.0 --port 8087 &
CLASS_PID=$!

# Go back to root
cd ../../../

# Store PIDs for cleanup
echo "$GATEWAY_PID $USER_PID $DOC_PID $PROC_PID $ANALYTICS_PID $OCR_PID $NLP_PID $CLASS_PID" > .service_pids

echo "✅ All services started!"
echo ""
echo "🎯 Service Endpoints:"
echo "   API Gateway: http://localhost:8080"
echo "   User Service: http://localhost:8081"
echo "   Document Service: http://localhost:8082"
echo "   Processing Service: http://localhost:8083"
echo "   Analytics Service: http://localhost:8084"
echo "   OCR Service: http://localhost:8085"
echo "   NLP Service: http://localhost:8086"
echo "   Classification Service: http://localhost:8087"
echo ""
echo "📖 API Documentation:"
echo "   http://localhost:8080/swagger-ui.html"
echo ""
echo "🛑 To stop all services: ./scripts/stop-services.sh"