import axios from 'axios';
import type {
  ApiResponse,
  Contest,
  ProblemDetail,
  Submission,
  LeaderboardResponse,
  User,
} from './types';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export async function getContest(contestId: number): Promise<Contest> {
  const response = await api.get<ApiResponse<Contest>>(`/contests/${contestId}`);
  return response.data.data;
}

export async function joinContest(
  contestId: number,
  username: string,
  email?: string
): Promise<User> {
  const response = await api.post<ApiResponse<{ userId: number; username: string; message: string }>>(
    `/contests/${contestId}/join`,
    { username, email }
  );
  return {
    id: response.data.data.userId,
    username: response.data.data.username,
    email,
  };
}

export async function getProblem(
  contestId: number,
  problemId: number
): Promise<ProblemDetail> {
  const response = await api.get<ApiResponse<ProblemDetail>>(
    `/contests/${contestId}/problems/${problemId}`
  );
  return response.data.data;
}

export async function submitCode(data: {
  userId: number;
  problemId: number;
  contestId: number;
  code: string;
  language: string;
}): Promise<Submission> {
  const response = await api.post<ApiResponse<Submission>>('/submissions', data);
  return response.data.data;
}

export async function getSubmissionStatus(submissionId: number): Promise<Submission> {
  const response = await api.get<ApiResponse<Submission>>(`/submissions/${submissionId}`);
  return response.data.data;
}

export async function getLeaderboard(contestId: number): Promise<LeaderboardResponse> {
  const response = await api.get<ApiResponse<LeaderboardResponse>>(
    `/contests/${contestId}/leaderboard`
  );
  return response.data.data;
}

export { api };

