# Quick Verification Script for Windows PowerShell
# Shodh-a-Code Platform

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Shodh-a-Code Quick Verification" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

$allPassed = $true

# Check Java files
Write-Host "Checking Backend Structure..." -ForegroundColor Yellow
$javaFiles = (Get-ChildItem -Path backend\src\main\java -Recurse -Filter "*.java").Count
if ($javaFiles -ge 35) {
    Write-Host "✓ Backend: $javaFiles Java files found" -ForegroundColor Green
}
else {
    Write-Host "✗ Backend: Only $javaFiles Java files found (expected 35+)" -ForegroundColor Red
    $allPassed = $false
}

# Check TypeScript files
Write-Host "Checking Frontend Structure..." -ForegroundColor Yellow
$tsFiles = (Get-ChildItem -Path frontend -Recurse -Include "*.tsx","*.ts" -Exclude "node_modules").Count
if ($tsFiles -ge 10) {
    Write-Host "✓ Frontend: $tsFiles TypeScript files found" -ForegroundColor Green
}
else {
    Write-Host "✗ Frontend: Only $tsFiles TypeScript files found (expected 10+)" -ForegroundColor Red
    $allPassed = $false
}

# Check critical files
Write-Host ""
Write-Host "Checking Critical Files..." -ForegroundColor Yellow

$files = @(
    "backend\pom.xml",
    "backend\src\main\resources\application.yml",
    "backend\src\main\resources\data.sql",
    "frontend\package.json",
    "frontend\next.config.js",
    "docker-compose.yml",
    "docker\judge-environment\Dockerfile",
    "README.md",
    "setup.sh"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        Write-Host "✓ $file exists" -ForegroundColor Green
    }
    else {
        Write-Host "✗ $file MISSING" -ForegroundColor Red
        $allPassed = $false
    }
}

# Check for Docker
Write-Host ""
Write-Host "Checking Prerequisites..." -ForegroundColor Yellow
$dockerCmd = Get-Command docker -ErrorAction SilentlyContinue
if ($dockerCmd) {
    $dockerVersion = docker --version 2>$null
    Write-Host "✓ Docker: $dockerVersion" -ForegroundColor Green
}
else {
    Write-Host "✗ Docker is not installed or not in PATH" -ForegroundColor Red
    $allPassed = $false
}

$composeCmd = Get-Command docker-compose -ErrorAction SilentlyContinue
if ($composeCmd) {
    $composeVersion = docker-compose --version 2>$null
    Write-Host "✓ Docker Compose: $composeVersion" -ForegroundColor Green
}
else {
    Write-Host "✗ Docker Compose is not installed or not in PATH" -ForegroundColor Red
    $allPassed = $false
}

# Final verdict
Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
if ($allPassed) {
    Write-Host "✓ ALL CHECKS PASSED!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Your project is ready! Next steps:" -ForegroundColor Cyan
    Write-Host "1. Run setup script: ./setup.sh" -ForegroundColor White
    Write-Host "2. Wait 60 seconds for services to start" -ForegroundColor White
    Write-Host "3. Visit http://localhost:3000" -ForegroundColor White
}
else {
    Write-Host "✗ SOME CHECKS FAILED" -ForegroundColor Red
    Write-Host "Please review the errors above" -ForegroundColor Yellow
}
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""
