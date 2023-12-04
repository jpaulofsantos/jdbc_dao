package model.dao.impl;

import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;
import model.dao.SellerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) { //criando uma dependência com a conexão com o banco de dados
        this.connection = connection;
    }
    @Override
    public void insert(Seller seller) {

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows>0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
                DB.closeResulSet(resultSet);
            } else {
                throw new DbException("Erro inesperado");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?");

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE from seller WHERE Id = ?");
            preparedStatement.setInt(1, id);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new DbException("Nenhum registro deletado");
            }


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(//sql que busca um vendedor no banco de dados através do ID
                    "SELECT seller .*, department.Name as DepName, department.Id as DepId "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?");

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
            DB.closeResulSet(resultSet);
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller .*, department.Name as DepName, department.Id as DepId "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Id ");

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>(); //map para não repetir o departamento, ou seja: os vendedores estarão apontando para o mesmo objeto department, e não serão criados departments repetidos.

            while (resultSet.next()) {

                Department dep = departmentMap.get(resultSet.getInt("DepartmentId")); //verifica se já existe o valor no map, passando o Id da coluna informada

                if (dep == null) { //se não existir
                    dep = instantiateDepartment(resultSet); //cria um novo departamento
                    departmentMap.put(resultSet.getInt("DepartmentId"), dep); //add no map
                }

                Seller seller = instantianteSeller(resultSet, dep);
                sellerList.add(seller);
            }
            return sellerList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResulSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller .*, department.Name as DepName, department.Id as DepId "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE department.Id = ? "
                            + "ORDER BY Name ");

            preparedStatement.setInt(1, department.getId());

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>(); //map para não repetir o departamento, ou seja: os vendedores estarão apontando para o mesmo objeto department, e não serão criados departments repetidos.

            while (resultSet.next()) {

                    Department dep = departmentMap.get(resultSet.getInt("DepartmentId")); //verifica se já existe o valor no map, passando o Id da coluna informada

                    if (dep == null) { //se não existir
                        dep = instantiateDepartment(resultSet); //cria um novo departamento
                        departmentMap.put(resultSet.getInt("DepartmentId"), dep); //add no map
                    }

                    Seller seller = instantianteSeller(resultSet, dep);
                    sellerList.add(seller);
            }
            return sellerList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResulSet(resultSet);
        }
    }
}