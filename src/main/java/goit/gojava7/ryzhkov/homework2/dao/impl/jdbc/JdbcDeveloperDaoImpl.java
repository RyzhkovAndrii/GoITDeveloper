package goit.gojava7.ryzhkov.homework2.dao.impl.jdbc;

import goit.gojava7.ryzhkov.homework2.dao.interfaces.DeveloperDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class JdbcDeveloperDaoImpl extends JdbcAbstractDao<Developer, Integer> implements DeveloperDao {

    private static final String SQL_SAVE =
            "INSERT INTO developers(developer_first_name, developer_last_name, developer_salary) " +
                    "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE developers SET developer_first_name = ?," +
            " developer_last_name = ?, developer_salary = ? WHERE developer_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM developers";
    private static final String SQL_GET_BY_ID = "SELECT * FROM developers WHERE developer_id = ?";
    private static final String SQL_GET_BY_ID_RANGE = "SELECT * FROM developers WHERE developer_id IN ";
    private static final String SQL_GET_BY_PROJECT_ID =
                    "SELECT developer_id, developer_first_name, developer_last_name, developer_salary " +
                    "FROM projects_developers pd " +
                    "JOIN developers USING (developer_id) " +
                    "WHERE pd.project_id = ";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM developers WHERE developer_id = ?";
    private static final String SQL_SAVE_LINKS_SKILLS =
            "INSERT INTO developers_skills(developer_id, skill_id) VALUES (?, ?)";
    private static final String SQL_GET_COUNT_LINKS_SKILLS =
            "SELECT COUNT(developer_id) FROM developers_skills WHERE developer_id = ?";
    private static final String SQL_DELETE_LINKS_SKILLS = "DELETE FROM developers_skills WHERE developer_id = ?";

    public JdbcDeveloperDaoImpl() {
    }

    Collection<Developer> getByProject(Project project) throws SQLException {
        return getAllWithOutCommit(SQL_GET_BY_PROJECT_ID + project.getId());
    }

    @Override
    public Integer save(Developer developer) throws SQLException {
        return save(developer, SQL_SAVE);
    }

    @Override
    public Developer getById(Integer id) throws SQLException {
        return getById(id, SQL_GET_BY_ID);
    }

    @Override
    public Collection<Developer> getByIdRange(Collection<Integer> idRange) throws SQLException {
        return getByIdRange(idRange, SQL_GET_BY_ID_RANGE);
    }

    @Override
    public Collection<Developer> getAll() throws SQLException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Developer developer) throws SQLException {
        update(developer.getId(), developer, SQL_UPDATE);
    }

    @Override
    public void remove(Developer developer) throws SQLException {
        removeById(developer.getId(), SQL_REMOVE_BY_ID);
    }

    @Override
    protected Developer readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("developer_id");
        String firstName = rs.getString("developer_first_name");
        String lastName = rs.getString("developer_last_name");
        double salary = rs.getDouble("developer_salary");
        return new Developer(id, firstName, lastName, salary, null);
    }

    @Override
    protected void prepareToSave(Developer developer, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, developer.getFirstName());
        pstmt.setString(2, developer.getLastName());
        pstmt.setDouble(3, developer.getSalary());
    }

    @Override
    protected void prepareToUpdate(Developer developer, PreparedStatement pstmt) throws SQLException {
        prepareToSave(developer, pstmt);
        pstmt.setInt(4, developer.getId());
    }

    @Override
    protected Integer readIdFromKeyResultSet(ResultSet rs) throws SQLException {
        return rs.getInt(1); // todo transfer to abstract method
    }

    @Override
    protected void enrichWithLinks(Developer developer) throws SQLException {
        developer.setSkills(new JdbcSkillDaoImpl().getByDeveloper(developer));
    }

    @Override
    protected void saveLinksInDb(Integer id, Developer developer) throws SQLException {
        Collection skillsIds = developer.getSkills().stream()
                .map(Skill::getId)
                .collect(Collectors.toSet());
        saveLinksInDb(id, skillsIds, SQL_SAVE_LINKS_SKILLS);
    }

    @Override
    protected void removeLinksFromDb(Integer id) throws SQLException {
        removeLinksFromDb(id, SQL_GET_COUNT_LINKS_SKILLS, SQL_DELETE_LINKS_SKILLS);
    }

}