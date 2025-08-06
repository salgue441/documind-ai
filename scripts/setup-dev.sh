#!/bin/bash
set -e

echo "🚀 Setting up DocuMind AI development environment..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Create necessary directories
echo "📁 Creating project directories..."
mkdir -p shared/ml-models/{classification,embeddings,ner}
mkdir -p services/ai-services/{ocr-service,nlp-service,classification-service}/app
mkdir -p docs tests/{integration,performance,api}
mkdir -p infrastructure/{k8s,terraform,helm}

# Make scripts executable
chmod +x scripts/*.sh

# Create Python virtual environment for AI services
echo "🐍 Setting up Python environment for AI services..."
if [ ! -d "venv" ]; then
    python3 -m venv venv
fi
source venv/bin/activate

# Install Python dependencies for AI services
pip install --upgrade pip
pip install fastapi uvicorn python-multipart
pip install opencv-python pillow pytesseract
pip install spacy transformers torch
pip install sentence-transformers
pip install pandas numpy scikit-learn
pip install pymilvus elasticsearch redis rabbitmq
pip install python-dotenv pydantic
pip install pytest pytest-asyncio httpx

# Download spaCy models
echo "📦 Downloading spaCy models..."
python -m spacy download en_core_web_sm
python -m spacy download en_core_web_lg

# Start infrastructure services
echo "🐳 Starting infrastructure services..."
docker-compose up -d postgres redis rabbitmq elasticsearch milvus-standalone minio

# Wait for services to be ready
echo "⏳ Waiting for services to start..."
sleep 30

# Check service health
echo "🔍 Checking service health..."
docker-compose ps

# Create initial database schemas
echo "🗄️ Setting up database schemas..."
docker-compose exec -T postgres psql -U documind -d documind -c "
CREATE SCHEMA IF NOT EXISTS users;
CREATE SCHEMA IF NOT EXISTS documents;
CREATE SCHEMA IF NOT EXISTS processing;
CREATE SCHEMA IF NOT EXISTS analytics;
"

# Create MinIO buckets
echo "🪣 Creating MinIO buckets..."
docker-compose exec -T minio mc alias set local http://localhost:9000 documind documind123
docker-compose exec -T minio mc mb local/documents || true
docker-compose exec -T minio mc mb local/processed || true
docker-compose exec -T minio mc mb local/models || true

# Set up RabbitMQ exchanges and queues
echo "🐰 Setting up RabbitMQ..."
sleep 10
curl -u documind:documind123 -X PUT http://localhost:15672/api/exchanges/documind/document.processing -H "content-type:application/json" -d '{"type":"topic","durable":true}' || true
curl -u documind:documind123 -X PUT http://localhost:15672/api/queues/documind/ocr.queue -H "content-type:application/json" -d '{"durable":true}' || true
curl -u documind:documind123 -X PUT http://localhost:15672/api/queues/documind/nlp.queue -H "content-type:application/json" -d '{"durable":true}' || true

echo "✅ Development environment setup complete!"
echo ""
echo "🎯 Service URLs:"
echo "   PostgreSQL: localhost:5432 (documind/documind123)"
echo "   Redis: localhost:6379 (password: documind123)"
echo "   RabbitMQ Management: http://localhost:15672 (documind/documind123)"
echo "   Elasticsearch: http://localhost:9200"
echo "   Milvus: localhost:19530"
echo "   MinIO: http://localhost:9001 (documind/documind123)"
echo ""
echo "📚 Next Steps:"
echo "   1. Run 'mvn clean install' to build Java services"
echo "   2. Run './scripts/download-models.sh' to download ML models"
echo "   3. Start coding! 🚀"
