package com.app.fileManager.repository;

import com.app.fileManager.domain.Document;
import com.app.fileManager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findUserByFirstName(String name);
    public boolean existsByEmail(String email);
    public boolean existsByLogin(String login);
}