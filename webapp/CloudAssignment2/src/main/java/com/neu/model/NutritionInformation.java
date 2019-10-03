package com.neu.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nutrition_information")
public class NutritionInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "calories",nullable=false)
    private int calories;
    @Column(name = "cholesterol_in_mg",nullable=false)
    private float cholesterol_in_mg;
    @Column(name = "sodium_in_mg",nullable=false)
    private int sodium_in_mg;
    @Column(name = "carbohydrates_in_grams",nullable=false)
    private float carbohydrates_in_grams;
    @Column(name = "protein_in_grams",nullable=false)
    private float protein_in_grams;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipie_id", referencedColumnName = "id")
    private Recipie recipie;
   

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public float getCholesterol_in_mg() {
		return cholesterol_in_mg;
	}

	public void setCholesterol_in_mg(float cholesterol_in_mg) {
		this.cholesterol_in_mg = cholesterol_in_mg;
	}

	public int getSodium_in_mg() {
		return sodium_in_mg;
	}

	public void setSodium_in_mg(int sodium_in_mg) {
		this.sodium_in_mg = sodium_in_mg;
	}

	public float getCarbohydrates_in_grams() {
		return carbohydrates_in_grams;
	}

	public void setCarbohydrates_in_grams(float carbohydrates_in_grams) {
		this.carbohydrates_in_grams = carbohydrates_in_grams;
	}

	public float getProtein_in_grams() {
		return protein_in_grams;
	}

	public void setProtein_in_grams(float protein_in_grams) {
		this.protein_in_grams = protein_in_grams;
	}

	public Recipie getRecipie() {
		return recipie;
	}

	public void setRecipie(Recipie recipie) {
		this.recipie = recipie;
	}
}
