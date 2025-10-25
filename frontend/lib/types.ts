// Type definitions for the application

export interface Contest {
  id: number;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  isActive: boolean;
  problems: ProblemSummary[];
}

export interface ProblemSummary {
  id: number;
  title: string;
  difficulty: 'EASY' | 'MEDIUM' | 'HARD';
  timeLimitMs: number;
  memoryLimitMb: number;
}

export interface ProblemDetail {
  id: number;
  title: string;
  description: string;
  difficulty: 'EASY' | 'MEDIUM' | 'HARD';
  timeLimitMs: number;
  memoryLimitMb: number;
  sampleTestCases: TestCaseSummary[];
}

export interface TestCaseSummary {
  id: number;
  input: string;
  expectedOutput: string;
}

export interface Submission {
  id: number;
  userId: number;
  problemId: number;
  contestId: number;
  language: string;
  status: SubmissionStatus;
  result?: string;
  score: number;
  executionTimeMs?: number;
  memoryUsedMb?: number;
  errorMessage?: string;
  testCasesPassed: number;
  totalTestCases: number;
  submittedAt: string;
  completedAt?: string;
  testCaseResults?: TestCaseResult[];
}

export type SubmissionStatus = 
  | 'PENDING' 
  | 'RUNNING' 
  | 'ACCEPTED' 
  | 'WRONG_ANSWER' 
  | 'TIME_LIMIT_EXCEEDED'
  | 'MEMORY_LIMIT_EXCEEDED'
  | 'RUNTIME_ERROR'
  | 'COMPILATION_ERROR'
  | 'SYSTEM_ERROR';

export interface TestCaseResult {
  testCaseNumber: number;
  passed: boolean;
  executionTimeMs: number;
  verdict: string;
}

export interface LeaderboardEntry {
  rank: number;
  userId: number;
  username: string;
  totalScore: number;
  problemsSolved: number;
  lastSubmissionTime: string;
}

export interface LeaderboardResponse {
  contestId: number;
  lastUpdated: string;
  entries: LeaderboardEntry[];
}

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
  timestamp: string;
}

export type Language = 'JAVA' | 'PYTHON' | 'CPP' | 'JAVASCRIPT';

export interface User {
  id: number;
  username: string;
  email?: string;
}

