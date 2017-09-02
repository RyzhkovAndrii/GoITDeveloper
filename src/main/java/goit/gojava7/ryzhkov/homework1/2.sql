SELECT pr.project_name, SUM(dev.developer_salary) project_expenses
FROM projects_developers pd
NATURAL JOIN developers dev
NATURAL JOIN projects pr
GROUP BY pd.project_id
HAVING project_expenses = (
	SELECT MAX(sel.project_expenses)
	FROM (
		SELECT SUM(dev.developer_salary) project_expenses
		FROM projects_developers pd
		NATURAL JOIN developers dev 
		GROUP BY pd.project_id
	) sel
)