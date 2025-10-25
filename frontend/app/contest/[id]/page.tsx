'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import { Code2, Loader2 } from 'lucide-react';
import { getContest } from '@/lib/api';
import type { Contest, User } from '@/lib/types';
import ProblemList from '@/components/ProblemList';
import ProblemDetail from '@/components/ProblemDetail';
import CodeEditor from '@/components/CodeEditor';
import Leaderboard from '@/components/Leaderboard';

export default function ContestPage() {
  const params = useParams();
  const contestId = parseInt(params.id as string);
  
  const [contest, setContest] = useState<Contest | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const [selectedProblemId, setSelectedProblemId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Load user from localStorage
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    } else {
      window.location.href = '/join';
      return;
    }

    // Load contest data
    loadContest();
  }, [contestId]);

  const loadContest = async () => {
    try {
      const data = await getContest(contestId);
      setContest(data);
      
      // Select first problem by default
      if (data.problems && data.problems.length > 0) {
        setSelectedProblemId(data.problems[0].id);
      }
      
      setLoading(false);
    } catch (err: any) {
      console.error('Error loading contest:', err);
      setError('Failed to load contest. Please try again.');
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="animate-spin h-12 w-12 text-blue-600 mx-auto mb-4" />
          <p className="text-gray-600">Loading contest...</p>
        </div>
      </div>
    );
  }

  if (error || !contest) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <p className="text-red-600 mb-4">{error || 'Contest not found'}</p>
          <a href="/join" className="text-blue-600 hover:text-blue-700">
            ← Back to Join
          </a>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 shadow-sm">
        <div className="container mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <Code2 className="w-8 h-8 text-blue-600" />
            <div>
              <h1 className="text-xl font-bold text-gray-900">{contest.title}</h1>
              <p className="text-sm text-gray-600">{contest.description}</p>
            </div>
          </div>
          <div className="flex items-center space-x-4">
            <div className="text-right">
              <p className="text-sm text-gray-600">Logged in as</p>
              <p className="font-semibold text-gray-900">{user?.username}</p>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="flex-1 container mx-auto px-4 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 h-full">
          {/* Left Sidebar - Problems */}
          <div className="lg:col-span-3 h-full">
            <div className="bg-white rounded-lg shadow-md p-4 h-full overflow-y-auto">
              <h2 className="text-lg font-semibold mb-4">Problems</h2>
              <ProblemList
                problems={contest.problems}
                selectedProblemId={selectedProblemId}
                onSelectProblem={setSelectedProblemId}
              />
            </div>
          </div>

          {/* Middle - Problem & Code Editor */}
          <div className="lg:col-span-6 h-full flex flex-col space-y-6">
            {/* Problem Description */}
            <div className="bg-white rounded-lg shadow-md p-6 overflow-y-auto max-h-[400px]">
              {selectedProblemId ? (
                <ProblemDetail
                  contestId={contestId}
                  problemId={selectedProblemId}
                />
              ) : (
                <p className="text-gray-500">Select a problem to get started</p>
              )}
            </div>

            {/* Code Editor */}
            <div className="flex-1 bg-white rounded-lg shadow-md overflow-hidden">
              {selectedProblemId && user ? (
                <CodeEditor
                  contestId={contestId}
                  problemId={selectedProblemId}
                  userId={user.id}
                />
              ) : (
                <div className="h-full flex items-center justify-center text-gray-500">
                  Select a problem to start coding
                </div>
              )}
            </div>
          </div>

          {/* Right Sidebar - Leaderboard */}
          <div className="lg:col-span-3 h-full">
            <div className="bg-white rounded-lg shadow-md p-4 h-full overflow-y-auto">
              <h2 className="text-lg font-semibold mb-4">Leaderboard</h2>
              <Leaderboard contestId={contestId} currentUserId={user?.id} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

