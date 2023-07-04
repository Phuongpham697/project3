package jmaster.io.project3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jmaster.io.project3.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
