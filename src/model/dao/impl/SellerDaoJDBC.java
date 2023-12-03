package model.dao.impl;

import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;
import model.dao.SellerDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) { //criando uma dependência com a conexão com o banco de dados
        this.connection = connection;
    }
    @Override
    public void insert(Seller department) {

    }

    @Override
    public void update(Seller department) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller .*, department.Name as DepName, department.Id as DepId "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?");  //sql que busca um vendedor no banco de dados através do ID

            preparedStatement.setInt(1, id); //id recebido da função, se refere ao ? da query acima

            resultSet = preparedStatement.executeQuery(); //traz os resultados no formato de tabela

            if (resultSet.next()) { //testando se veio algum resultado, pois o resultSet aponta para a posição 0 e o objeto está na posição 1

                Department department = instantiateDepartment(resultSet);

                return instantianteSeller(resultSet, department);

            }

            return null;

        } catch (SQLException e){
            throw new DbException(e.getMessage());

        } finally {
            DB.closeStatement(preparedStatement);
            DB.claseResulSet(resultSet);
        }
    }

    private Seller instantianteSeller(ResultSet resultSet, Department department) throws SQLException {

        Seller seller =  new Seller(); //instanciando um Seller e setando os valores a partir dos resultados do resultSet
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);

        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {

        Department department = new Department(); //instanciando um Department e setando os valores a partir dos resultados do resultSet
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));

        return department;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
