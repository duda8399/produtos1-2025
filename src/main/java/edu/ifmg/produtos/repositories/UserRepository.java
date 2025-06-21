package edu.ifmg.produtos.repositories;

import edu.ifmg.produtos.entities.User;
import edu.ifmg.produtos.projections.UserDetailsProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Query Methods:
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);

    @Query(nativeQuery = true,
            value = """
            SELECT u.email as username, 
                u.password,
                r.id as roleId,
                r.authority
            FROM users u
            INNER JOIN users_to_roles ur ON u.id = ur.user_id
            INNER JOIN roles r ON r.id = ur.role_id
            WHERE u.email = :email  
        """
    )
    List<UserDetailsProjection> searchUserAndRoleByEmail(String email);
}