package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.entity.UserInfo;
import ru.edu.entity.enums.Roles;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findByName(String name);
    List<UserInfo> findByRole(Roles role);
    Optional<UserInfo> findByNameAndPhone(String name, String phone);
}
