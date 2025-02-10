package com.terminal.todo_application.repository;

import com.terminal.todo_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameStartingWith(String username);
}
