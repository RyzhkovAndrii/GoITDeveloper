package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.DeveloperDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Project;
import goit.gojava7.ryzhkov.homework2.model.Skill;
import goit.gojava7.ryzhkov.homework2.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class MySqlDeveloperDaoImpl extends MySqlAbstractDAO<Developer, Integer> implements DeveloperDao {

    private static final String SQL_SAVE =
            "INSERT INTO developers(developer_first_name, developer_last_name, developer_salary) " +
                    "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE developers SET developer_first_name = ?," +
            " developer_last_name = ?, developer_salary = ? WHERE developer_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM developers";
    private static final String SQL_GET_BY_ID = "SELECT * FROM developers WHERE developer_id = ?";
    private static final String SQL_GET_BY_ID_RANGE = "SELECT * FROM developers WHERE developer_id IN ";
    private static final String SQL_GET_BY_PROJECT_ID =
                    "SELECT developer_id, developer_first_name, developer_last_name, developer_salary" +
                    " FROM projects_developers pd" +
                    " JOIN developers USING (developer_id)" +
                    " WHERE pd.project_id = ?";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM developers WHERE developer_id = ?";
    private static final String SQL_SAVE_LINKS_SKILLS =
            "INSERT INTO developers_skills(developer_id, skill_id) VALUES (?, ?)";
    private static final String SQL_GET_COUNT_LINKS_SKILLS =
            "SELECT COUNT(developer_id) FROM developers_skills WHERE developer_id = ?";
    private static final String SQL_DELETE_LINKS_SKILLS = "DELETE FROM developers_skills WHERE developer_id = ?";

    public MySqlDeveloperDaoImpl() {
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
    public Collection<Developer> getByIds(Collection<Integer> idRange) throws SQLException {
        return getByIdRange(idRange, SQL_GET_BY_ID_RANGE);
    }

    @Override
    public Collection<Developer> getAll() throws SQLException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Developer developer) throws SQLException {
        update(developer, SQL_UPDATE);
    }

    @Override
    public void remove(Developer developer) throws SQLException {
        removeById(developer.getId(), SQL_REMOVE_BY_ID);
    }

    @Override
    public Collection<Developer> getByProject(Project project) throws SQLException {
        return getAllWithOutTransaction(SQL_GET_BY_PROJECT_ID + project.getId());
    }

    @Override
    protected void saveId(Integer id, Developer developer) {
        developer.setId(id);
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
    protected void getLinks(Developer developer) throws SQLException {
        Collection<Skill> skills = new MySqlSkillDaoImpl().getByDeveloper(developer);
        developer.setSkills(skills);
    }

    @Override
    protected void saveLinks(Developer developer) throws SQLException {
        try (PreparedStatement pstmt =
                     ConnectionUtils.getConnection().prepareStatement(SQL_SAVE_LINKS_SKILLS)) {
            pstmt.setInt(1, developer.getId());
            for (Skill skill : developer.getSkills()) {
                pstmt.setInt(2, skill.getId());
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Saving links failed.");
                }
            }
        }
    }

    @Override
    protected void removeLinks(Developer developer) throws SQLException {
        int skillsCount = getLinksCount(developer);
        Connection connection = ConnectionUtils.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(SQL_DELETE_LINKS_SKILLS)) {
            pstmt.setInt(1, developer.getId());
            if (pstmt.executeUpdate() != skillsCount) {
                throw new SQLException("Deleting links failed.");
            }
        }
    }

    @Override
    protected int getLinksCount(Developer developer) throws SQLException {
        try (PreparedStatement pstmt =
                     ConnectionUtils.getConnection().prepareStatement(SQL_GET_COUNT_LINKS_SKILLS)) {
            pstmt.setInt(1, developer.getId());
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
}