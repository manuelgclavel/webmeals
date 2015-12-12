package com.example.mobile.data;

public class Residence {
	private int pk;
	private String name;
	private String lang;
	private String zone;


	public Residence(int pk){
		this.pk = pk;
	}
	


	public Residence(int pk, String name, String lang, String zone) {
		this.pk = pk;
		this.name = name;
		this.lang = lang;
		this.zone = zone;
	}



	public int getPk() {
		return pk;
	}

	public String getName() {
		return name;
	}

	public String getLang() {
		return lang;
	}

	public String getZone() {
		return zone;
	}

	public void setPk(int pk){
		this.pk  = pk;
	}

	public void setName(String name){
		this.name  = name;
	}

	public void setLang(String lang){
		this.lang  = lang;
	}

	public void setZone(String zone){
		this.zone  = zone;
	}

}

