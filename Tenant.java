package com.ecommerce.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    private boolean active = true;

    public Tenant() {}

    public Tenant(Long id, String tenantId, String name, boolean active) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static TenantBuilder builder() {
        return new TenantBuilder();
    }

    public static class TenantBuilder {
        private Long id;
        private String tenantId;
        private String name;
        private boolean active = true;

        public TenantBuilder id(Long id) { this.id = id; return this; }
        public TenantBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public TenantBuilder name(String name) { this.name = name; return this; }
        public TenantBuilder active(boolean active) { this.active = active; return this; }
        public Tenant build() { return new Tenant(id, tenantId, name, active); }
    }
}
