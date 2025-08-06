# DocuMind AI 🧠📄

> Intelligent Document Processing Platform combining Java microservices with AI/ML capabilities

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Python](https://img.shields.io/badge/Python-3.11+-blue.svg)](https://www.python.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🚀 Quick Start

### Prerequisites

- Java 21+
- Python 3.11+
- Docker & Docker Compose
- Maven 3.9+

### 1-Command Setup

```bash
git clone https://github.com/yourusername/documind-ai.git
cd documind-ai
./scripts/setup-dev.sh
```

### Start All Services

```bash
./scripts/start-services.sh
```

### Access the Platform

- **API Gateway**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **RabbitMQ Management**: http://localhost:15672
- **MinIO Console**: http://localhost:9001

## 🏗️ Architecture

### Microservices

- **API Gateway** (8080) - Request routing & authentication
- **User Service** (8081) - User management & auth
- **Document Service** (8082) - Document CRUD & metadata
- **Processing Service** (8083) - Document processing orchestration
- **Analytics Service** (8084) - Insights & reporting

### AI Services

- **OCR Service** (8085) - Text extraction from images/PDFs
- **NLP Service** (8086) - Entity extraction & analysis
- **Classification Service** (8087) - Document categorization

### Infrastructure

- **PostgreSQL** - Primary database
- **Redis** - Caching & sessions
- **RabbitMQ** - Message queuing
- **Elasticsearch** - Document search
- **Milvus** - Vector embeddings
- **MinIO** - File storage

## 🎯 Features

### Core Functionality

- ✅ Multi-format document upload (PDF, DOCX, images)
- ✅ Intelligent document classification
- ✅ OCR text extraction
- ✅ Named entity recognition
- ✅ Document summarization
- ✅ Semantic search
- ✅ Real-time processing status
- ✅ Analytics dashboard

### AI/ML Capabilities

- 🤖 Document type classification
- 📝 Text extraction & OCR
- 🏷️ Named entity recognition
- 📊 Document similarity matching
- 📋 Automatic summarization
- 🔍 Semantic search with embeddings

## 📚 API Documentation

### Document Upload

```bash
curl -X POST http://localhost:8080/api/documents \\
-F "file=@document.pdf" \\
-H "Authorization: Bearer <token>"
```

### Get Document Analysis

```bash
curl -X GET http://localhost:8080/api/documents/{id}/analysis \\
-H "Authorization: Bearer <token>"
```

### Search Documents

```bash
curl -X GET "http://localhost:8080/api/documents/search?q=contract&type=legal" \\
-H "Authorization: Bearer <token>"
```

## 🛠️ Development

### Build All Services

```bash
./scripts/build-all.sh
```

### Run Tests

```bash
mvn test
./scripts/test-api.sh
```

### Download ML Models

```bash
./scripts/download-models.sh
```

### Stop Services

```bash
./scripts/stop-services.sh
```

## 🚀 Deployment

### Railway

```bash
./scripts/deploy.sh railway
```

### Render

```bash
./scripts/deploy.sh render
```

### AWS

```bash
./scripts/deploy.sh aws
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (\`git checkout -b feature/amazing-feature\`)
3. Commit changes (\`git commit -m 'Add amazing feature'\`)
4. Push to branch (\`git push origin feature/amazing-feature\`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Hugging Face for pre-trained models
- OpenCV community for computer vision tools
- All open-source contributors
