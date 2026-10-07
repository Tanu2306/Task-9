package com.ecommerce.service;

import com.ecommerce.model.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User getCurrentUser();
    List<User> getAllUsers();
    Optional<User> findByEmailAndTenantId(String email, String tenantId);
}
