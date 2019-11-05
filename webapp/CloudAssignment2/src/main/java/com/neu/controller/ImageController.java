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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.neu.exceptionHandler.ImageException;
import com.neu.exceptionHandler.RecipieValidationException;
import com.neu.model.Image;
import com.neu.model.Recipie;
import com.neu.service.ImageService;

@RestController
@RequestMapping("/v1/recipie/{id}/image")
public class ImageController {
	
	private final static Logger logger = LoggerFactory.getLogger(ImageController.class);
	
    private ImageService imageService;

    @Autowired
    ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    
	@PostMapping("")
	public ResponseEntity<Object> uploadImage(@RequestPart(value = "image") MultipartFile file, Authentication auth, @PathVariable UUID id) throws Exception {
		
		logger.info("Inside post /image mapping");

		HashMap<String, Object> entities = new HashMap<String, Object>();
		Image i = imageService.uploadFile(file, auth, id);
		try {
		if(i!=null) {
			entities.put("image", i);
			return new ResponseEntity<>(entities.get("image"), HttpStatus.CREATED);
		}else {
			entities.put("message", "File not uploaded");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
		}catch(Exception e) {
			entities.put("message","Image already present in recipie");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{imageId}")
	public ResponseEntity<Object> deleteRecipie(@PathVariable UUID id, @PathVariable UUID imageId, Authentication auth) throws Exception{

		logger.info("Inside Image delete mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
			imageService.delete(id, imageId, auth);
			entities.put("Deleted", "Image was successfuly deleted");
			return new ResponseEntity<>(entities, HttpStatus.NO_CONTENT);
		
		}catch(Exception e) {
			entities.put("message", e.getMessage());
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/{imageId}")
	public ResponseEntity<Object> getBookById(@PathVariable UUID id, @PathVariable UUID imageId) throws Exception{
		logger.info("Inside /Image/{id} GET mapping");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		try {
		Optional<Image> im = imageService.getImage(id, imageId);
		if (null == im) {
			entities.put("message", "Recipie does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}else {
			entities.put("image:",im);
			return new ResponseEntity<>(entities,HttpStatus.OK);
		}	
		}catch(Exception e) {
			entities.put("message",e.getMessage());
			return new ResponseEntity<>(entities,HttpStatus.BAD_REQUEST);
		}
		
	}

}
