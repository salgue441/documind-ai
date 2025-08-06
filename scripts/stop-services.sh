#!/bin/bash
set -e

echo "🛑 Stopping DocuMind AI services..."

# Kill Java and Python services
if [ -f .service_pids ]; then
    echo "⚙️ Stopping application services..."
    PIDS=$(cat .service_pids)
    for PID in $PIDS; do
        if kill -0 $PID 2>/dev/null; then
            kill $PID
            echo "   Stopped service with PID: $PID"
        fi
    done
    rm -f .service_pids
fi

# Kill any remaining Java processes
echo "☕ Cleaning up Java processes..."
pkill -f "spring-boot:run" || true
pkill -f "mvn.*spring-boot" || true

# Kill any remaining Python processes
echo "🐍 Cleaning up Python processes..."
pkill -f "uvicorn.*main:app" || true

# Stop Docker containers
echo "🐳 Stopping infrastructure services..."
docker-compose down

echo "✅ All services stopped!"