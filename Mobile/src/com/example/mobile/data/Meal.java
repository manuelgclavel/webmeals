package com.example.mobile.data;

public class Meal {
	private int pk ;
	private int position ;
	private String literal ;
	private int residence;
	
	public Meal (int pk, int position, String literal, int residence) {
		this.pk = pk;
		this.position = position;
		this.literal = literal;
		this.residence = residence;
	}
	
	public int getPk() {
		return pk;
	}
	
	public int getPosition() {
		return position;
	}
	
	public String getLiteral() {
		return literal;
	}
	
	public int getResidence() {
		return residence;
	}

	void setPk(int pk){
		this.pk = pk;
	}
	
	void setPosition(int position){
		this.position = position;
	}
	
	void setLiteral(String literal){
		this.literal = literal;
	}
	
	void setResidence(int residence){
		this.residence = residence;
	}
	
}
