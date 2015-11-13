package com.example.mobile.data;

public class User {
	private int pk ;
	private String name ;
	private int role ;
	private String surname;
	private String login;
	private String password;
	private int residence;
	
	public User (int pk, String name, int role, String surname, String login, String password, int residence) {
		this.pk = pk;
		this.name = name;
		this.role = role;
		this.surname = surname;
		this.login = login;
		this.password = password;
		this.residence = residence;
	}
	
	public User(int pk, String surname, String name) {
		this.pk = pk;
		this.name = name;
		this.surname = surname;
	}

	public User(int pk) {
		// TODO Auto-generated constructor stub
		this.pk = pk;
		
		
	}

	public int getPk() {
		return pk;
	}
	
	public String getName() {
		return name;
	}

	public int getRole() {
		return role;
	}
	public String getSurname() {
		return surname;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getResidence() {
		return residence;
	}
	
	

	public void setPk(int pk){
		this.pk = pk;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setRole(int role){
		this.role = role;
	}
	
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	public void setLogin(String login){
		this.login = login;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	
	public void setResidence(int residence){
		this.residence = residence;
	}
}
