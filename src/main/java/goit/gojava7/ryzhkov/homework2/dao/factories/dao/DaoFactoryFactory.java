package goit.gojava7.ryzhkov.homework2.dao.factories.dao;

public class DaoFactoryFactory {

    private static DaoFactory daoFactory;

    public static DaoFactory getDaoFactory() {
        if (daoFactory == null) {
            return new HibernateDaoFactory();
        }
        return daoFactory;
    }

    public static void setDaoFactory(DaoFactory daoFactory) {
        DaoFactoryFactory.daoFactory = daoFactory;
    }

}
