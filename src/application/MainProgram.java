package application;

import entities.Department;

import java.sql.Connection;

public class MainProgram {
    public static void main(String[] args) {

        Connection connection = null;

        Department department = new Department(1, "Books");
        System.out.println(department);


    }
}
