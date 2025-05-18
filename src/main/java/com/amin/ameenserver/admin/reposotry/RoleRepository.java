package com.amin.ameenserver.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    //Role findByRoleName(String name);
    //Set<Role> findByName(String name);
}
