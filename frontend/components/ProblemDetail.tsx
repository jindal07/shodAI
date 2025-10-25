'use client';

import { useState, useEffect } from 'react';
import { Clock, Database, Loader2 } from 'lucide-react';
import { getProblem } from '@/lib/api';
import { getDifficultyColor, formatTime } from '@/lib/utils';
import type { ProblemDetail as ProblemDetailType } from '@/lib/types';

interface ProblemDetailProps {
  contestId: number;
  problemId: number;
}

export default function ProblemDetail({ contestId, problemId }: ProblemDetailProps) {
  const [problem, setProblem] = useState<ProblemDetailType | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProblem();
  }, [problemId]);

  const loadProblem = async () => {
    setLoading(true);
    try {
      const data = await getProblem(contestId, problemId);
      setProblem(data);
    } catch (err) {
      console.error('Error loading problem:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-8">
        <Loader2 className="animate-spin h-8 w-8 text-blue-600" />
      </div>
    );
  }

  if (!problem) {
    return <div className="text-red-600">Failed to load problem</div>;
  }

  return (
    <div>
      {/* Title and Metadata */}
      <div className="mb-4">
        <div className="flex items-center justify-between mb-2">
          <h2 className="text-2xl font-bold text-gray-900">{problem.title}</h2>
          <span
            className={`px-3 py-1 text-sm font-medium rounded-full ${getDifficultyColor(
              problem.difficulty
            )}`}
          >
            {problem.difficulty}
          </span>
        </div>

        <div className="flex items-center space-x-4 text-sm text-gray-600">
          <div className="flex items-center space-x-1">
            <Clock className="w-4 h-4" />
            <span>{formatTime(problem.timeLimitMs)}</span>
          </div>
          <div className="flex items-center space-x-1">
            <Database className="w-4 h-4" />
            <span>{problem.memoryLimitMb}MB</span>
          </div>
        </div>
      </div>

      {/* Description */}
      <div className="mb-6">
        <h3 className="text-lg font-semibold mb-2">Description</h3>
        <div className="prose max-w-none">
          <pre className="whitespace-pre-wrap text-sm text-gray-700 font-sans">
            {problem.description}
          </pre>
        </div>
      </div>

      {/* Sample Test Cases */}
      {problem.sampleTestCases && problem.sampleTestCases.length > 0 && (
        <div>
          <h3 className="text-lg font-semibold mb-3">Sample Test Cases</h3>
          <div className="space-y-4">
            {problem.sampleTestCases.map((testCase, index) => (
              <div key={testCase.id} className="bg-gray-50 rounded-lg p-4">
                <div className="font-semibold text-gray-900 mb-2">
                  Example {index + 1}
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <div className="text-sm font-medium text-gray-600 mb-1">
                      Input:
                    </div>
                    <pre className="bg-white p-2 rounded border border-gray-200 text-sm overflow-x-auto">
                      {testCase.input}
                    </pre>
                  </div>
                  <div>
                    <div className="text-sm font-medium text-gray-600 mb-1">
                      Output:
                    </div>
                    <pre className="bg-white p-2 rounded border border-gray-200 text-sm overflow-x-auto">
                      {testCase.expectedOutput}
                    </pre>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

