package ru.edu.dao.repository;

import ru.edu.dao.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByNameAndPhone(String name, String phone);
}
