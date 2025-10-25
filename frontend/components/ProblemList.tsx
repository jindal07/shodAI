import { CheckCircle } from 'lucide-react';
import { getDifficultyColor } from '@/lib/utils';
import type { ProblemSummary } from '@/lib/types';

interface ProblemListProps {
  problems: ProblemSummary[];
  selectedProblemId: number | null;
  onSelectProblem: (problemId: number) => void;
}

export default function ProblemList({
  problems,
  selectedProblemId,
  onSelectProblem,
}: ProblemListProps) {
  return (
    <div className="space-y-2">
      {problems.map((problem, index) => (
        <button
          key={problem.id}
          onClick={() => onSelectProblem(problem.id)}
          className={`w-full text-left p-3 rounded-lg border-2 transition-all ${
            selectedProblemId === problem.id
              ? 'border-blue-500 bg-blue-50'
              : 'border-gray-200 hover:border-gray-300 hover:bg-gray-50'
          }`}
        >
          <div className="flex items-start justify-between">
            <div className="flex-1">
              <div className="flex items-center space-x-2">
                <span className="font-semibold text-sm text-gray-900">
                  {index + 1}. {problem.title}
                </span>
              </div>
              <div className="mt-1">
                <span
                  className={`inline-block px-2 py-0.5 text-xs font-medium rounded ${getDifficultyColor(
                    problem.difficulty
                  )}`}
                >
                  {problem.difficulty}
                </span>
              </div>
            </div>
          </div>
        </button>
      ))}
    </div>
  );
}

