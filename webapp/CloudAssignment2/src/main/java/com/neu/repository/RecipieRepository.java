package com.neu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neu.model.Recipie;

public interface RecipieRepository extends CrudRepository<Recipie, String>{
	
	@Query(value = "SELECT * FROM recipie r WHERE r.id = ?1", nativeQuery = true)
	Recipie getRecipieById(String recipie_id);

}
