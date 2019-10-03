package com.neu.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.neu.exceptionHandler.RecipieValidationException;
import com.neu.model.Recipie;
import com.neu.repository.RecipieRepository;

public class RecipieService {
	@Autowired
	private RecipieRepository recRepo;
	
	public Recipie add(Recipie recipie) throws RecipieValidationException {
		Recipie r = new Recipie();
		
		r.setUser(recipie.getUser());
		
		if((recipie.getCook_time_in_min()%5)==0) {			
			r.setCook_time_in_min(recipie.getCook_time_in_min());
		}else throw new RecipieValidationException("Cook time must be a multiple of 5");
		
		if((recipie.getPrep_time_in_min()%5)==0) {			
			r.setPrep_time_in_min(recipie.getPrep_time_in_min());
		}else throw new RecipieValidationException("Prep time must be a multiple of 5");
		
		if((recipie.getTotal_time_in_min()%5)==0) {			
			r.setTotal_time_in_min(recipie.getTotal_time_in_min());
		}else throw new RecipieValidationException("Total time must be a multiple of 5");
		
		r.setTitle(recipie.getTitle());
		r.setCusine(recipie.getCusine());
		r.setServings(recipie.getServings());
		//List<Ingredients> ing = recipie.getIngredients();
		r.setIngredients(recipie.getIngredients());
		r.setoList(recipie.getoList());
		r.setnInfo(recipie.getnInfo());
			
		return recRepo.save(r);
	}
	
	public Recipie getRecipieById(String id) throws RecipieValidationException {
		Recipie r = recRepo.getRecipieById(id);
		if(r.equals(null)) {
			throw new RecipieValidationException("Null value for this id");
		}
		return r;
	}
	
	public void deleteRecipie(Recipie recipie) {
		recRepo.delete(recipie);
	}
	
	public Recipie updateRecipie(Recipie recipie, String id) throws RecipieValidationException {
		Recipie r = recRepo.getRecipieById(id);
		
		r.setUser(recipie.getUser());
		if((recipie.getCook_time_in_min()%5)==0) {			
			r.setCook_time_in_min(recipie.getCook_time_in_min());
		}else throw new RecipieValidationException("Cook time must be a multiple of 5");
		
		if((recipie.getPrep_time_in_min()%5)==0) {			
			r.setPrep_time_in_min(recipie.getPrep_time_in_min());
		}else throw new RecipieValidationException("Prep time must be a multiple of 5");
		
		if((recipie.getTotal_time_in_min()%5)==0) {			
			r.setTotal_time_in_min(recipie.getTotal_time_in_min());
		}else throw new RecipieValidationException("Total time must be a multiple of 5");
		
		r.setTitle(recipie.getTitle());
		r.setCusine(recipie.getCusine());
		r.setServings(recipie.getServings());
		//List<Ingredients> ing = recipie.getIngredients();
		r.setIngredients(recipie.getIngredients());
		r.setoList(recipie.getoList());
		r.setnInfo(recipie.getnInfo());
			
		return recRepo.save(r);
		}
	

}
