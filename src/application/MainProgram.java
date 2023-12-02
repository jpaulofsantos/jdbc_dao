package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;

import java.sql.Connection;
import java.util.Date;

public class MainProgram {

    //steps
    //1-criar a base para conexão com banco de dados (properties, classes)
    //2-criar as entities (objetos que irão representar os resultados obtidos do banco)
    //3-criar as interfaces com os métodos de acesso ao banco de dados
    //4-criar as classes que irão implementar  os métodos das interfaces

    public static void main(String[] args) {

        Connection connection = null;

        SellerDao sellerDao = DaoFactory.createSellerDao(); //o programa não conhece a implementação, somente a interface. É tbm uma forma de se fazer injeção de dep sem explicitar a implementação
        System.out.println(sellerDao.findById(2));

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        System.out.println(departmentDao.findById(1));


    }
}
