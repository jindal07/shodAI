-- Sample Data for Shodh-a-Code Contest Platform

-- Insert sample contest
INSERT INTO contests (id, title, description, start_time, end_time, is_active, created_at)
VALUES 
(1, 'Weekly Contest #1', 'Test your coding skills with these algorithmic problems!', 
 NOW() - INTERVAL '1 hour', NOW() + INTERVAL '2 hours', true, NOW());

-- Insert sample problems
INSERT INTO problems (id, contest_id, title, description, difficulty, time_limit_ms, memory_limit_mb, display_order)
VALUES 
(1, 1, 'Two Sum', 
 'Given an array of integers nums and an integer target, return the indices of the two numbers such that they add up to target.

Input Format:
- First line: n (size of array)
- Second line: n space-separated integers (array elements)
- Third line: target integer

Output Format:
- Two space-separated integers (indices, 0-based)

Example:
Input:
4
2 7 11 15
9

Output:
0 1

Explanation: nums[0] + nums[1] = 2 + 7 = 9', 
 'EASY', 1000, 256, 1),

(2, 1, 'Valid Parentheses', 
 'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.

An input string is valid if:
1. Open brackets must be closed by the same type of brackets.
2. Open brackets must be closed in the correct order.

Input Format:
- Single line containing the string

Output Format:
- Print "true" if valid, "false" otherwise

Example:
Input:
()[]{}

Output:
true', 
 'EASY', 1000, 256, 2),

(3, 1, 'Fibonacci Number', 
 'Calculate the nth Fibonacci number. The Fibonacci sequence is defined as:
F(0) = 0, F(1) = 1
F(n) = F(n - 1) + F(n - 2) for n > 1

Input Format:
- Single integer n (0 <= n <= 30)

Output Format:
- Single integer: the nth Fibonacci number

Example:
Input:
10

Output:
55', 
 'MEDIUM', 1000, 256, 3);

-- Insert test cases for Problem 1: Two Sum
INSERT INTO test_cases (id, problem_id, input, expected_output, is_sample, points, display_order)
VALUES 
(1, 1, '4
2 7 11 15
9', '0 1', true, 20, 1),
(2, 1, '3
3 2 4
6', '1 2', true, 20, 2),
(3, 1, '2
3 3
6', '0 1', false, 20, 3),
(4, 1, '5
1 5 3 7 9
12', '2 3', false, 20, 4),
(5, 1, '6
0 4 3 0 2 1
0', '0 3', false, 20, 5);

-- Insert test cases for Problem 2: Valid Parentheses
INSERT INTO test_cases (id, problem_id, input, expected_output, is_sample, points, display_order)
VALUES 
(6, 2, '()', 'true', true, 20, 1),
(7, 2, '()[]{}', 'true', true, 20, 2),
(8, 2, '(]', 'false', false, 20, 3),
(9, 2, '([)]', 'false', false, 20, 4),
(10, 2, '{[]}', 'true', false, 20, 5);

-- Insert test cases for Problem 3: Fibonacci
INSERT INTO test_cases (id, problem_id, input, expected_output, is_sample, points, display_order)
VALUES 
(11, 3, '0', '0', true, 20, 1),
(12, 3, '1', '1', true, 20, 2),
(13, 3, '10', '55', true, 20, 3),
(14, 3, '15', '610', false, 20, 4),
(15, 3, '20', '6765', false, 20, 5);

-- Reset sequences (for PostgreSQL)
SELECT setval('contests_id_seq', (SELECT MAX(id) FROM contests));
SELECT setval('problems_id_seq', (SELECT MAX(id) FROM problems));
SELECT setval('test_cases_id_seq', (SELECT MAX(id) FROM test_cases));

