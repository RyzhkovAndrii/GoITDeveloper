SELECT company_name, customer_name, project_profit
FROM (
	SELECT MIN(cus_pay.project_profit) min_profit
	FROM (
		SELECT company_id, customer_id, SUM(pr.project_cost - project_expenses) project_profit
		FROM companies_projects
		JOIN customers_projects USING (project_id)
		JOIN projects pr USING (project_id)
		JOIN (
			SELECT pd.project_id, SUM(dev.developer_salary) project_expenses
			FROM projects_developers pd
			JOIN developers dev USING (developer_id)
			GROUP BY pd.project_id
		) pc USING (project_id)
		GROUP BY company_id, customer_id
	) cus_pay
	GROUP BY cus_pay.company_id
) min_cus_pay JOIN (
	SELECT com.company_name, cus.customer_name, SUM(pr.project_cost - project_expenses) project_profit
	FROM companies_projects 
	JOIN customers_projects USING (project_id)
	JOIN projects pr USING (project_id)
	JOIN customers cus USING (customer_id) 
	JOIN companies com USING (company_id) 
	JOIN (
		SELECT pd.project_id, SUM(dev.developer_salary) project_expenses
		FROM projects_developers pd
		JOIN developers dev USING (developer_id)
		GROUP BY pd.project_id
	) pc USING (project_id)
	GROUP BY com.company_name, cus.customer_name
) cus_pay ON min_cus_pay.min_profit = cus_pay.project_profit;