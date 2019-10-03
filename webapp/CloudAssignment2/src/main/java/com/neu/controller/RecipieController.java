package com.neu.controller;

import java.net.MalformedURLException;
import java.util.HashMap;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(path="/v1")
public class RecipieController {
	
	@Autowired
	private RecipieService recService;

	private final static Logger logger = LoggerFactory.getLogger(RecipieController.class);
	
	@PostMapping("/recipie/")
	public ResponseEntity<Object> addUsers(@Valid @RequestBody Recipie recipie) throws RecipieValidationException {
		
		System.out.println("Inside post /recipie mapping");		
		logger.info("Inside post /recipie mapping");

		HashMap<String, Object> entities = new HashMap<String, Object>();
		Recipie rec = recService.add(recipie);
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
	
	@DeleteMapping("/recipie/{id}")
	public ResponseEntity<Object> deleteRecipie(@PathVariable(value = "id") String recipieId) throws MalformedURLException, RecipieValidationException {
		//statsDClient.incrementCounter("endpoint.book.id.http.del");
		logger.info("Inside /recipie/del recipie DELETE mapping");
		Recipie rec = recService.getRecipieById(recipieId);
		//Book book = bookDaoServiceImpl.getBookById(bookId);
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
		if (null == rec) {
			entities.put("message", "Rec does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		} 
		else{
			recService.deleteRecipie(rec);
			entities.put("Deleted", "Book was successfuly deleted");
			return new ResponseEntity<>(entities, HttpStatus.NO_CONTENT);
		}
		}catch(Exception e) {
			entities.put("message", "Rec does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/recipie/{id}")
	public ResponseEntity<Object> updateRecipie(@RequestBody Recipie recipie, @PathVariable String id) throws RecipieValidationException{
		logger.info("Inside /recipie/{id} PUT mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
			
			Recipie rec = recService.getRecipieById(id);
			if (null == rec) {
				entities.put("message", "Recipie does not exist");
				return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
			}else {
				Recipie updatdRec = recService.updateRecipie(recipie, id);
				entities.put("recipie", updatdRec);
				return new ResponseEntity<>(entities,HttpStatus.OK);			
			}
		}catch(RecipieValidationException rve){
			entities.put("message", rve.getMessage());
			return new ResponseEntity<>(entities,HttpStatus.BAD_REQUEST);			
			
		}
		
	}
	
	@GetMapping("/recipie/{id}")
	public ResponseEntity<Object> getBookById(@PathVariable(value = "id") String recId) throws MalformedURLException, RecipieValidationException {
		logger.info("Inside /recipie/{id} GET mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
		Recipie rec = recService.getRecipieById(recId);
		if (null == rec) {
			entities.put("message", "Recipie does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}else {
			entities.put("recipie",rec);
			return new ResponseEntity<>(entities,HttpStatus.OK);
		}	
		}catch(Exception e) {
			entities.put("message",e.getMessage());
			return new ResponseEntity<>(entities,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
