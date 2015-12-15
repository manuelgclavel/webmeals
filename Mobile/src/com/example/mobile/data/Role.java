package com.example.mobile.data;

public class Role {
	private int id;
	private String literal;

	
	public Role(int id, String literal){
		this.id = id;
		this.literal = literal;
	}
	
	public Role(int id){
		this.id = id;
		
		switch(id) {
		case (0):
			this.literal = "Resident";
			break;
		case (1):
			this.literal = "Director";
			break;
		case (2):
			this.literal = "Administration";
			break;
		case (3):
			this.literal = "System";
			break;
		default : 
			this.literal = "";
			break;
		}
	}
	
	
	
	public int getId() {
		return id;
	}
	
	public String getLiteral() {
		return literal;
	}
	
	public void setId(int id){
		this.id  = id;
	}
	
	public void setLiteral(String literal){
		this.literal  = literal;
	}
	
}
