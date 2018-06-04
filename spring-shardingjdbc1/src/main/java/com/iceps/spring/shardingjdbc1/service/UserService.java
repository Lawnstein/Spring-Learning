package com.iceps.spring.shardingjdbc1.service;

import java.util.List;

import com.iceps.spring.shardingjdbc1.model.User;

public interface UserService {

    int addUser(User user);
    
    User selUser(Integer userId);

    List<User> findAll(int pageNum, int pageSize);
    
}
