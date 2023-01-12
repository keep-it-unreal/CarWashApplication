package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.entity.UserInfo;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CityRepository;
import ru.edu.repository.UserInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository repository;

    public List<UserInfo> findAll() {
        return repository.findAll();
    }

    public UserInfo findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("UserInfo not found, id = " + id));
    }

    public UserInfo save(UserInfo userInfo) {
        return repository.save(userInfo);
    }

    public UserInfo update(UserInfo userInfo) {
        findById(userInfo.getId());
        return repository.save(userInfo);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public UserInfo findByNameAndPhone(UserInfo userInfo) {
        return repository.findByNameAndPhone(userInfo.getName(), userInfo.getPhone()).orElseThrow(() -> new ItemNotFoundException("UserInfo not found, name = " + userInfo.getName() + ", phone = " + userInfo.getPhone()));
    }
}
