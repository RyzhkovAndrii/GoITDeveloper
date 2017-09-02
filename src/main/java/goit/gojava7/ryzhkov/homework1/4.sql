#ALTER TABLE projects DROP COLUMN project_cost;
ALTER TABLE projects ADD COLUMN project_cost DECIMAL(11,2) DEFAULT 0.00;
UPDATE projects
SET cost =
CASE
	WHEN project_id = 1 THEN 15000.00
    WHEN project_id = 2 THEN 12000.00
    WHEN project_id = 3 THEN 7550.00
    WHEN project_id = 4 THEN 8000.00
    WHEN project_id = 5 THEN 7000.00
END
WHERE project_id BETWEEN 1 AND 5;
