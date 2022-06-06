package com.app.fileManager.repository;

import com.app.fileManager.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    public Optional<Role> getByName(String name);
    public boolean existsByName(String name);
}
