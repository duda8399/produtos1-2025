package edu.ifmg.produtos.repository;

import edu.ifmg.produtos.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}

