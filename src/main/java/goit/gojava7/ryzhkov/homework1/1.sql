#ALTER TABLE developers DROP COLUMN developer_salary;
ALTER TABLE developers ADD COLUMN developer_salary DECIMAL(9,2) DEFAULT 0.00;
UPDATE developers
SET developer_salary =
CASE
	WHEN developer_id = 1 THEN 1500.00
    WHEN developer_id = 2 THEN 400.00
    WHEN developer_id = 3 THEN 1000.00
    WHEN developer_id = 4 THEN 2500.00
    WHEN developer_id = 5 THEN 750.00
    WHEN developer_id = 6 THEN 900.00
    WHEN developer_id = 7 THEN 1500.00
    WHEN developer_id = 8 THEN 400.00
    WHEN developer_id = 9 THEN 1000.00
END
WHERE developer_id BETWEEN 1 AND 9;
