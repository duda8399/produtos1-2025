package edu.ifmg.produtos.repository;

import edu.ifmg.produtos.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

