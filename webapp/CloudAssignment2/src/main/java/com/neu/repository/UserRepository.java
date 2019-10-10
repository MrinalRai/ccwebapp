package com.neu.repository;

import org.springframework.data.repository.CrudRepository;

import com.neu.model.User;

public interface UserRepository extends CrudRepository<User, String>{
	
	User findByEmail(String email);
}
