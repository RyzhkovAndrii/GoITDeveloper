SELECT SUM(dev.developer_salary) common_java_salary
FROM developers_skills 
JOIN developers dev USING (developer_id)
JOIN skills USING (skill_id)
WHERE skill_name = 'Java';
