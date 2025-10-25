import { type ClassValue, clsx } from 'clsx';
import type { SubmissionStatus } from './types';

export function cn(...inputs: ClassValue[]) {
  return clsx(inputs);
}

export function getDifficultyColor(difficulty: string): string {
  switch (difficulty.toUpperCase()) {
    case 'EASY':
      return 'text-green-600 bg-green-100';
    case 'MEDIUM':
      return 'text-yellow-600 bg-yellow-100';
    case 'HARD':
      return 'text-red-600 bg-red-100';
    default:
      return 'text-gray-600 bg-gray-100';
  }
}

export function getStatusColor(status: SubmissionStatus): string {
  switch (status) {
    case 'ACCEPTED':
      return 'text-green-600 bg-green-100';
    case 'PENDING':
    case 'RUNNING':
      return 'text-blue-600 bg-blue-100';
    case 'WRONG_ANSWER':
      return 'text-red-600 bg-red-100';
    case 'TIME_LIMIT_EXCEEDED':
    case 'MEMORY_LIMIT_EXCEEDED':
      return 'text-orange-600 bg-orange-100';
    case 'RUNTIME_ERROR':
    case 'COMPILATION_ERROR':
      return 'text-purple-600 bg-purple-100';
    case 'SYSTEM_ERROR':
      return 'text-gray-600 bg-gray-100';
    default:
      return 'text-gray-600 bg-gray-100';
  }
}

export function getStatusText(status: SubmissionStatus): string {
  switch (status) {
    case 'PENDING':
      return 'Pending';
    case 'RUNNING':
      return 'Running';
    case 'ACCEPTED':
      return 'Accepted';
    case 'WRONG_ANSWER':
      return 'Wrong Answer';
    case 'TIME_LIMIT_EXCEEDED':
      return 'Time Limit Exceeded';
    case 'MEMORY_LIMIT_EXCEEDED':
      return 'Memory Limit Exceeded';
    case 'RUNTIME_ERROR':
      return 'Runtime Error';
    case 'COMPILATION_ERROR':
      return 'Compilation Error';
    case 'SYSTEM_ERROR':
      return 'System Error';
    default:
      return status;
  }
}

export function formatTime(ms?: number): string {
  if (!ms) return '-';
  if (ms < 1000) return `${ms}ms`;
  return `${(ms / 1000).toFixed(2)}s`;
}

export function formatMemory(mb?: number): string {
  if (!mb) return '-';
  return `${mb}MB`;
}

export function formatDate(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleString();
}

export function getLanguageDisplayName(language: string): string {
  switch (language.toUpperCase()) {
    case 'JAVA':
      return 'Java';
    case 'PYTHON':
      return 'Python';
    case 'CPP':
      return 'C++';
    case 'JAVASCRIPT':
      return 'JavaScript';
    default:
      return language;
  }
}

export function getCodeTemplate(language: string, problemTitle: string): string {
  switch (language.toUpperCase()) {
    case 'JAVA':
      return `import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Read input
        
        // Solve problem
        
        // Print output
        
        sc.close();
    }
}`;
    case 'PYTHON':
      return `# ${problemTitle}

def solve():
    # Read input
    
    # Solve problem
    
    # Print output
    pass

if __name__ == "__main__":
    solve()`;
    case 'CPP':
      return `#include <iostream>
#include <vector>
using namespace std;

int main() {
    // Read input
    
    // Solve problem
    
    // Print output
    
    return 0;
}`;
    case 'JAVASCRIPT':
      return `// ${problemTitle}

const readline = require('readline');
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

let lines = [];
rl.on('line', (line) => {
    lines.push(line);
}).on('close', () => {
    // Read input from lines array
    
    // Solve problem
    
    // Print output
    console.log();
});`;
    default:
      return '// Start coding here...';
  }
}

