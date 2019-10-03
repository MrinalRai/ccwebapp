package com.neu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.neu.exceptionHandler.UserNotFoundException;
import com.neu.exceptionHandler.UserServiceValidationException;
import com.neu.model.User;
import com.neu.repository.UserRepository;

@Component
public class UserService{

	@Autowired
	private UserRepository userRepo;
	
	
	public User add(User user) {
		User u = new User();
		List<User> userList = new ArrayList<>();
		u.setFirstname(user.getFirstname());
		u.setLastname(user.getLastname());
		for(User ul: userList) {
			if(ul.equals(user.getEmail())) {
				throw new UserNotFoundException("This user is already registered");
			}else {
				u.setEmail(user.getEmail());
			}
		}
		
		String pw = user.getPassword();
		String salt = BCrypt.gensalt();
		pw = BCrypt.hashpw(pw, salt);
		u.setPassword(pw);		
		//u.setPassword(user.getPassword());
		return userRepo.save(u);
	}
		

	public String update(User user, String email) throws UserServiceValidationException {
		
		if(!(user.getEmail().equals(email))) {
			throw new UserServiceValidationException("You can not change the email id");
		}
		String returning = null;
		List<User> ul = (List<User>) userRepo.findAll();
		for(User u: ul) {
			if (!(u.getEmail().equals(email))) {
				returning= "notFound";
				throw new UserNotFoundException("This email id is not registered for any user");				
			}
			else {
			u.setFirstname(user.getFirstname());
			u.setLastname(user.getLastname());
			//String userEmail = user.getEmail();
			u.setEmail(email);
			
			String pw = user.getPassword();
			String salt = BCrypt.gensalt();
			pw = BCrypt.hashpw(pw, salt);
			u.setPassword(pw);	
			userRepo.save(u);
			returning= "updated";
			break;
			}
		} 
		return returning; 
		
	}
	
	public Iterable<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	public User getUserByEmail(String email) {
		User u = userRepo.findByEmail(email);
		if(u.equals(null)) {
			throw new UserNotFoundException("This email id is not registered for any user");			
		}
		return u;
	}
	
//	public User loadUserByUsername(String email) {
//		List<User> userList = new ArrayList<>();
//		
//		for(User ul: userList) {
//			if(!((ul.getEmail()).equals(email))) {
//				throw new UserNotFoundException("User not found");
//			}else {
//				return ul;
//			}
//		}
//	}

}
