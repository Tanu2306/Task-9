package com.ecommerce.repository;

import com.ecommerce.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndTenantId(String email, String tenantId);
    Optional<User> findByEmail(String email);
    boolean existsByEmailAndTenantId(String email, String tenantId);
}
