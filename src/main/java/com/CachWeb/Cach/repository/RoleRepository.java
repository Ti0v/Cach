package com.CachWeb.Cach.repository;

import com.CachWeb.Cach.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}