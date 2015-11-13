package com.example.mobile.data;

public class FoodRegime {
	private int pk ;
	private String description ;
	private String name;
	private int residence;
	
	public FoodRegime (int pk, String description, String name, int residence) {
		this.pk = pk;
		this.description = description;
		this.name = name;
		this.residence = residence;
	}
	
	public int getPk() {
		return pk;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}
	
	public int getResidence() {
		return residence;
	}

	void setPk(int pk){
		this.pk = pk;
	}
	
	void setDescription(String description){
		this.description = description;
	}
	
	void setName(String name){
		this.name = name;
	}
	
	void setResidence(int residence){
		this.residence = residence;
	}
	
}
