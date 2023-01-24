package com.carwash.telegram.repository;

import com.carwash.telegram.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotUserRepository extends JpaRepository<BotUser,String> {

    //BotUser findByName(String name);

}