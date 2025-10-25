'use client';

import { useState, useEffect } from 'react';
import Editor from '@monaco-editor/react';
import { Play, Loader2 } from 'lucide-react';
import { submitCode, getSubmissionStatus } from '@/lib/api';
import { getCodeTemplate, getLanguageDisplayName } from '@/lib/utils';
import SubmissionStatus from './SubmissionStatus';
import type { Language, Submission } from '@/lib/types';

interface CodeEditorProps {
  contestId: number;
  problemId: number;
  userId: number;
}

const LANGUAGES: Language[] = ['JAVA', 'PYTHON', 'CPP', 'JAVASCRIPT'];

export default function CodeEditor({ contestId, problemId, userId }: CodeEditorProps) {
  const [language, setLanguage] = useState<Language>('JAVA');
  const [code, setCode] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [currentSubmission, setCurrentSubmission] = useState<Submission | null>(null);

  useEffect(() => {
    // Load code from localStorage
    const savedCode = localStorage.getItem(`code_${contestId}_${problemId}_${language}`);
    if (savedCode) {
      setCode(savedCode);
    } else {
      setCode(getCodeTemplate(language, 'Problem'));
    }
  }, [contestId, problemId, language]);

  useEffect(() => {
    // Save code to localStorage
    if (code) {
      localStorage.setItem(`code_${contestId}_${problemId}_${language}`, code);
    }
  }, [code, contestId, problemId, language]);

  const handleLanguageChange = (newLanguage: Language) => {
    setLanguage(newLanguage);
    const savedCode = localStorage.getItem(`code_${contestId}_${problemId}_${newLanguage}`);
    if (savedCode) {
      setCode(savedCode);
    } else {
      setCode(getCodeTemplate(newLanguage, 'Problem'));
    }
  };

  const handleSubmit = async () => {
    if (!code.trim()) {
      alert('Please write some code before submitting');
      return;
    }

    if (!confirm('Submit your code for judging?')) {
      return;
    }

    setSubmitting(true);

    try {
      const submission = await submitCode({
        userId,
        problemId,
        contestId,
        code,
        language,
      });

      setCurrentSubmission(submission);
    } catch (err: any) {
      console.error('Error submitting code:', err);
      alert('Failed to submit code. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const getEditorLanguage = (lang: Language): string => {
    switch (lang) {
      case 'JAVA':
        return 'java';
      case 'PYTHON':
        return 'python';
      case 'CPP':
        return 'cpp';
      case 'JAVASCRIPT':
        return 'javascript';
      default:
        return 'plaintext';
    }
  };

  return (
    <div className="h-full flex flex-col">
      {/* Editor Header */}
      <div className="flex items-center justify-between px-4 py-3 border-b border-gray-200 bg-gray-50">
        <div className="flex items-center space-x-4">
          <span className="text-sm font-medium text-gray-700">Language:</span>
          <select
            value={language}
            onChange={(e) => handleLanguageChange(e.target.value as Language)}
            className="px-3 py-1.5 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            {LANGUAGES.map((lang) => (
              <option key={lang} value={lang}>
                {getLanguageDisplayName(lang)}
              </option>
            ))}
          </select>
        </div>

        <button
          onClick={handleSubmit}
          disabled={submitting}
          className="flex items-center space-x-2 px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {submitting ? (
            <>
              <Loader2 className="w-4 h-4 animate-spin" />
              <span>Submitting...</span>
            </>
          ) : (
            <>
              <Play className="w-4 h-4" />
              <span>Submit</span>
            </>
          )}
        </button>
      </div>

      {/* Monaco Editor */}
      <div className="flex-1">
        <Editor
          height="100%"
          language={getEditorLanguage(language)}
          value={code}
          onChange={(value) => setCode(value || '')}
          theme="vs-light"
          options={{
            minimap: { enabled: false },
            fontSize: 14,
            lineNumbers: 'on',
            scrollBeyondLastLine: false,
            automaticLayout: true,
            tabSize: 4,
            wordWrap: 'on',
          }}
        />
      </div>

      {/* Submission Status */}
      {currentSubmission && (
        <div className="border-t border-gray-200">
          <SubmissionStatus
            submission={currentSubmission}
            onClose={() => setCurrentSubmission(null)}
          />
        </div>
      )}
    </div>
  );
}

