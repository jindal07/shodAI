#!/bin/bash

# Stop Shodh-a-Code Platform

echo "Stopping Shodh-a-Code Platform..."

docker-compose down

echo ""
echo "✓ All services stopped"
echo ""
echo "To remove all data (including database):"
echo "  docker-compose down -v"
echo ""

