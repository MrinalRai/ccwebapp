package com.neu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.neu.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	User findByEmail(String email);
}
