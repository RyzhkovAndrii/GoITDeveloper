package goit.gojava7.ryzhkov.homework2.dao.impl.mysql;

import goit.gojava7.ryzhkov.homework2.dao.SkillDao;
import goit.gojava7.ryzhkov.homework2.model.Developer;
import goit.gojava7.ryzhkov.homework2.model.Skill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class MySqlSkillDaoImpl extends MySqlAbstractDAO<Skill, Integer> implements SkillDao {

    private static final String SQL_SAVE = "INSERT INTO skills(skill_name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE skills SET skill_name = ? WHERE skill_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM skills";
    private static final String SQL_GET_BY_ID = "SELECT * FROM skills WHERE skill_id = ?";
    private static final String SQL_GET_BY_ID_RANGE = "SELECT * FROM skills WHERE skill_id IN ";
    private static final String SQL_GET_BY_DEVELOPER_ID =
            "SELECT * FROM skills JOIN developers_skills USING (skill_id) WHERE developer_id = ";
    private static final String SQL_REMOVE_BY_ID = "DELETE FROM skills WHERE skill_id = ?";

    public MySqlSkillDaoImpl() {
    }

    public Collection<Skill> getByDeveloper(Developer developer) throws SQLException {
        return getAllWithOutTransaction(SQL_GET_BY_DEVELOPER_ID + developer.getId());
    }

    @Override
    public Integer save(Skill skill) throws SQLException {
        return save(skill, SQL_SAVE);
    }

    @Override
    public Skill getById(Integer id) throws SQLException {
        return getById(id, SQL_GET_BY_ID);
    }

    @Override
    public Collection<Skill> getByIds(Collection<Integer> idRange) throws SQLException {
        return getByIdRange(idRange, SQL_GET_BY_ID_RANGE);
    }

    @Override
    public Collection<Skill> getAll() throws SQLException {
        return getAll(SQL_GET_ALL);
    }

    @Override
    public void update(Skill skill) throws SQLException {
        update(skill, SQL_UPDATE);
    }

    @Override
    public void remove(Skill skill) throws SQLException {
        removeById(skill.getId(), SQL_REMOVE_BY_ID);
    }


    @Override
    protected void saveId(Integer id, Skill skill) {
        skill.setId(id);
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
        return rs.getInt(1); // todo transfer to abstract method
    }

    @Override
    protected void getLinks(Skill skill) throws SQLException {
        // doesn't have links
    }

    @Override
    protected void saveLinks(Skill skill) throws SQLException {
        // doesn't have links
    }

    @Override
    protected void removeLinks(Skill skill) throws SQLException {
        // doesn't have links
    }

    @Override
    protected int getLinksCount(Skill entity) throws SQLException {
        // doesn't have links
        return 0;
    }

}
