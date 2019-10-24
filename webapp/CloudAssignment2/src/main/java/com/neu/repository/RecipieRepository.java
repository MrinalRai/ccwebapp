package com.neu.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.neu.model.Recipie;

public interface RecipieRepository extends CrudRepository<Recipie, UUID>{
	Optional<Recipie> findRecipiesById(UUID uuid);
	
}
