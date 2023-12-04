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
    //1-criar a base para conexão com banco de dados (properties, classes DB)
    //2-criar as entities (objetos que irão representar os resultados obtidos do banco)
    //3-criar as interfaces com os métodos de acesso ao banco de dados e o DaoFactory que irá passar o Connection e instanciar as classes que implementam as interfaces
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

        System.out.println("");
        System.out.println("---TEST Seller All---");

        List<Seller> sellerListAll = sellerDao.findAll();
        sellerListAll.forEach(System.out::println);

        System.out.println("");
        System.out.println("---TEST Department All---");

        List<Department> departmentListAll = departmentDao.findAll();
        departmentListAll.forEach(System.out::println);

        System.out.println("");
        System.out.println("---TEST Seller Insert---");

        Seller seller = new Seller();
        seller.setName("JP3");
        seller.setEmail("jp3@teste.com.br");
        seller.setBirthDate(new Date());
        seller.setBaseSalary(3000.00);
        seller.setDepartment(departmentDao.findById(3));

        //sellerDao.insert(seller);
        //System.out.println("Insert ok! ID: " + seller.getId());

        System.out.println("");
        System.out.println("---TEST Seller Update---");

        seller = sellerDao.findById(20);
        seller.setName("Teste");
        sellerDao.update(seller);
        System.out.println("Update ok!");

        System.out.println("");
        System.out.println("---TEST Seller Delete---");

        sellerDao.deleteById(29);
    }
}
