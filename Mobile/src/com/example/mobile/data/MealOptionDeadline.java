package com.example.mobile.data;

public class MealOptionDeadline {
	private int pk;
	private int cday;
	private int cminute;
	private int chour;
	private String literal;
	private int ownedBy;
	
	public MealOptionDeadline(int pk){
		this.pk = pk;
	}
	
	public MealOptionDeadline(int pk, int cday, int chour, int cminute, String literal, int ownedBy){
		this.pk = pk;
		this.cday = cday;
		this.cminute = cminute;
		this.chour = chour;
		this.literal = literal;
		this.ownedBy = ownedBy;
	}
		
	public int getPk() {
		return pk;
	}

	public int getCday() {
		return cday;
	}

	public int getCminute() {
		return cminute;
	}
	public int getChour() {
		return chour;
	}

	public String getLiteral() {
		return literal;
	}
	public int getOwnedBy() {
		return ownedBy;
	}


	public void setPk(int pk){
		this.pk = pk;
	}

	public void setCday(int cday){
		this.cday = cday;
	}

	public void setCminute(int cminute){
		this.cminute = cminute;
	}

	public void setChour(int chour){
		this.chour = chour;
	}

	public void setLiteral(String literal){
		this.literal = literal;
	}

	public void setOwnedBy(int ownedBy){
		this.ownedBy = ownedBy;
	}

}