package com.neu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.neu.exceptionHandler.UserNotFoundException;
import com.neu.exceptionHandler.UserServiceException;
import com.neu.exceptionHandler.UserServiceValidationException;
import com.neu.model.User;
import com.neu.service.UserService;

@RestController
@RequestMapping(path="/v1")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(path="/user")
	public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers(){
		List<User> users;
		try {
			users = (List<User>) userService.getAllUsers();
		}catch (UserNotFoundException ux) {
			return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
		}
		 return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(path="/user/{email}")
	public @ResponseBody ResponseEntity<User> getUserByEmail(@PathVariable String email) throws UserServiceException{
		User user;
		try {
			user = userService.getUserByEmail(email);
		}catch (UserNotFoundException unx) {
			return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
		}
		 return new ResponseEntity<>(user, HttpStatus.OK);
		//return userService.getUserByEmail(email);
	}
			
	@PostMapping(path="/user")	
	public @ResponseBody String addUsers(@RequestBody User user) {
		userService.add(user);
		return "Saved";
	}
	
	@PutMapping(path="/user/{email}")
	public @ResponseBody String updateUsers(@RequestBody User user, @PathVariable String email) throws UserServiceValidationException {
		try{
			if(userService.update(user, email).equalsIgnoreCase("notFound"))
			{
				return "User not found";
				}else return "Updated";
				
		}catch(UserServiceValidationException usx) {
			return "You can not change the email id";
		}
		
			
		
	}

}
