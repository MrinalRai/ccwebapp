package com.neu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.neu.controller.UserController;
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

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping(path = "/user/self", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listUser() {
		
		//System.out.println("-----------statsDClient.toString()--------------");
		
		logger.info("--Inside root mapping--");
		logger.warn("---This is a Warn Message");
		logger.error("This is an error message");
		//statsDClient.incrementCounter("endpoint.login.http.get");
		HashMap<String, Object> entities = new HashMap();
		entities.put("Status", "Authenticated");
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		entities.put("Time", formatter.format(new Date()));

		return ResponseEntity.ok(entities);
	}
//	
//	@GetMapping(path="/user")
//	public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers(){
//		List<User> users;
//		try {
//			users = (List<User>) userService.getAllUsers();
//		}catch (UserNotFoundException ux) {
//			return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
//		}
//		 return new ResponseEntity<>(users, HttpStatus.OK);
//	}
	

	
	@GetMapping(path="/user/self",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getUserByEmail(@PathVariable String email) throws UserServiceException{
		User user;
		HashMap<String, Object> entities = new HashMap();
		try {
			user = userService.getUserByEmail(email);
		}catch (UserNotFoundException unx) {
			entities.put("Message: ", unx.getMessage());
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}
		 entities.put("User", user);
		 return new ResponseEntity<>(user, HttpStatus.OK);
		//return userService.getUserByEmail(email);
	}
			
//	@PostMapping(path="/user")	
//	public @ResponseBody String addUsers(@RequestBody User user) {
//		logger.info("Inside post V1/user mapping");
//		
//		userService.add(user);
//		return "Saved";
//	}
	
	@PostMapping("/user")
	public ResponseEntity<Object> addUsers(@Valid @RequestBody User user) {
		
		System.out.println("Inside post /user mapping");		
		logger.info("Inside post /user mapping");

		HashMap<String, Object> entities = new HashMap();
		User ent = null;
		if (validateEmail(user.getEmail()) && validatePassword(user.getPassword())) {
			if(null == userService.getUserByEmail(user.getEmail())) {
				ent = userService.add(user);
				//ent = userDaoService.save(user);
				//byte[] enc = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				entities.put("Message:", "User is saved");
				return new ResponseEntity<>(entities,HttpStatus.CREATED);
			} else {
				entities.put("Message", "User already exists !!");
				return new ResponseEntity<>(entities, HttpStatus.FORBIDDEN);
			}
		} else {
			entities.put("Invalid Format","Please input correct format for email id and/or a Password with atleast 8 chars including 1 number and a special char");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(path="/user/self")
	public ResponseEntity<Object> updateUsers(@RequestBody User user, @PathVariable String email) throws UserServiceValidationException {
		
		System.out.println("Inside put /user/self mapping");		
		logger.info("Inside put /user/self mapping");
		
		HashMap<String, Object> entities = new HashMap();
		User ent = null;
		
		try{
			if(userService.update(user, email).equalsIgnoreCase("notFound"))
			{
				entities.put("Message:", "User is not found");
				return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
				}else if (validatePassword(user.getPassword())) {
					entities.put("Message:", "User details updated");
					return new ResponseEntity<>(entities,HttpStatus.ACCEPTED);
				}else {
					entities.put("Invalid Format:","Please input correct format for email id and/or a Password with atleast 8 chars including 1 number and a special char");
					return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
					
				}
				
		}catch(UserServiceValidationException usx) {
			entities.put("Message", usx.getMessage());
			return new ResponseEntity<>(entities, HttpStatus.FORBIDDEN);
		}		
	}
//	@PutMapping(path="/user/self")
//	public @ResponseBody String updateUsers(@RequestBody User user, @PathVariable String email) throws UserServiceValidationException {
//		try{
//			if(userService.update(user, email).equalsIgnoreCase("notFound"))
//			{
//				return "User not found";
//				}else return "Updated";
//				
//		}catch(UserServiceValidationException usx) {
//			return "You can not change the email id";
//		}		
//	}
	
	public Boolean validatePassword(String password) {
		if (password != null && (!password.equalsIgnoreCase(""))) {
			String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
			return (password.matches(pattern));
		} else {
			return Boolean.FALSE;
		}

	}

	public Boolean validateEmail(String email) {
		if (email != null && (!email.equalsIgnoreCase(""))) {
			String emailvalidator = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
					+ "A-Z]{2,7}$";

			return email.matches(emailvalidator);
		} else {
			return Boolean.FALSE;
		}

	}

}
