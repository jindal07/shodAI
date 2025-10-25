# Shodh-a-Code Contest Platform

A full-stack **live coding contest platform** with real-time code execution, submission tracking, and leaderboards. Built with Spring Boot, Next.js, and Docker.

![Platform Demo](https://img.shields.io/badge/Status-Production%20Ready-success)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![Next.js](https://img.shields.io/badge/Next.js-14-black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

## 📋 Table of Contents

- [Features](#-features)
- [Demo](#-demo)
- [Tech Stack](#-tech-stack)
- [Setup Instructions](#-setup-instructions)
- [API Documentation](#-api-documentation)
- [Architecture & Design](#-architecture--design)
- [Key Technical Decisions](#-key-technical-decisions)
- [Challenges & Solutions](#-challenges--solutions)
- [Future Improvements](#-future-improvements)

---

## ✨ Features

### Core Functionality
- **Contest Management**: Join contests using contest ID and username
- **Problem Solving**: View problem descriptions, constraints, and sample test cases
- **Code Submission**: Write and submit code in multiple languages (Java, Python, C++, JavaScript)
- **Real-time Judging**: Docker-based code execution with instant feedback
- **Live Leaderboard**: Real-time rankings based on scores and submission times
- **Submission Tracking**: Poll for submission status with detailed verdict information

### Technical Highlights
- **Secure Code Execution**: Sandboxed Docker containers with resource limits
- **Asynchronous Processing**: Queue-based submission processing with worker threads
- **Real-time Updates**: Polling mechanism for submission status and leaderboard
- **Responsive UI**: Modern, mobile-friendly interface with Tailwind CSS
- **Code Persistence**: Local storage for code across sessions
- **Multi-language Support**: Java, Python, C++, and JavaScript

---

## 🎬 Demo

### Quick Start (Try it now!)
1. Run the setup: `chmod +x setup.sh && ./setup.sh`
2. Visit: http://localhost:3000
3. Join Contest with ID: **1** and any username
4. Start solving problems!

### Default Contest
The platform comes pre-loaded with **Weekly Contest #1** featuring:
- **Problem 1**: Two Sum (Easy)
- **Problem 2**: Valid Parentheses (Easy)
- **Problem 3**: Fibonacci Number (Medium)

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Maven
- **API**: RESTful with JSON

### Frontend
- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS 3.x
- **Code Editor**: Monaco Editor (VS Code editor)
- **HTTP Client**: Axios
- **State Management**: React Hooks + Context

### DevOps & Execution
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **Judge Environment**: Custom Docker image (Ubuntu + JDK + Python + g++ + Node.js)
- **Code Execution**: Isolated Docker containers with resource limits

---

## 🚀 Setup Instructions

### Prerequisites
- **Docker** (version 20.10+)
- **Docker Compose** (version 2.0+)
- **Git**

### Option 1: Quick Setup (Recommended)

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/shodh-a-code.git
cd shodh-a-code

# Run setup script
chmod +x setup.sh
./setup.sh

# Wait 30-60 seconds for services to initialize
# Visit http://localhost:3000
```

### Option 2: Manual Setup

#### Step 1: Build Judge Environment
```bash
cd docker/judge-environment
chmod +x build.sh
./build.sh
cd ../..
```

#### Step 2: Create Frontend Environment File
```bash
echo "NEXT_PUBLIC_API_URL=http://localhost:8080/api" > frontend/.env.local
```

#### Step 3: Start Services
```bash
docker-compose up --build -d
```

#### Step 4: Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Database**: localhost:5432 (postgres/postgres)

### Stopping the Application

```bash
# Stop services
chmod +x stop.sh
./stop.sh

# Or manually
docker-compose down

# To remove all data (including database)
docker-compose down -v
```

### Troubleshooting

**Services won't start?**
- Ensure ports 3000, 8080, and 5432 are not in use
- Check Docker daemon is running: `docker ps`

**Backend can't execute code?**
- Verify judge-env image exists: `docker images | grep judge-env`
- Rebuild: `cd docker/judge-environment && ./build.sh`

**Frontend can't connect to backend?**
- Check backend is running: `docker-compose logs backend`
- Verify .env.local exists in frontend/

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### 1. Get Contest Details
```http
GET /contests/{contestId}

Response 200 OK:
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Weekly Contest #1",
    "description": "Test your coding skills!",
    "startTime": "2025-10-25T10:00:00",
    "endTime": "2025-10-25T12:00:00",
    "isActive": true,
    "problems": [
      {
        "id": 1,
        "title": "Two Sum",
        "difficulty": "EASY",
        "timeLimitMs": 1000,
        "memoryLimitMb": 256
      }
    ]
  }
}
```

#### 2. Join Contest
```http
POST /contests/{contestId}/join

Request Body:
{
  "username": "john_doe",
  "email": "john@example.com"  // optional
}

Response 200 OK:
{
  "success": true,
  "message": "Successfully joined contest",
  "data": {
    "userId": 123,
    "username": "john_doe",
    "message": "Successfully joined contest"
  }
}
```

#### 3. Submit Code
```http
POST /submissions

Request Body:
{
  "userId": 123,
  "problemId": 1,
  "contestId": 1,
  "code": "public class Main { ... }",
  "language": "JAVA"
}

Response 202 Accepted:
{
  "success": true,
  "message": "Submission received and queued for judging",
  "data": {
    "id": 456,
    "userId": 123,
    "problemId": 1,
    "contestId": 1,
    "language": "JAVA",
    "status": "PENDING",
    "score": 0,
    "testCasesPassed": 0,
    "totalTestCases": 0,
    "submittedAt": "2025-10-25T10:15:00"
  }
}
```

#### 4. Get Submission Status
```http
GET /submissions/{submissionId}

Response 200 OK:
{
  "success": true,
  "data": {
    "id": 456,
    "status": "ACCEPTED",
    "result": "AC",
    "score": 100,
    "executionTimeMs": 234,
    "memoryUsedMb": 45,
    "testCasesPassed": 5,
    "totalTestCases": 5,
    "submittedAt": "2025-10-25T10:15:00",
    "completedAt": "2025-10-25T10:15:05"
  }
}
```

#### 5. Get Leaderboard
```http
GET /contests/{contestId}/leaderboard

Response 200 OK:
{
  "success": true,
  "data": {
    "contestId": 1,
    "lastUpdated": "2025-10-25T10:30:00",
    "entries": [
      {
        "rank": 1,
        "userId": 123,
        "username": "alice",
        "totalScore": 300,
        "problemsSolved": 3,
        "lastSubmissionTime": "2025-10-25T10:30:00"
      }
    ]
  }
}
```

### Error Responses

```http
404 Not Found:
{
  "success": false,
  "message": "Contest not found with id: '999'",
  "timestamp": "2025-10-25T10:30:00"
}

400 Bad Request:
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "username": "Username must be at least 3 characters"
  },
  "timestamp": "2025-10-25T10:30:00"
}
```

---

## 🏗️ Architecture & Design

### System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENT LAYER                          │
│  ┌───────────────────────────────────────────────────┐  │
│  │    Next.js Frontend (React + Tailwind CSS)       │  │
│  │  - Join Page  - Contest Page  - Code Editor      │  │
│  │  - Problem View  - Leaderboard  - Status Tracker │  │
│  └─────────────────────┬─────────────────────────────┘  │
└────────────────────────┼─────────────────────────────────┘
                         │ HTTP/REST (Polling)
┌────────────────────────▼─────────────────────────────────┐
│                  APPLICATION LAYER                       │
│  ┌───────────────────────────────────────────────────┐  │
│  │         Spring Boot Backend (Java 17)             │  │
│  │  ┌──────────┐  ┌───────────┐  ┌──────────────┐   │  │
│  │  │Controllers│  │  Services │  │ Judge Engine │   │  │
│  │  └─────┬────┘  └─────┬─────┘  └──────┬───────┘   │  │
│  │        │             │                │           │  │
│  │  ┌─────▼─────────────▼────────────────▼────────┐  │  │
│  │  │        Repositories (Spring Data JPA)       │  │  │
│  │  └──────────────────┬──────────────────────────┘  │  │
│  └────────────────────┬┼──────────────────────────────┘  │
└───────────────────────┼┼──────────────────────────────────┘
                        ││ Docker Commands
┌───────────────────────▼▼──────────────────────────────────┐
│     DATA & EXECUTION LAYER                               │
│  ┌────────────────┐        ┌───────────────────────┐    │
│  │   PostgreSQL   │        │   Docker Containers   │    │
│  │    Database    │        │  (Judge Environment)  │    │
│  │  - contests    │        │  - Isolated execution │    │
│  │  - problems    │        │  - Resource limits    │    │
│  │  - submissions │        │  - Multi-language     │    │
│  │  - users       │        │  - Auto cleanup       │    │
│  └────────────────┘        └───────────────────────┘    │
└──────────────────────────────────────────────────────────┘
```

### Backend Architecture

**Layer Structure**:
1. **Controller Layer**: REST endpoints, request validation
2. **Service Layer**: Business logic, orchestration
3. **Repository Layer**: Data access (Spring Data JPA)
4. **Judge Engine**: Code execution, validation, queue management

**Key Components**:
- `JudgeService`: Orchestrates submission processing
- `DockerExecutor`: Manages Docker container execution
- `SubmissionQueue`: Async queue with worker threads
- `TestCaseValidator`: Output validation logic
- `LanguageStrategy`: Language-specific configurations

### Frontend Architecture

**Component Structure**:
```
app/
├── page.tsx                 # Landing page
├── join/page.tsx            # Contest join page
└── contest/[id]/page.tsx    # Main contest page

components/
├── ProblemList.tsx          # Problem selector
├── ProblemDetail.tsx        # Problem description
├── CodeEditor.tsx           # Monaco editor integration
├── SubmissionStatus.tsx     # Real-time status display
└── Leaderboard.tsx          # Live rankings
```

**State Management**:
- React Hooks for local state
- LocalStorage for code persistence and user data
- Polling for real-time updates (submissions & leaderboard)

### Database Schema

```sql
-- Core Tables
contests (id, title, description, start_time, end_time, is_active)
problems (id, contest_id, title, description, difficulty, time_limit_ms, memory_limit_mb)
test_cases (id, problem_id, input, expected_output, is_sample, points)
users (id, username, email, created_at)
submissions (id, user_id, problem_id, contest_id, code, language, status, result, score, submitted_at)
contest_participants (id, contest_id, user_id, joined_at, total_score)

-- Key Relationships
- Contest → Problems (1:N)
- Problem → TestCases (1:N)
- User → Submissions (1:N)
- Contest ↔ Users (M:N through contest_participants)
```

### Code Execution Flow

```
1. User submits code
   ↓
2. Backend creates Submission (status: PENDING)
   ↓
3. Add to SubmissionQueue → Return submissionId
   ↓
4. Worker thread picks up submission
   ↓
5. Update status to RUNNING
   ↓
6. For each test case:
   - Write code to temp file
   - Execute in Docker container
   - Capture output
   - Validate against expected output
   - Collect verdict (AC/WA/TLE/MLE/RE/CE)
   ↓
7. Aggregate results (calculate score)
   ↓
8. Update submission with final status
   ↓
9. Clean up temp files and containers
   ↓
10. Frontend polls status (every 2s)
    ↓
11. Display verdict to user
```

---

## 💡 Key Technical Decisions

### 1. Docker-Based Code Execution

**Decision**: Execute user code in isolated Docker containers

**Justification**:
- **Security**: Complete isolation from host system
- **Resource Control**: Enforce memory/CPU/time limits
- **Multi-language**: Easy to support multiple languages
- **Reproducibility**: Consistent execution environment

**Implementation**:
- Custom Docker image with Java, Python, C++, Node.js
- Non-root user for additional security
- Network disabled (`--network=none`)
- ProcessBuilder from Java to execute `docker run`

**Trade-offs**:
- **Pro**: Maximum security and flexibility
- **Con**: Slight overhead (~100-200ms per execution)
- **Alternative Considered**: Direct process execution (rejected due to security concerns)

### 2. Polling vs WebSocket

**Decision**: Use HTTP polling for real-time updates

**Justification**:
- **Simplicity**: Easier to implement and debug
- **Stateless**: No connection management
- **Sufficient**: 2-second polling acceptable for contest scenario
- **Reliability**: Works behind any proxy/firewall

**Implementation**:
- Frontend polls every 2 seconds for submission status
- Stops polling when submission completes
- Leaderboard auto-refreshes every 20 seconds

**Trade-offs**:
- **Pro**: Simple, reliable, scalable
- **Con**: Slightly higher latency than WebSocket
- **Future**: Can upgrade to WebSocket for instant updates

### 3. Asynchronous Submission Processing

**Decision**: Queue-based async processing with worker threads

**Justification**:
- **Non-blocking**: API returns immediately with submissionId
- **Scalability**: Multiple workers process submissions in parallel
- **Resilience**: Queue survives temporary failures

**Implementation**:
- `BlockingQueue` with configurable capacity (default: 100)
- Fixed thread pool (default: 4 workers)
- Each worker processes submissions independently

**Trade-offs**:
- **Pro**: Fast API response, handles load spikes
- **Con**: Requires polling for results
- **Alternative Considered**: Synchronous processing (rejected - would timeout for slow submissions)

### 4. Next.js with App Router

**Decision**: Use Next.js 14 with App Router (Server Components)

**Justification**:
- **Performance**: Automatic code splitting and optimization
- **SEO**: Server-side rendering support (future)
- **Developer Experience**: File-based routing, TypeScript integration
- **Modern**: Latest React features

**Trade-offs**:
- **Pro**: Best-in-class frontend framework
- **Con**: Learning curve for App Router
- **Alternative Considered**: Create React App (rejected - less features)

### 5. PostgreSQL for Database

**Decision**: Use PostgreSQL as primary database

**Justification**:
- **Reliability**: ACID compliance, proven at scale
- **Performance**: Excellent query performance with proper indexes
- **Features**: JSON support, full-text search (future)
- **Spring Boot**: Excellent integration with JPA

**Trade-offs**:
- **Pro**: Production-ready, feature-rich
- **Con**: Overkill for simple apps (but we're not simple!)
- **Alternative Considered**: MySQL (similar, but PostgreSQL has better JSON support)

---

## 🚧 Challenges & Solutions

### Challenge 1: Docker Orchestration from Java

**Problem**: Executing Docker commands from Java and capturing I/O

**Solution**:
- Used `ProcessBuilder` to execute `docker run` commands
- Configured stdin redirection for test input
- Captured stdout/stderr using separate threads
- Implemented timeout mechanism with `waitFor(timeout)`

**Code Snippet**:
```java
ProcessBuilder pb = new ProcessBuilder(dockerCommand);
Process process = pb.start();
String output = readOutput(process.getInputStream());
boolean finished = process.waitFor(timeLimitMs, TimeUnit.MILLISECONDS);
```

**Learning**: ProcessBuilder is powerful but requires careful handling of streams and timeouts.

### Challenge 2: Resource Cleanup

**Problem**: Orphaned Docker containers and temp files

**Solution**:
- Use `--rm` flag for automatic container removal
- Implement comprehensive try-finally cleanup
- Periodic cleanup job for orphaned resources
- Proper exception handling throughout

**Key Learning**: Always clean up resources, even in error scenarios.

### Challenge 3: Leaderboard Performance

**Problem**: Calculating leaderboard from thousands of submissions

**Solution**:
- Native SQL query with CTEs for optimal performance
- Calculate best score per problem per user
- Database indexes on (contest_id, user_id, score)
- Cache results (future improvement)

**Query Optimization**:
```sql
WITH user_scores AS (
  SELECT user_id, problem_id, MAX(score) as best_score
  FROM submissions
  WHERE contest_id = ? AND status = 'ACCEPTED'
  GROUP BY user_id, problem_id
)
SELECT user_id, SUM(best_score) as total_score
FROM user_scores
GROUP BY user_id
ORDER BY total_score DESC, last_submission_time ASC
```

**Learning**: Database-level aggregation is orders of magnitude faster than application-level.

### Challenge 4: Frontend State Management

**Problem**: Managing user state, code persistence, and real-time updates

**Solution**:
- LocalStorage for persistent data (user, code)
- React state for UI state
- Custom hooks for polling logic
- Clean separation of concerns

**Pattern Used**:
```typescript
// Custom hook for submission polling
const useSubmissionPolling = (submissionId, status) => {
  useEffect(() => {
    if (shouldPoll(status)) {
      const interval = setInterval(fetchStatus, 2000);
      return () => clearInterval(interval);
    }
  }, [submissionId, status]);
};
```

**Learning**: Custom hooks are perfect for encapsulating complex polling logic.

### Challenge 5: Multi-language Support

**Problem**: Different compilation/execution commands for each language

**Solution**:
- Strategy pattern with `LanguageStrategy` component
- Centralized configuration for each language
- Easy to add new languages

**Implementation**:
```java
interface LanguageConfig {
  String fileName;
  String compileCommand;
  String runCommand;
  boolean needsCompilation;
}
```

**Learning**: Design patterns make extensibility trivial.

---

## 🔮 Future Improvements

### Short Term
- [ ] WebSocket for real-time updates (eliminate polling)
- [ ] Redis for submission queue (better scalability)
- [ ] More languages (Go, Rust, Ruby)
- [ ] Detailed test case feedback (which test case failed)
- [ ] Contest timer/countdown

### Medium Term
- [ ] OAuth authentication (Google, GitHub)
- [ ] User profiles and submission history
- [ ] Contest creation UI for admins
- [ ] Problem difficulty ratings
- [ ] Editorial solutions and hints

### Long Term
- [ ] Kubernetes deployment for horizontal scaling
- [ ] Multi-region support
- [ ] AI-powered code analysis and suggestions
- [ ] Video tutorials for problems
- [ ] Team contests and tournaments
- [ ] Gamification (badges, achievements)

---

## 📝 License

This project is built for the Shodh AI assessment.

---

## 👨‍💻 Developer

Built with ❤️ for Shodh AI

**Key Stats**:
- **Backend**: ~40 Java files, ~4,000 lines of code
- **Frontend**: ~25 TypeScript files, ~3,000 lines of code
- **Total Development Time**: ~120 hours
- **Coffee Consumed**: ∞

---

## 📞 Support

For issues or questions:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review logs: `docker-compose logs -f`
3. Verify all services are running: `docker-compose ps`

---

**🎉 Thank you for checking out Shodh-a-Code!**

*Happy Coding!* 🚀

# shodAI
