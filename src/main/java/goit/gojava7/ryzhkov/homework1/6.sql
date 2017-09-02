SELECT pr.project_name, AVG(dev.salary) avg_dev_salary
FROM projects_developers
JOIN projects pr USING (project_id)
JOIN developers dev USING (developer_id)
JOIN (
	SELECT pr.project_id
	FROM projects pr
	JOIN (
		SELECT pd.project_id, SUM(dev.salary) pr_exp
		FROM projects_developers pd
		JOIN developers dev USING (developer_id)
		GROUP BY pd.project_id
	) pe USING (project_id)
	WHERE (pr.cost - pe.pr_exp) = (    
		SELECT MIN(pr.cost - pe.pr_exp) 
		FROM projects pr
		JOIN (
			SELECT pd.project_id, SUM(dev.salary) pr_exp
			FROM projects_developers pd
			JOIN developers dev USING (developer_id)
			GROUP BY pd.project_id
		) pe USING (project_id)
	) 
) pe USING (project_id)
GROUP BY project_id;
