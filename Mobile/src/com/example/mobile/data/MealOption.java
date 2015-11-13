package com.example.mobile.data;

public class MealOption {
	private int pk ;
	private int position ;
	private String literal ;
	private String initial;
	private int ownedBy;
	
	public MealOption (int pk, int position, String initial, String literal, int ownedBy) {
		this.pk = pk;
		this.position = position;
		this.initial = initial;
		this.literal = literal;
		this.ownedBy = ownedBy;
	}
	
	public int getPk() {
		return pk;
	}
	
	public int getPosition() {
		return position;
	}

	public String getInitial() {
		return initial;
	}
	public String getLiteral() {
		return literal;
	}
	
	public int getOwnedBy() {
		return ownedBy;
	}

	void setPk(int pk){
		this.pk = pk;
	}
	
	void setPosition(int position){
		this.position = position;
	}
	
	void setInitial(String initial){
		this.initial = initial;
	}
	
	void setLiteral(String literal){
		this.literal = literal;
	}
	
	void setOwnedBy(int ownedBy){
		this.ownedBy = ownedBy;
	}
}
