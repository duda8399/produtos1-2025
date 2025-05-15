package edu.ifmg.produtos.repository;

import edu.ifmg.produtos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Role, Long> {
    
}

