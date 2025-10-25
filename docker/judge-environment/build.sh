#!/bin/bash

# Build script for judge environment Docker image

echo "Building judge environment Docker image..."

docker build -t judge-env:latest .

if [ $? -eq 0 ]; then
    echo "✓ Successfully built judge-env:latest"
    echo ""
    echo "Verifying image..."
    docker images | grep judge-env
    echo ""
    echo "Testing Java..."
    docker run --rm judge-env:latest java -version
    echo ""
    echo "Testing Python..."
    docker run --rm judge-env:latest python3 --version
    echo ""
    echo "Testing G++..."
    docker run --rm judge-env:latest g++ --version
    echo ""
    echo "Testing Node.js..."
    docker run --rm judge-env:latest node --version
    echo ""
    echo "✓ Judge environment is ready!"
else
    echo "✗ Build failed"
    exit 1
fi

