SELECT pr.project_name, AVG(dev.developer_salary) avg_dev_salary
FROM projects_developers
JOIN projects pr USING (project_id)
JOIN developers dev USING (developer_id)
JOIN (
	SELECT pr.project_id
	FROM projects pr
	JOIN (
		SELECT pd.project_id, SUM(dev.developer_salary) pr_exp
		FROM projects_developers pd
		JOIN developers dev USING (developer_id)
		GROUP BY pd.project_id
	) pe USING (project_id)
	WHERE (pr.project_cost - pe.pr_exp) = (    
		SELECT MIN(pr.project_cost - pe.pr_exp) 
		FROM projects pr
		JOIN (
			SELECT pd.project_id, SUM(dev.developer_salary) pr_exp
			FROM projects_developers pd
			JOIN developers dev USING (developer_id)
			GROUP BY pd.project_id
		) pe USING (project_id)
	) 
) pe USING (project_id)
GROUP BY project_id;
