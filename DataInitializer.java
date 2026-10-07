package com.ecommerce.config;

import com.ecommerce.model.entity.*;
import com.ecommerce.model.enums.UserStatus;
import com.ecommerce.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(TenantRepository tenantRepository,
                           RoleRepository roleRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Roles + permissions
        if (!roleRepository.existsByName(Role.ADMIN)) {
            Role admin = roleRepository.save(Role.builder().name(Role.ADMIN).build());
            Role vendor = roleRepository.save(Role.builder().name(Role.VENDOR).build());
            Role customer = roleRepository.save(Role.builder().name(Role.CUSTOMER).build());
            Role superAdmin = roleRepository.save(Role.builder().name(Role.SUPER_ADMIN).build());

            // Tenants
            Tenant t1 = tenantRepository.save(Tenant.builder()
                    .tenantId("tenant1").name("Tenant One").active(true).build());
            tenantRepository.save(Tenant.builder()
                    .tenantId("tenant2").name("Tenant Two").active(true).build());
            tenantRepository.save(Tenant.builder()
                    .tenantId("public").name("Public").active(true).build());

            String pwd = passwordEncoder.encode("SecurePass123!");
            User admin1 = userRepository.save(User.builder()
                    .email("admin@tenant1.com").tenantId(t1.getTenantId()).password(pwd)
                    .firstName("Tenant").lastName("Admin")
                    .status(UserStatus.ACTIVE).roles(Collections.singleton(admin)).build());
            userRepository.save(User.builder()
                    .email("vendor@tenant1.com").tenantId(t1.getTenantId()).password(pwd)
                    .firstName("Vera").lastName("Vendor")
                    .status(UserStatus.ACTIVE).roles(Collections.singleton(vendor)).build());
            userRepository.save(User.builder()
                    .email("customer@tenant1.com").tenantId(t1.getTenantId()).password(pwd)
                    .firstName("Carl").lastName("Customer")
                    .status(UserStatus.ACTIVE).roles(Collections.singleton(customer)).build());
            userRepository.save(User.builder()
                    .email("superadmin@system.com").tenantId("system").password(pwd)
                    .firstName("Super").lastName("Admin")
                    .status(UserStatus.ACTIVE).roles(Collections.singleton(superAdmin)).build());

            productRepository.save(Product.builder().name("Wireless Mouse")
                    .description("Ergonomic 2.4GHz mouse").price(new BigDecimal("29.99"))
                    .stock(100).tenantId(t1.getTenantId()).vendor(admin1).build());
            productRepository.save(Product.builder().name("USB-C Hub")
                    .description("7-in-1 hub").price(new BigDecimal("49.99"))
                    .stock(50).tenantId(t1.getTenantId()).vendor(admin1).build());

            log.info("Seeded tenants, roles, demo users (password: SecurePass123!) and products");
        }
    }
}
