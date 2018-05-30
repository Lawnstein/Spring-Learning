package com.iceps.spring.shardingjdbc2.service;

import com.iceps.spring.shardingjdbc2.model.User;

import java.util.List;

public interface UserService {

    int addUser(User user);
    
    User selUser(Integer userId);

    List<User> findAll(int pageNum, int pageSize);
    
}
