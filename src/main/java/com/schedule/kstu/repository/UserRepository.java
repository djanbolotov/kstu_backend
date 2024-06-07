package com.schedule.kstu.repository;

import com.schedule.kstu.model.Role;
import com.schedule.kstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<List<User>> findAllByRole(Role role);
}
