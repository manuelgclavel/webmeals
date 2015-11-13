package com.example.mobile.data;

public class Guest {
	private int pk ;
	private String name ;
	private String surname;
	private int residence;

	public Guest (int pk, String name, String surname, int residence) {
		this.pk = pk;
		this.name = name;
		this.surname = surname;
		this.residence = residence;
	}

	public int getPk() {
		return pk;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
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

	void setSurname(String surname){
		this.surname = surname;
	}

	void setResidence(int residence){
		this.residence = residence;
	}
}
