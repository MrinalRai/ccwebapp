package com.neu.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neu.model.User;
import com.neu.repository.UserRepository;
import com.neu.service.UserService;

@RestController
@RequestMapping(path="/v1/user")
public class UserController {
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	@GetMapping("/self")
    public User getUser(Authentication authentication) {

		logger.info("Inside get: /user/self mapping");
        return userRepo.findUserByEmail(authentication.getName()).get();
    }	
	
	@PostMapping
	public ResponseEntity<Object> addUsers(@RequestBody User user) throws Exception {

		logger.info("Inside post: /user mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		ResponseEntity<Object> responseEntity = null;
		try {

			if (validateEmail(user.getEmail()) && validatePassword(user.getPassword())) {
				if (user.getId() == null && user.getCreateDate() == null && user.getModifyDate() == null) {
					User ent = userService.add(user);
					entities.put("User detail:", ent);
					responseEntity = new ResponseEntity<>(entities, HttpStatus.CREATED);
				}
			} else {
				entities.put("Invalid Format",
						"Please input correct format for email id and/or a Password with atleast 8 chars including 1 number and a special char");
				responseEntity = new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			entities.put("Message: ", e.getMessage());
			responseEntity = new ResponseEntity<>(entities, HttpStatus.FORBIDDEN);

		}
		return responseEntity;

	}
	
	@PutMapping("/self")
    public ResponseEntity<Object> updateUser(@RequestBody User user, Authentication auth) throws Exception {

		logger.info("Inside put: /user/self mapping");
        return userService.update(user, auth);
    }
	
	public Boolean validatePassword(String password) {
		if (password != null && (!password.equalsIgnoreCase(""))) {
			String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
			return (password.matches(pattern));
		} else {
			return Boolean.FALSE;
		}

	}

	public Boolean validateEmail(String email) {
		if (email != null && (!email.equalsIgnoreCase(""))) {
			String emailvalidator = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

			return email.matches(emailvalidator);
		} else {
			return Boolean.FALSE;
		}

	}

}
