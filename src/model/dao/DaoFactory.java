package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //classe responsável por instanciar novos Dao

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection()); //passando o getConnection para o SellerDaoJDBC
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
