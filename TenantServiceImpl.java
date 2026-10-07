package com.ecommerce.service.impl;

import com.ecommerce.model.entity.Tenant;
import com.ecommerce.repository.TenantRepository;
import com.ecommerce.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public Optional<Tenant> getTenantById(Long id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Optional<Tenant> getTenantByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId);
    }

    @Override
    public Tenant createTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant updateTenant(Long id, Tenant tenant) {
        return tenantRepository.findById(id).map(existing -> {
            existing.setName(tenant.getName());
            existing.setActive(tenant.isActive());
            return tenantRepository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
    }
}
