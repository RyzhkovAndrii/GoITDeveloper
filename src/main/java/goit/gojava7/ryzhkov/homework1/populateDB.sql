--
-- Dumping data for table `skills`
--
INSERT INTO `skills` VALUES (1,'Java'),(2,'JavaScript'),(3,'C++'),
							(4,'C#'),(5,'Python'),(6,'PHP'),(7,'Delphi');

--
-- Dumping data for table `developers`
--
INSERT INTO `developers` VALUES (1,'Stephen','Wozniak'),(2,'Mark','Zuckerberg'),
								(3,'Linus','Torvalds'),(4,'Paul','Graham'),
								(5,'Paul','Allen'),(6,'Alan','Cox'),
								(7,'Joshua','Bloch'),(8,'Rob','Pike'),
								(9,'Henry','Spencer');
--
-- Dumping data for table `projects`
--
INSERT INTO `projects` VALUES (1,'AmazonWebSite'),(2,'AliExpressWebSite'),
							  (3,'WindowsUpdate'),(4,'WindowsInstall'),
							  (5,'JavaDocs');          
                              
--
-- Dumping data for table `companies`
--
INSERT INTO `companies` VALUES (1,'Microsoft'),(2,'Google'),(3,'Oracle');

--
-- Dumping data for table `customers`
--
INSERT INTO `customers` VALUES (1,'Amazon'),(2,'AliExpress');

--
-- Dumping data for table `developers_skills`
--
INSERT INTO `developers_skills` VALUES (1,1),(1,2),(2,3),(2,4),(3,5),(4,1),(5,2),
									   (5,6),(6,1),(7,1),(8,7),(9,1),(9,5);
                                       
--
-- Dumping data for table `projects_developers`
--
INSERT INTO `projects_developers` VALUES (1,1),(1,3),(1,5),(2,1),(2,7),(3,2),
										 (3,4),(3,9),(4,2),(4,4),(4,6),(5,8);
                                       
--
-- Dumping data for table `companies_projects`
--
INSERT INTO `companies_projects` VALUES (2,1),(2,2),(1,3),(1,4),(3,5);


--
-- Dumping data for table `customers_projects`
--
INSERT INTO `customers_projects` VALUES (1,1),(2,2),(1,3),(2,4),(2,5);