SELECT SUM(dev.developer_salary) common_java_salary
FROM developers_skills 
NATURAL JOIN developers dev
NATURAL JOIN skills 
WHERE skill_name = 'Java';
