#!/bin/bash

# Shodh-a-Code Platform Setup Script

set -e

echo "========================================="
echo "Shodh-a-Code Platform Setup"
echo "========================================="
echo ""

# Check prerequisites
echo "Checking prerequisites..."

if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "✓ Docker is installed"
echo "✓ Docker Compose is installed"
echo ""

# Build judge environment
echo "Building judge environment Docker image..."
cd docker/judge-environment
chmod +x build.sh
./build.sh
cd ../..
echo ""

# Create frontend .env.local if it doesn't exist
if [ ! -f frontend/.env.local ]; then
    echo "Creating frontend/.env.local..."
    echo "NEXT_PUBLIC_API_URL=http://localhost:8080/api" > frontend/.env.local
    echo "✓ Created frontend/.env.local"
fi
echo ""

# Build and start services
echo "Building and starting services..."
docker-compose up --build -d

echo ""
echo "========================================="
echo "Setup Complete!"
echo "========================================="
echo ""
echo "Services starting..."
echo "- Backend: http://localhost:8080"
echo "- Frontend: http://localhost:3000"
echo "- Database: localhost:5432"
echo ""
echo "Please wait 30-60 seconds for all services to initialize."
echo ""
echo "To check logs:"
echo "  docker-compose logs -f"
echo ""
echo "To stop services:"
echo "  docker-compose down"
echo ""
echo "Visit http://localhost:3000 to get started!"
echo ""

