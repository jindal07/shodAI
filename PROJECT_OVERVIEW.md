# Shodh-a-Code Contest Platform - Project Overview

## 📚 Documentation Guide

This project includes comprehensive planning documentation to guide you through building a full-stack coding contest platform. Here's how to use these documents:

### 📄 Available Documents

1. **PROJECT_PLAN.md** (Main Document)
   - Complete development roadmap
   - 21-day timeline with phases
   - Detailed task breakdown
   - Technology stack details
   - Architecture overview
   - **Start here** for overall understanding

2. **ARCHITECTURE.md** (Technical Reference)
   - System architecture diagrams
   - Component architecture
   - Data model and relationships
   - Code judge engine design
   - API specifications
   - Security considerations
   - **Use this** for technical decisions

3. **QUICKSTART.md** (Getting Started)
   - Immediate action steps
   - Setup instructions
   - Code snippets ready to use
   - Development workflow
   - Troubleshooting guide
   - **Use this** to start coding today

4. **CHECKLIST.md** (Progress Tracking)
   - 250+ checkboxes
   - Organized by phase
   - Daily progress tracking
   - Success criteria
   - **Use this** to track completion

5. **PROJECT_OVERVIEW.md** (This Document)
   - Documentation guide
   - Quick reference
   - Key concepts
   - Recommended workflow

---

## 🎯 Project Summary

### What You're Building
A **live coding contest platform** where users can:
- Join contests using a contest ID
- Solve coding problems
- Submit code in multiple languages (Java, Python, C++)
- See real-time submission results
- Compete on a live leaderboard

### Technology Stack
- **Backend**: Spring Boot (Java 17+)
- **Frontend**: Next.js 14+ with TypeScript
- **Database**: PostgreSQL 15+
- **Styling**: Tailwind CSS
- **Code Execution**: Docker containers
- **Orchestration**: Docker Compose

### Core Features
1. **Contest Management**: Create and join contests
2. **Problem Display**: View problems with descriptions and test cases
3. **Code Editor**: Write and submit code
4. **Judge Engine**: Execute code in sandboxed Docker containers
5. **Real-time Status**: Poll for submission results
6. **Leaderboard**: Live rankings based on scores

---

## 🚀 Recommended Workflow

### Week 1: Backend Foundation
**Days 1-2: Setup**
- Read: QUICKSTART.md (Step 1-9)
- Set up development environment
- Initialize projects
- Verify everything runs

**Days 3-5: Core Backend**
- Refer to: PROJECT_PLAN.md (Phase 2)
- Check: CHECKLIST.md (Phase 2)
- Create entities and repositories
- Implement basic API endpoints
- Test with Postman

**Days 6-7: Judge Engine Foundation**
- Refer to: ARCHITECTURE.md (Code Judge Engine)
- Create Docker execution environment
- Implement basic code execution

### Week 2: Integration & Frontend
**Days 8-9: Complete Judge Engine**
- Refer to: PROJECT_PLAN.md (Phase 3)
- Check: CHECKLIST.md (Phase 5)
- Implement full judge logic
- Add queue management
- Test all verdicts (AC, WA, TLE, etc.)

**Days 10-12: Frontend Core**
- Refer to: PROJECT_PLAN.md (Phase 5)
- Check: CHECKLIST.md (Phase 6-7)
- Build join page
- Create contest page layout
- Implement code editor
- Connect to backend API

**Days 13-14: Real-time Features**
- Implement submission status polling
- Add leaderboard with auto-refresh
- Test end-to-end flow

### Week 3: Polish & Submission
**Days 15-17: Polish**
- Refer to: CHECKLIST.md (Phase 8)
- Improve UI/UX
- Add loading states and animations
- Test responsive design
- Fix bugs

**Days 18-19: DevOps & Testing**
- Refer to: PROJECT_PLAN.md (Phase 7, 9)
- Create docker-compose.yml
- Add sample data
- Comprehensive testing
- Performance optimization

**Days 20-21: Documentation & Submission**
- Refer to: PROJECT_PLAN.md (Phase 8, 10)
- Write comprehensive README
- Document architecture decisions
- Final testing on fresh machine
- Submit to GitHub

---

## 💡 Key Concepts

### 1. Docker-Based Code Execution
The judge engine runs user code in isolated Docker containers:
```
User submits code → Backend saves it → Queue picks it up
→ Write code to temp file → Execute in Docker container
→ Capture output → Validate against expected output
→ Update submission status → Clean up
```

### 2. Asynchronous Processing
Submissions are processed asynchronously to avoid blocking:
```
Frontend submits → Backend returns submissionId immediately
→ Frontend polls GET /submissions/{id} every 2-3 seconds
→ Backend processes in background → Updates status
→ Frontend shows status updates
```

### 3. Polling vs WebSocket
This project uses **polling** for simplicity:
- Frontend requests status every few seconds
- Easier to implement than WebSocket
- Good enough for contest scenario
- Can be upgraded to WebSocket later

### 4. Leaderboard Calculation
Leaderboard is calculated from submissions:
```sql
SELECT username, SUM(score) as total_score, 
       MAX(submitted_at) as last_submission
FROM submissions 
WHERE contest_id = ? AND status = 'ACCEPTED'
GROUP BY user_id, username
ORDER BY total_score DESC, last_submission ASC
```

### 5. Security Through Isolation
Docker provides security for code execution:
- Each submission runs in isolated container
- No network access (--network=none)
- Resource limits (memory, CPU, time)
- Non-root user inside container
- Auto-cleanup after execution

---

## 📊 Project Metrics

### Estimated Effort
- **Total Time**: 120-160 hours (3-4 weeks full-time)
- **Backend Development**: 40% (48-64 hours)
- **Frontend Development**: 30% (36-48 hours)
- **Judge Engine**: 15% (18-24 hours)
- **Testing & Documentation**: 15% (18-24 hours)

### Lines of Code (Estimated)
- **Backend**: ~3,000-4,000 lines
- **Frontend**: ~2,000-3,000 lines
- **Total**: ~5,000-7,000 lines

### File Structure (Final)
```
shodh-a-code/
├── backend/                    (~30-40 files)
│   ├── src/main/java/
│   │   └── com/shodhai/contest/
│   │       ├── model/          (6 entities)
│   │       ├── repository/     (6 repositories)
│   │       ├── service/        (5-6 services)
│   │       ├── controller/     (3-4 controllers)
│   │       ├── dto/           (8-10 DTOs)
│   │       └── judge/         (5-6 judge classes)
│   └── src/main/resources/
│       ├── application.yml
│       └── data.sql
├── frontend/                   (~20-30 files)
│   ├── app/
│   │   ├── page.tsx
│   │   ├── join/
│   │   └── contest/[id]/
│   ├── components/            (10-15 components)
│   ├── lib/                   (api.ts, types.ts)
│   └── hooks/                 (3-4 custom hooks)
├── docker/
│   └── judge-environment/
│       └── Dockerfile
├── docker-compose.yml
├── README.md                  (Main documentation)
├── PROJECT_PLAN.md
├── ARCHITECTURE.md
├── QUICKSTART.md
├── CHECKLIST.md
└── PROJECT_OVERVIEW.md
```

---

## 🎓 Learning Outcomes

By completing this project, you will demonstrate:

### Backend Skills
✅ RESTful API design
✅ Spring Boot application structure
✅ JPA/Hibernate ORM
✅ PostgreSQL database design
✅ Asynchronous processing
✅ Process orchestration (Docker from Java)
✅ Error handling and validation

### Frontend Skills
✅ Next.js App Router
✅ TypeScript
✅ React hooks and state management
✅ API integration
✅ Polling mechanisms
✅ Responsive design with Tailwind CSS
✅ Code editor integration

### DevOps Skills
✅ Docker containerization
✅ Docker Compose orchestration
✅ Multi-service deployment
✅ Environment configuration
✅ Resource management

### System Design Skills
✅ Microservices architecture
✅ Asynchronous workflows
✅ Real-time updates (polling)
✅ Scalability considerations
✅ Security in code execution

---

## 🔥 Quick Start (TL;DR)

If you want to start coding **right now**:

1. **Read**: QUICKSTART.md sections 1-9
2. **Set up**: Development environment (2 hours)
3. **Code**: Follow Day 2 in QUICKSTART.md
4. **Track**: Use CHECKLIST.md to mark progress
5. **Reference**: Use ARCHITECTURE.md when making decisions
6. **Build**: Follow PROJECT_PLAN.md phases sequentially

---

## 🛠️ Troubleshooting Resources

### Common Issues

**Backend won't start**
→ Check: QUICKSTART.md → Troubleshooting section

**Database connection error**
→ Verify PostgreSQL is running: `docker ps`
→ Check application.yml connection string

**Frontend API calls failing**
→ Verify backend is running on port 8080
→ Check CORS configuration
→ Verify .env.local has correct API URL

**Docker execution failing**
→ Ensure Docker daemon is running
→ Check if judge image is built
→ Review ARCHITECTURE.md → Docker Execution Flow

**Getting lost in planning**
→ Focus on QUICKSTART.md first
→ Use CHECKLIST.md for next immediate task
→ Refer to PROJECT_PLAN.md for context

---

## 📝 Documentation Quality Checklist

Before submission, ensure your README includes:

- [ ] **Overview**: Clear description of what the project does
- [ ] **Screenshots**: At least 2-3 screenshots or GIFs
- [ ] **Setup**: Step-by-step instructions that work on fresh machine
- [ ] **API Docs**: All endpoints with request/response examples
- [ ] **Architecture**: System diagram and component explanation
- [ ] **Decisions**: Why you chose specific approaches
- [ ] **Challenges**: Problems faced and how you solved them
- [ ] **Future Work**: Ideas for improvement

---

## 🎯 Success Indicators

You're on track if:
- ✅ By Day 5: Basic API endpoints working
- ✅ By Day 10: Code execution in Docker working
- ✅ By Day 15: Frontend displays data and submits code
- ✅ By Day 18: End-to-end flow working
- ✅ By Day 20: Docker Compose starts everything
- ✅ By Day 21: Documentation complete

---

## 🏆 Evaluation Focus

Reviewers will pay special attention to:

1. **Technical Execution** (40%)
   - Does everything work as specified?
   - Are edge cases handled?
   - Is error handling robust?

2. **Code Quality** (25%)
   - Clean, readable code
   - Good architecture
   - Proper separation of concerns

3. **Docker Integration** (20%)
   - Reliable code execution
   - Proper resource management
   - Security considerations

4. **Communication** (15%)
   - Clear README
   - Well-explained decisions
   - Professional presentation

---

## 📞 When You Need Help

### Self-Help Resources
1. **Review**: Relevant section in documentation
2. **Search**: Stack Overflow for specific errors
3. **Check**: GitHub issues for similar projects
4. **Read**: Official documentation (Spring Boot, Next.js)

### Documentation Map
- **"How do I start?"** → QUICKSTART.md
- **"What should I do next?"** → CHECKLIST.md
- **"How does X work?"** → ARCHITECTURE.md
- **"What's the timeline?"** → PROJECT_PLAN.md
- **"What am I building?"** → This document

---

## 🎉 Final Thoughts

This is a **substantial project** that will take dedicated effort. The good news:

✅ You have a complete roadmap
✅ Every feature is documented
✅ Code snippets are provided
✅ Architecture is pre-designed
✅ Common issues are addressed

### Your Job
1. Follow the plan
2. Write the code
3. Test thoroughly
4. Document well
5. Submit with confidence

### Recommended Mindset
- **Start small**: Get one feature working end-to-end
- **Iterate**: Don't try to perfect everything at once
- **Test often**: Catch issues early
- **Commit frequently**: Track your progress
- **Document as you go**: Don't leave it to the end
- **Ask for help**: When truly stuck (but try first!)

---

## 🚀 Ready to Begin?

**Your first action**: Open `QUICKSTART.md` and start with Step 1.

Good luck building Shodh-a-Code! 🎊

---

## 📈 Quick Reference Card

| Need | Document | Section |
|------|----------|---------|
| Start coding now | QUICKSTART.md | Step 1-9 |
| Understand architecture | ARCHITECTURE.md | System Overview |
| Track progress | CHECKLIST.md | All phases |
| See timeline | PROJECT_PLAN.md | Phase 1-10 |
| Get API specs | ARCHITECTURE.md | API Specification |
| Database design | ARCHITECTURE.md | Data Model |
| Judge engine details | ARCHITECTURE.md | Code Judge Engine |
| Troubleshooting | QUICKSTART.md | Troubleshooting |
| Daily workflow | QUICKSTART.md | Development Workflow |
| What to submit | PROJECT_PLAN.md | Phase 10 |

---

**Document Version**: 1.0
**Last Updated**: October 25, 2025
**Estimated Read Time**: 10 minutes
**Project Duration**: 3-4 weeks (120-160 hours)

*Let's build something amazing! 🚀*

