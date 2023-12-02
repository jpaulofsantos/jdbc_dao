package application;

import entities.Department;
import entities.Seller;

import java.sql.Connection;
import java.util.Date;

public class MainProgram {
    public static void main(String[] args) {

        Connection connection = null;

        Department department = new Department(1, "Books");
        System.out.println(department);

        Seller seller = new Seller(11, "Bob", "bob@gmail.com", new Date(), 2500.00, department);
        System.out.println(seller);


    }
}
