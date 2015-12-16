package com.example.mobile.data;

public class Contract {

	private int pk ;
	private String name ;
	private int residence;

	public Contract (int pk, String name, int residence) {
		this.pk = pk;
		this.name = name;
		this.residence = residence;
	}

	public int getPk() {
		return pk;
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

	void setName(String name){
		this.name = name;
	}

	
	void setResidence(int residence){
		this.residence = residence;
	}
}
