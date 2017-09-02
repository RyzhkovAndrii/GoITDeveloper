SELECT SUM(salary) common_java_salary
FROM developers_skills 
NATURAL JOIN developers 
NATURAL JOIN skills 
WHERE skill_name = 'Java';
