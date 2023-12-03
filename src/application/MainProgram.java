package application;

import entities.Department;
import entities.Seller;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainProgram {

    //steps
    //1-criar a base para conexão com banco de dados (properties, classes)
    //2-criar as entities (objetos que irão representar os resultados obtidos do banco)
    //3-criar as interfaces com os métodos de acesso ao banco de dados
    //4-criar as classes que irão implementar  os métodos das interfaces

    public static void main(String[] args) {

        System.out.println("---TEST Seller by ID 2---");
        SellerDao sellerDao = DaoFactory.createSellerDao(); //o programa não conhece a implementação, somente a interface. É tbm uma forma de se fazer injeção de dep sem explicitar a implementação
        System.out.println(sellerDao.findById(2));
        System.out.println("");

        System.out.println("---TEST Department by ID 1---");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        System.out.println(departmentDao.findById(1));

        System.out.println("");
        System.out.println("---TEST Seller by Department 2---");

        List<Seller> sellerList = sellerDao.findByDepartment(departmentDao.findById(2));
        sellerList.forEach(System.out::println);


    }
}
