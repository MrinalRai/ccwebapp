package com.neu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orderedList")
public class OrderedList {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "position")
	@Min(1)
    private int position;
	@Column(name = "items")
    private String items;
	
	 @ManyToOne
	 @JoinColumn(name="recipie_id", nullable=false, updatable= false)
	 private Recipie recipie;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public Recipie getRecipie() {
		return recipie;
	}
	public void setRecipie(Recipie recipie) {
		this.recipie = recipie;
	}

}
