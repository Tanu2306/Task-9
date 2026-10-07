package com.ecommerce.service;

import com.ecommerce.model.entity.Tenant;
import java.util.List;
import java.util.Optional;

public interface TenantService {
    List<Tenant> getAllTenants();
    Optional<Tenant> getTenantById(Long id);
    Optional<Tenant> getTenantByTenantId(String tenantId);
    Tenant createTenant(Tenant tenant);
    Tenant updateTenant(Long id, Tenant tenant);
}
