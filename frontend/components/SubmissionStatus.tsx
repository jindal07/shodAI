'use client';

import { useState, useEffect } from 'react';
import {
  CheckCircle,
  XCircle,
  Clock,
  Loader2,
  AlertTriangle,
  X,
} from 'lucide-react';
import { getSubmissionStatus } from '@/lib/api';
import { getStatusColor, getStatusText, formatTime, formatMemory } from '@/lib/utils';
import type { Submission, SubmissionStatus as Status } from '@/lib/types';

interface SubmissionStatusProps {
  submission: Submission;
  onClose: () => void;
}

export default function SubmissionStatus({
  submission: initialSubmission,
  onClose,
}: SubmissionStatusProps) {
  const [submission, setSubmission] = useState<Submission>(initialSubmission);
  const [polling, setPolling] = useState(true);

  useEffect(() => {
    if (!shouldPoll(submission.status)) {
      setPolling(false);
      return;
    }

    const interval = setInterval(async () => {
      try {
        const updated = await getSubmissionStatus(submission.id);
        setSubmission(updated);

        if (!shouldPoll(updated.status)) {
          setPolling(false);
          clearInterval(interval);
        }
      } catch (err) {
        console.error('Error polling submission status:', err);
      }
    }, 2000); // Poll every 2 seconds

    return () => clearInterval(interval);
  }, [submission.id, submission.status]);

  const shouldPoll = (status: Status): boolean => {
    return status === 'PENDING' || status === 'RUNNING';
  };

  const getStatusIcon = () => {
    switch (submission.status) {
      case 'ACCEPTED':
        return <CheckCircle className="w-6 h-6 text-green-600" />;
      case 'WRONG_ANSWER':
        return <XCircle className="w-6 h-6 text-red-600" />;
      case 'PENDING':
      case 'RUNNING':
        return <Loader2 className="w-6 h-6 text-blue-600 animate-spin" />;
      case 'TIME_LIMIT_EXCEEDED':
      case 'MEMORY_LIMIT_EXCEEDED':
        return <Clock className="w-6 h-6 text-orange-600" />;
      default:
        return <AlertTriangle className="w-6 h-6 text-purple-600" />;
    }
  };

  return (
    <div className="bg-white px-4 py-3">
      <div className="flex items-start justify-between mb-3">
        <div className="flex items-center space-x-3">
          {getStatusIcon()}
          <div>
            <h3 className="font-semibold text-gray-900">
              {getStatusText(submission.status)}
            </h3>
            <p className="text-sm text-gray-600">
              {polling ? 'Judging in progress...' : 'Submission completed'}
            </p>
          </div>
        </div>
        <button
          onClick={onClose}
          className="text-gray-400 hover:text-gray-600 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>
      </div>

      {/* Results */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-3">
        <div className="bg-gray-50 rounded-lg p-3">
          <div className="text-xs text-gray-600 mb-1">Status</div>
          <div
            className={`inline-block px-2 py-1 text-xs font-medium rounded ${getStatusColor(
              submission.status
            )}`}
          >
            {submission.result || submission.status}
          </div>
        </div>

        <div className="bg-gray-50 rounded-lg p-3">
          <div className="text-xs text-gray-600 mb-1">Score</div>
          <div className="text-lg font-semibold text-gray-900">
            {submission.score || 0}
          </div>
        </div>

        <div className="bg-gray-50 rounded-lg p-3">
          <div className="text-xs text-gray-600 mb-1">Test Cases</div>
          <div className="text-lg font-semibold text-gray-900">
            {submission.testCasesPassed}/{submission.totalTestCases}
          </div>
        </div>

        <div className="bg-gray-50 rounded-lg p-3">
          <div className="text-xs text-gray-600 mb-1">Time</div>
          <div className="text-lg font-semibold text-gray-900">
            {formatTime(submission.executionTimeMs)}
          </div>
        </div>
      </div>

      {/* Error Message */}
      {submission.errorMessage && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-3">
          <div className="text-xs font-medium text-red-800 mb-1">Error:</div>
          <pre className="text-xs text-red-700 overflow-x-auto whitespace-pre-wrap">
            {submission.errorMessage}
          </pre>
        </div>
      )}

      {/* Success Message */}
      {submission.status === 'ACCEPTED' && (
        <div className="bg-green-50 border border-green-200 rounded-lg p-3 text-center">
          <p className="text-green-800 font-medium">
            🎉 Congratulations! All test cases passed!
          </p>
        </div>
      )}
    </div>
  );
}

