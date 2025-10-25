'use client';

import { useState, useEffect } from 'react';
import { Trophy, Loader2, RefreshCw } from 'lucide-react';
import { getLeaderboard } from '@/lib/api';
import { formatDate } from '@/lib/utils';
import type { LeaderboardResponse } from '@/lib/types';

interface LeaderboardProps {
  contestId: number;
  currentUserId?: number;
}

export default function Leaderboard({ contestId, currentUserId }: LeaderboardProps) {
  const [leaderboard, setLeaderboard] = useState<LeaderboardResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [lastUpdate, setLastUpdate] = useState<string>('');

  useEffect(() => {
    loadLeaderboard();

    // Auto-refresh every 20 seconds
    const interval = setInterval(loadLeaderboard, 20000);

    return () => clearInterval(interval);
  }, [contestId]);

  const loadLeaderboard = async () => {
    try {
      const data = await getLeaderboard(contestId);
      setLeaderboard(data);
      setLastUpdate(new Date().toLocaleTimeString());
      setLoading(false);
    } catch (err) {
      console.error('Error loading leaderboard:', err);
      setLoading(false);
    }
  };

  const getRankColor = (rank: number): string => {
    switch (rank) {
      case 1:
        return 'text-yellow-600 bg-yellow-50 border-yellow-300';
      case 2:
        return 'text-gray-600 bg-gray-50 border-gray-300';
      case 3:
        return 'text-orange-600 bg-orange-50 border-orange-300';
      default:
        return 'text-gray-600 bg-white border-gray-200';
    }
  };

  const getRankIcon = (rank: number) => {
    if (rank <= 3) {
      return <Trophy className="w-4 h-4" />;
    }
    return null;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-8">
        <Loader2 className="animate-spin h-8 w-8 text-blue-600" />
      </div>
    );
  }

  if (!leaderboard || leaderboard.entries.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">
        <Trophy className="w-12 h-12 mx-auto mb-2 text-gray-400" />
        <p>No submissions yet</p>
        <p className="text-sm mt-1">Be the first to submit!</p>
      </div>
    );
  }

  return (
    <div>
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-2 text-sm text-gray-600">
          <RefreshCw className="w-4 h-4" />
          <span>{lastUpdate}</span>
        </div>
        <button
          onClick={loadLeaderboard}
          className="text-blue-600 hover:text-blue-700 text-sm font-medium"
        >
          Refresh
        </button>
      </div>

      {/* Leaderboard List */}
      <div className="space-y-2">
        {leaderboard.entries.map((entry) => (
          <div
            key={entry.userId}
            className={`border-2 rounded-lg p-3 transition-all ${
              entry.userId === currentUserId
                ? 'border-blue-500 bg-blue-50'
                : getRankColor(entry.rank)
            }`}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <div className="flex items-center justify-center w-8 h-8 rounded-full bg-white border-2 border-current">
                  <span className="font-bold text-sm">#{entry.rank}</span>
                </div>
                <div>
                  <div className="font-semibold text-gray-900 flex items-center space-x-1">
                    {entry.rank <= 3 && getRankIcon(entry.rank)}
                    <span>{entry.username}</span>
                  </div>
                  <div className="text-xs text-gray-600">
                    {entry.problemsSolved} problem{entry.problemsSolved !== 1 ? 's' : ''}{' '}
                    solved
                  </div>
                </div>
              </div>
              <div className="text-right">
                <div className="text-lg font-bold text-gray-900">
                  {entry.totalScore}
                </div>
                <div className="text-xs text-gray-600">points</div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Footer */}
      <div className="mt-4 text-center text-xs text-gray-500">
        Showing top {leaderboard.entries.length} participants
      </div>
    </div>
  );
}

