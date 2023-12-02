package model.dao;

import entities.Seller;

import java.util.List;

public interface SellerDao {

    void insert(Seller department);
    void update(Seller department);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
}
