# DocuMind AI - Development Makefile

.PHONY: help setup build test clean start stop deploy lint format security

# Default target
help: ## Show this help message
	@echo "DocuMind AI - Development Commands"
	@echo "=================================="
	@grep -E '^[a-zA-Z_-]+:.*?## .*$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $1, $2}'

# Setup and Installation
setup: ## Set up development environment
	@echo "🚀 Setting up DocuMind AI development environment..."
	./scripts/setup-dev.sh

install: ## Install dependencies
	@echo "📦 Installing dependencies..."
	mvn clean install -DskipTests
	pip install -r requirements.txt

models: ## Download ML models
	@echo "🤖 Downloading ML models..."
	./scripts/download-models.sh

# Build
build: ## Build all services
	@echo "🔨 Building all services..."
	./scripts/build-all.sh

build-java: ## Build only Java services
	@echo "☕ Building Java services..."
	mvn clean package -DskipTests

build-docker: ## Build Docker images
	@echo "🐳 Building Docker images..."
	docker-compose build

# Testing
test: ## Run all tests
	@echo "🧪 Running tests..."
	mvn test
	./scripts/test-api.sh

test-java: ## Run Java tests only
	@echo "☕ Running Java tests..."
	mvn test

test-python: ## Run Python tests only
	@echo "🐍 Running Python tests..."
	find services/ai-services -name "test_*.py" -exec pytest {} \;

test-integration: ## Run integration tests
	@echo "🔗 Running integration tests..."
	./scripts/test-api.sh

# Development
start: ## Start all services
	@echo "🚀 Starting all services..."
	./scripts/start-services.sh

stop: ## Stop all services
	@echo "🛑 Stopping all services..."
	./scripts/stop-services.sh

restart: stop start ## Restart all services

logs: ## View logs from all services
	@echo "📋 Viewing logs..."
	docker-compose logs -f

status: ## Check service status
	@echo "📊 Service status..."
	docker-compose ps
	curl -s http://localhost:8080/actuator/health | jq . || echo "API Gateway not responding"

# Code Quality
lint: ## Run linting
	@echo "🔍 Running linters..."
	mvn spotless:check || echo "Java linting issues found"
	flake8 services/ai-services/ || echo "Python linting issues found"

format: ## Format code
	@echo "✨ Formatting code..."
	mvn spotless:apply
	black services/ai-services/
	isort services/ai-services/

security: ## Run security scans
	@echo "🔒 Running security scans..."
	mvn org.owasp:dependency-check-maven:check
	bandit -r services/ai-services/ -f json -o security-report.json || echo "Security issues found"

pre-commit: ## Run pre-commit hooks
	@echo "🪝 Running pre-commit hooks..."
	pre-commit run --all-files

# Database
db-migrate: ## Run database migrations
	@echo "🗄️ Running database migrations..."
	docker-compose exec postgres psql -U documind -d documind -f /docker-entrypoint-initdb.d/init.sql

db-reset: ## Reset database
	@echo "🔄 Resetting database..."
	docker-compose down -v
	docker-compose up -d postgres
	sleep 10
	$(MAKE) db-migrate

# Deployment
deploy-dev: ## Deploy to development environment
	@echo "🚢 Deploying to development..."
	./scripts/deploy.sh dev

deploy-staging: ## Deploy to staging environment
	@echo "🚢 Deploying to staging..."
	./scripts/deploy.sh staging

deploy-prod: ## Deploy to production environment
	@echo "🚢 Deploying to production..."
	./scripts/deploy.sh prod

# Documentation
docs: ## Generate documentation
	@echo "📚 Generating documentation..."
	mvn javadoc:aggregate
	cd docs && mkdocs build

docs-serve: ## Serve documentation locally
	@echo "📖 Serving documentation..."
	cd docs && mkdocs serve

# Cleanup
clean: ## Clean build artifacts
	@echo "🧹 Cleaning up..."
	mvn clean
	docker system prune -f
	find . -name "*.pyc" -delete
	find . -name "__pycache__" -delete

clean-docker: ## Clean Docker resources
	@echo "🐳 Cleaning Docker resources..."
	docker-compose down -v
	docker system prune -a -f

# Utilities
shell-postgres: ## Open PostgreSQL shell
	docker-compose exec postgres psql -U documind -d documind

shell-redis: ## Open Redis shell
	docker-compose exec redis redis-cli -a documind123

shell-rabbitmq: ## Open RabbitMQ management
	@echo "🐰 Opening RabbitMQ Management: http://localhost:15672"
	@echo "Username: documind, Password: documind123"

backup: ## Backup data
	@echo "💾 Creating backup..."
	docker-compose exec postgres pg_dump -U documind documind > backup/db-$(shell date +%Y%m%d-%H%M%S).sql

# Development helpers
watch: ## Watch for changes and restart services
	@echo "👀 Watching for changes..."
	find services/ -name "*.java" -o -name "*.py" | entr -r $(MAKE) restart

tail-logs: ## Tail application logs
	@echo "📜 Tailing logs..."
	tail -f logs/*.log

check-ports: ## Check if required ports are available
	@echo "🔌 Checking ports..."
	@for port in 8080 8081 8082 8083 8084 8085 8086 8087 5432 6379 5672 9200 19530 9000; do \
		if lsof -i :$port > /dev/null 2>&1; then \
			echo "❌ Port $port is in use"; \
		else \
			echo "✅ Port $port is available"; \
		fi; \
	done

# Release
release-patch: ## Create patch release
	@echo "📦 Creating patch release..."
	mvn release:prepare release:perform -DreleaseVersion=PATCH

release-minor: ## Create minor release  
	@echo "📦 Creating minor release..."
	mvn release:prepare release:perform -DreleaseVersion=MINOR

release-major: ## Create major release
	@echo "📦 Creating major release..."
	mvn release:prepare release:perform -DreleaseVersion=MAJOR