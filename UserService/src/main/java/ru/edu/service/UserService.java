package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.dao.entity.UserInfo;
import ru.edu.dao.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserInfo findById(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found, id = " + id));
    }

    public UserInfo findByName(String name){
        return repository.findByName(name).orElseThrow(() -> new RuntimeException("User not found, name = " + name));
    }
    public UserInfo findByNameAndPhone(String name, String phone){
        return repository.findByNameAndPhone(name, phone).orElseThrow(() -> new RuntimeException("User not found, name = " + name + ", phone = " + phone));
    }

    public List<UserInfo> findAll(){
        return repository.findAll();
    }

    public UserInfo save(UserInfo user){
        return repository.save(user);
    }

    public UserInfo updateUser(UserInfo user){
        findById(user.getId());
        return repository.save(user);
    }

    public void deleteUser(Long id){
        UserInfo user = findById(id);
        repository.delete(user);
    }
}
