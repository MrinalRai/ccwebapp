package com.neu.controller;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neu.exceptionHandler.RecipieValidationException;
import com.neu.model.Recipie;
import com.neu.service.RecipieService;

@RestController
@RequestMapping(path="/v1/recipie")
public class RecipieController {
	
	@Autowired
	private RecipieService recService;

	private final static Logger logger = LoggerFactory.getLogger(RecipieController.class);
	
	@PostMapping("/")
	public ResponseEntity<Object> addUsers(@RequestBody Recipie recipie, Authentication auth) throws RecipieValidationException {
		
		System.out.println("Inside post /recipie mapping");		
		logger.info("Inside post /recipie mapping");

		HashMap<String, Object> entities = new HashMap<String, Object>();
		Recipie rec = recService.add(recipie, auth);
		try {
		if(rec!=null) {
			entities.put("rec", rec);
			return new ResponseEntity<>(entities.get("rec"), HttpStatus.CREATED);
		}else {
			entities.put("message", "Recipie details are not entered correct");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
		}catch(Exception e) {

			entities.put("message", e.getMessage());
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteRecipie(@PathVariable UUID id, Authentication auth) throws Exception{

		logger.info("Inside recipie delete mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
			recService.delete(id,auth);
			entities.put("Deleted", "Book was successfuly deleted");
			return new ResponseEntity<>(entities, HttpStatus.NO_CONTENT);
		
		}catch(Exception e) {
			entities.put("message", e.getMessage());
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateRecipie(@RequestBody Recipie recipie, Authentication auth, @PathVariable UUID id) throws Exception{
		logger.info("Inside recipie PUT mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
				Recipie updatdRec = recService.update(recipie, auth, id);
				entities.put("recipie", updatdRec);
				return new ResponseEntity<>(entities,HttpStatus.OK);			
			
		}catch(Exception e){
			entities.put("message", e.getMessage());
			return new ResponseEntity<>(entities,HttpStatus.BAD_REQUEST);			
		}		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getBookById(@PathVariable UUID id) throws Exception{
		logger.info("Inside /recipie/{id} GET mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
		Optional<Recipie> rec = recService.getRecipieById(id);
		if (null == rec) {
			entities.put("message", "Recipie does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}else {
			entities.put("recipie:",rec);
			return new ResponseEntity<>(entities,HttpStatus.OK);
		}	
		}catch(Exception e) {
			entities.put("message",e.getMessage());
			return new ResponseEntity<>(entities,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
}
