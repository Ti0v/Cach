package com.CachWeb.Cach.config;

import com.CachWeb.Cach.entity.Role;
import com.CachWeb.Cach.repository.RoleRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBSeeder {

    private final RoleRepository roleRepository;

    public DBSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        initiate();
    }

    private void initiate() {
        if (roleRepository.count()==0) {
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("ROLE_USER");
            roleRepository.save(user);
        }
    }
}
