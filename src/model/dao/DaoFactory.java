package model.dao;

import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //classe responsável por instanciar novos Dao

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC();
    }
}
