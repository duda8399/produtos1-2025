package edu.ifmg.produtos.repositories;

import edu.ifmg.produtos.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {

    // jpql -> BD Orientado a objetos
    @Query( "SELECT obj FROM PasswordRecover obj WHERE (obj.token = :token) AND (obj.expiration > :now())" )
    public List<PasswordRecover> searchValidToken (String token, Instant now);

}