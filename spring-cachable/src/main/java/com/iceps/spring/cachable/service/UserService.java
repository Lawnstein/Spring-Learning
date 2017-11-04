package com.iceps.spring.cachable.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "users",keyGenerator = "userKeyGenerator")
public class UserService {

    @Cacheable(key = "#id")
	public User getUser(Long id) {
		User u = new User();
		u.setId(id);
		u.setName("ID-" + id);
		if (id > 100) {
			u.setAge(18);
		} else {
			u.setAge(24);
		}
		System.out.println("create new User : " + u);
		return u;
	}


    //@Cacheable(keyGenerator = "userKeyGenerator")
    @Cacheable
    public User getByUserEntity(User user) {
		User u = new User();
		u.setId(user.getId());
		u.setName(user.getName());
		if (u.getId() > 100) {
			u.setAge(18);
		} else {
			u.setAge(24);
		}
		System.out.println("create new User : " + u + " by User " + user);
		return u;
    }
}
