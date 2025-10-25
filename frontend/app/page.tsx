import Link from 'next/link';
import { Code2, Trophy, Users, Zap } from 'lucide-react';

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-16">
        {/* Header */}
        <div className="text-center mb-16">
          <div className="flex justify-center mb-4">
            <Code2 className="w-16 h-16 text-blue-600" />
          </div>
          <h1 className="text-5xl font-bold text-gray-900 mb-4">
            Shodh-a-Code
          </h1>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            Live Coding Contest Platform - Compete in real-time, solve challenging problems, and climb the leaderboard!
          </p>
        </div>

        {/* Features */}
        <div className="grid md:grid-cols-3 gap-8 mb-16">
          <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="flex justify-center mb-4">
              <Zap className="w-12 h-12 text-yellow-500" />
            </div>
            <h3 className="text-xl font-semibold text-center mb-2">
              Real-time Judging
            </h3>
            <p className="text-gray-600 text-center">
              Submit your code and see results instantly with our Docker-based execution engine
            </p>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="flex justify-center mb-4">
              <Trophy className="w-12 h-12 text-amber-500" />
            </div>
            <h3 className="text-xl font-semibold text-center mb-2">
              Live Leaderboard
            </h3>
            <p className="text-gray-600 text-center">
              Track your ranking in real-time and compete with other participants
            </p>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="flex justify-center mb-4">
              <Users className="w-12 h-12 text-green-500" />
            </div>
            <h3 className="text-xl font-semibold text-center mb-2">
              Multiple Languages
            </h3>
            <p className="text-gray-600 text-center">
              Code in Java, Python, C++, or JavaScript - your choice!
            </p>
          </div>
        </div>

        {/* CTA */}
        <div className="text-center">
          <Link
            href="/join"
            className="inline-block bg-blue-600 text-white px-8 py-4 rounded-lg text-lg font-semibold hover:bg-blue-700 transition-colors shadow-lg"
          >
            Join a Contest
          </Link>
          <p className="mt-4 text-gray-600">
            Enter a contest ID to get started
          </p>
        </div>

        {/* Footer */}
        <div className="mt-20 text-center text-gray-500 text-sm">
          <p>Built with Spring Boot, Next.js, and Docker</p>
          <p className="mt-2">© 2025 Shodh-a-Code Platform</p>
        </div>
      </div>
    </div>
  );
}

