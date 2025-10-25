# Quick Start Guide - Shodh-a-Code Platform

## Immediate Action Steps

This guide will help you get started on building the Shodh-a-Code platform **right now**.

---

## Day 1: Project Initialization

### Step 1: Create GitHub Repository (15 minutes)

```bash
# Create local directory
mkdir shodh-a-code
cd shodh-a-code

# Initialize git
git init

# Create basic structure
mkdir -p backend frontend docker/judge-environment

# Create README
touch README.md

# Create .gitignore
cat > .gitignore << 'EOF'
# IDE
.idea/
.vscode/
*.iml

# Backend
backend/target/
backend/.gradle/
backend/build/

# Frontend
frontend/node_modules/
frontend/.next/
frontend/out/
frontend/build/

# Environment
.env
.env.local

# OS
.DS_Store
Thumbs.db

# Docker
.dockerignore

# Temp
/tmp/
*.log
EOF

# Initial commit
git add .
git commit -m "Initial project structure"

# Create GitHub repo and push
# (Create repo on GitHub first, then:)
git remote add origin https://github.com/YOUR_USERNAME/shodh-a-code.git
git branch -M main
git push -u origin main
```

### Step 2: Initialize Spring Boot Backend (30 minutes)

**Option A: Using Spring Initializr (Recommended)**

1. Go to https://start.spring.io/
2. Configure:
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.2.x (latest stable)
   - **Group**: com.shodhai
   - **Artifact**: contest-platform
   - **Name**: Contest Platform
   - **Packaging**: Jar
   - **Java**: 17

3. Add Dependencies:
   - Spring Web
   - Spring Data JPA
   - PostgreSQL Driver
   - Lombok
   - Spring Boot DevTools
   - Validation

4. Generate and extract to `backend/` folder

**Option B: Manual Setup**

```bash
cd backend

# Create Maven project structure
mkdir -p src/main/java/com/shodhai/contest
mkdir -p src/main/resources
mkdir -p src/test/java/com/shodhai/contest

# Create pom.xml
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.shodhai</groupId>
    <artifactId>contest-platform</artifactId>
    <version>1.0.0</version>
    <name>Contest Platform</name>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF
```

### Step 3: Create Application Configuration (15 minutes)

```bash
cd backend/src/main/resources

# Create application.yml
cat > application.yml << 'EOF'
spring:
  application:
    name: contest-platform
  
  datasource:
    url: jdbc:postgresql://localhost:5432/contest_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

server:
  port: 8080

judge:
  docker:
    image: judge-env:latest
    memory-limit: 256m
    cpu-limit: 1
    timeout-seconds: 5
  temp-dir: /tmp/judge
EOF
```

### Step 4: Create Main Application Class (10 minutes)

```java
// File: backend/src/main/java/com/shodhai/contest/ContestPlatformApplication.java

package com.shodhai.contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContestPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContestPlatformApplication.class, args);
    }
}
```

### Step 5: Initialize Next.js Frontend (20 minutes)

```bash
cd ../../../frontend

# Create Next.js app with TypeScript and Tailwind
npx create-next-app@latest . --typescript --tailwind --app --no-src-dir --import-alias "@/*"

# Install additional dependencies
npm install axios monaco-editor @monaco-editor/react lucide-react

# Install dev dependencies
npm install -D @types/node @types/react
```

### Step 6: Configure Frontend Environment (10 minutes)

```bash
# Create .env.local
cat > .env.local << 'EOF'
NEXT_PUBLIC_API_URL=http://localhost:8080/api
EOF
```

### Step 7: Set Up PostgreSQL (20 minutes)

**Option A: Using Docker (Recommended)**

```bash
# Create docker-compose for development
cd ..
cat > docker-compose.dev.yml << 'EOF'
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: contest-postgres
    environment:
      POSTGRES_DB: contest_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
EOF

# Start PostgreSQL
docker-compose -f docker-compose.dev.yml up -d
```

**Option B: Local Installation**
- Install PostgreSQL 15+
- Create database: `createdb contest_db`
- Ensure it's running on port 5432

### Step 8: Test Backend Startup (10 minutes)

```bash
cd backend

# Run the application
./mvnw spring-boot:run

# Or if using Gradle:
./gradlew bootRun

# You should see:
# - Application started on port 8080
# - Database tables created
```

### Step 9: Test Frontend Startup (10 minutes)

```bash
cd ../frontend

# Start development server
npm run dev

# Visit http://localhost:3000
# You should see the default Next.js page
```

---

## Day 2: First Working Feature

### Create Your First Entity (Contest)

```java
// File: backend/src/main/java/com/shodhai/contest/model/Contest.java

package com.shodhai.contest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "contests")
@Data
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
```

### Create Repository

```java
// File: backend/src/main/java/com/shodhai/contest/repository/ContestRepository.java

package com.shodhai.contest.repository;

import com.shodhai.contest.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    Optional<Contest> findByIdAndIsActiveTrue(Long id);
}
```

### Create Service

```java
// File: backend/src/main/java/com/shodhai/contest/service/ContestService.java

package com.shodhai.contest.service;

import com.shodhai.contest.model.Contest;
import com.shodhai.contest.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    
    public Contest getContest(Long id) {
        return contestRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Contest not found"));
    }
}
```

### Create Controller

```java
// File: backend/src/main/java/com/shodhai/contest/controller/ContestController.java

package com.shodhai.contest.controller;

import com.shodhai.contest.model.Contest;
import com.shodhai.contest.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ContestController {
    private final ContestService contestService;
    
    @GetMapping("/{id}")
    public Contest getContest(@PathVariable Long id) {
        return contestService.getContest(id);
    }
}
```

### Test the API

```bash
# Start backend
cd backend
./mvnw spring-boot:run

# In another terminal, test with curl:
curl http://localhost:8080/api/contests/1

# Or use Postman/Insomnia
```

---

## Day 3-5: Build Core Features

### Priority Order

1. **Day 3: Complete Backend Data Models**
   - Create all entities (Contest, Problem, TestCase, Submission, User)
   - Create all repositories
   - Test with sample data

2. **Day 4: Basic API Endpoints**
   - Implement GET /api/contests/{id}
   - Implement POST /api/submissions
   - Implement GET /api/submissions/{id}
   - Test with Postman

3. **Day 5: Basic Frontend Pages**
   - Create join page
   - Create contest page layout
   - Connect to backend API
   - Display contest data

---

## Essential Code Snippets

### Create API Client (Frontend)

```typescript
// File: frontend/lib/api.ts

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getContest(contestId: string) {
  const response = await fetch(`${API_URL}/contests/${contestId}`);
  if (!response.ok) throw new Error('Failed to fetch contest');
  return response.json();
}

export async function submitCode(data: {
  userId: number;
  problemId: number;
  contestId: number;
  code: string;
  language: string;
}) {
  const response = await fetch(`${API_URL}/submissions`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  if (!response.ok) throw new Error('Failed to submit code');
  return response.json();
}

export async function getSubmissionStatus(submissionId: string) {
  const response = await fetch(`${API_URL}/submissions/${submissionId}`);
  if (!response.ok) throw new Error('Failed to fetch submission');
  return response.json();
}

export async function getLeaderboard(contestId: string) {
  const response = await fetch(`${API_URL}/contests/${contestId}/leaderboard`);
  if (!response.ok) throw new Error('Failed to fetch leaderboard');
  return response.json();
}
```

### Sample Data SQL

```sql
-- File: backend/src/main/resources/data.sql

-- Insert sample contest
INSERT INTO contests (id, title, description, start_time, end_time, is_active, created_at)
VALUES (1, 'Weekly Contest #1', 'Test your coding skills!', 
        NOW(), NOW() + INTERVAL '2 hours', true, NOW());

-- Insert sample problem
INSERT INTO problems (id, contest_id, title, description, difficulty, time_limit_ms, memory_limit_mb)
VALUES (1, 1, 'Two Sum', 
        'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.',
        'EASY', 1000, 256);

-- Insert sample test case
INSERT INTO test_cases (id, problem_id, input, expected_output, is_sample, points)
VALUES (1, 1, '4\n2 7 11 15\n9', '0 1', true, 50);
```

---

## Development Workflow

### Daily Routine

```bash
# Morning - Start services
docker-compose -f docker-compose.dev.yml up -d  # Start PostgreSQL
cd backend && ./mvnw spring-boot:run &           # Start backend
cd frontend && npm run dev &                      # Start frontend

# Development
# - Make changes
# - Test locally
# - Commit frequently

# Evening - Stop services
pkill -f spring-boot
pkill -f "next dev"
docker-compose -f docker-compose.dev.yml down
```

### Git Workflow

```bash
# Create feature branch
git checkout -b feature/contest-api

# Make changes and commit
git add .
git commit -m "feat: implement contest API endpoint"

# Push to GitHub
git push origin feature/contest-api

# Merge to main when complete
git checkout main
git merge feature/contest-api
git push origin main
```

---

## Useful Commands

### Backend Commands

```bash
# Run application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build JAR
./mvnw clean package

# Format code
./mvnw spotless:apply
```

### Frontend Commands

```bash
# Development server
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Lint
npm run lint
```

### Docker Commands

```bash
# Build judge image
cd docker/judge-environment
docker build -t judge-env:latest .

# List running containers
docker ps

# View logs
docker logs contest-postgres

# Clean up
docker system prune -a
```

---

## Troubleshooting

### Backend won't start
- Check PostgreSQL is running: `docker ps`
- Check database credentials in application.yml
- Check port 8080 is not in use: `lsof -i :8080`

### Frontend won't start
- Delete node_modules: `rm -rf node_modules && npm install`
- Check port 3000 is not in use: `lsof -i :3000`
- Check .env.local exists

### Database connection error
- Verify PostgreSQL is running
- Check connection string in application.yml
- Try connecting with psql: `psql -h localhost -U postgres -d contest_db`

---

## Next Steps

Once you have the basic setup working:

1. ✅ Add more entities (Problem, TestCase, Submission, User)
2. ✅ Implement remaining API endpoints
3. ✅ Build Docker judge environment
4. ✅ Implement code execution engine
5. ✅ Build frontend UI components
6. ✅ Implement polling mechanisms
7. ✅ Add leaderboard calculation
8. ✅ Write tests
9. ✅ Create comprehensive README
10. ✅ Deploy and submit

---

## Resources

### Documentation
- Spring Boot: https://spring.io/projects/spring-boot
- Next.js: https://nextjs.org/docs
- Tailwind CSS: https://tailwindcss.com/docs
- PostgreSQL: https://www.postgresql.org/docs/

### Tools
- Postman: https://www.postman.com/
- Docker Desktop: https://www.docker.com/products/docker-desktop
- DBeaver: https://dbeaver.io/

### Tutorials
- Spring Boot REST API: https://spring.io/guides/tutorials/rest/
- Next.js Tutorial: https://nextjs.org/learn
- Docker Tutorial: https://docs.docker.com/get-started/

---

**Good luck with your project! 🚀**

Remember:
- Start small and iterate
- Test frequently
- Commit often
- Document as you go
- Ask for help when stuck

