package com.neu.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "recipie")
@JsonIgnoreProperties(value = {"created_ts", "updated_ts"}, 
allowGetters = true)
public class Recipie {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	
	private String id;
	
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_ts", nullable=false,updatable= false)
    private Date created_ts;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_ts",nullable=false,updatable= false)
    private Date updated_ts;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "cook_time_in_min",nullable=false)
    private int cook_time_in_min;
    
    @Column(name = "prep_time_in_min",nullable=false)
    private int prep_time_in_min;
    
    @Column(name = "total_time_in_min",updatable= false)
    private int total_time_in_min;
    
    @Column(name = "title",nullable=false)
    private String title;
    
    @Column(name = "cusine",nullable=false)
    private String cusine;
    
    @Column(name = "servings",nullable=false)
    @Max(5)
    @Min(1)
    private int servings;
    
    @Column(name = "ingredients",nullable=false)
    private List<String> ingredients;
    
    @OneToMany(mappedBy="recipie")
   // @Column(name= "steps",nullable=false)
    private Set<OrderedList> oList;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nInfo_id", nullable = false)
    private NutritionInformation nInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated_ts() {
		return created_ts;
	}

	public void setCreated_ts(Date created_ts) {
		this.created_ts = created_ts;
	}

	public Date getUpdated_ts() {
		return updated_ts;
	}

	public void setUpdated_ts(Date updated_ts) {
		this.updated_ts = updated_ts;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getCook_time_in_min() {
		return cook_time_in_min;
	}

	public void setCook_time_in_min(int cook_time_in_min) {
		this.cook_time_in_min = cook_time_in_min;
	}

	public int getPrep_time_in_min() {
		return prep_time_in_min;
	}

	public void setPrep_time_in_min(int prep_time_in_min) {
		this.prep_time_in_min = prep_time_in_min;
	}

	public int getTotal_time_in_min() {
		return total_time_in_min;
	}

	public void setTotal_time_in_min(int total_time_in_min) {
		this.total_time_in_min = total_time_in_min;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCusine() {
		return cusine;
	}

	public void setCusine(String cusine) {
		this.cusine = cusine;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public Set<OrderedList> getoList() {
		return oList;
	}

	public void setoList(Set<OrderedList> oList) {
		this.oList = oList;
	}

	public NutritionInformation getnInfo() {
		return nInfo;
	}

	public void setnInfo(NutritionInformation nInfo) {
		this.nInfo = nInfo;
	}
    
    

}
