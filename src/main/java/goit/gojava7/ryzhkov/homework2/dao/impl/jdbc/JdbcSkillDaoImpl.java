package goit.gojava7.ryzhkov.homework2.dao.impl.jdbc;

import goit.gojava7.ryzhkov.homework2.dao.StorageException;
import goit.gojava7.ryzhkov.homework2.dao.interfaces.SkillDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class JdbcSkillDaoImpl extends JdbcAbstractDao<Skill, Integer> implements SkillDao {

    private static final String SQL_SAVE = "INSERT INTO skills(skill_name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE skills SET skill_name = ? WHERE skill_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM skills";
    private static final String SQL_GET_BY_ID = "SELECT * FROM skills WHERE skill_id = ?";
    private static final String SQL_GET_BY_ID_RANGE = "SELECT * FROM skills WHERE skill_id IN ";
    private static final String SQL_GET_BY_DEVELOPER_ID =
            "SELECT * FROM skills JOIN developers_skills USING (skill_id) WHERE developer_id = ";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM skills WHERE skill_id = ?";

    public JdbcSkillDaoImpl() {
    }

    Collection<Skill> getByDeveloper(Developer developer) throws SQLException {
        return getAllWithOutCommit(SQL_GET_BY_DEVELOPER_ID + developer.getId());
    }

    @Override
    public Integer save(Skill skill) throws StorageException {
        return save(skill, SQL_SAVE);
    }

    @Override
    public Skill getById(Integer id) throws StorageException {
        return getById(id, SQL_GET_BY_ID);
    }

    @Override
    public Collection<Skill> getByIdRange(Collection<Integer> idRange) throws StorageException {
        return getByIdRange(idRange, SQL_GET_BY_ID_RANGE);
    }

    @Override
    public Collection<Skill> getAll() throws StorageException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Skill skill) throws StorageException {
        update(skill.getId(), skill, SQL_UPDATE);
    }

    @Override
    public void remove(Skill skill) throws StorageException {
        removeById(skill.getId(), SQL_REMOVE_BY_ID);
    }

    @Override
    protected Skill readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("skill_id");
        String name = rs.getString("skill_name");
        return new Skill(id, name);
    }

    @Override
    protected void prepareToSave(Skill skill, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, skill.getName());
    }

    @Override
    protected void prepareToUpdate(Skill skill, PreparedStatement pstmt) throws SQLException {
        prepareToSave(skill, pstmt);
        pstmt.setInt(2, skill.getId());
    }

    @Override
    protected Integer readIdFromKeyResultSet(ResultSet rs) throws SQLException {
        return rs.getInt(1);
    }

    @Override
    protected void enrichWithLinks(Skill skill) throws SQLException {
        // doesn't have links
    }

    @Override
    protected void saveLinksInDb(Integer id, Skill skill) throws SQLException {
        // doesn't have links
    }

    @Override
    protected void removeLinksFromDb(Integer id) throws SQLException {
        // doesn't have links
    }

}
