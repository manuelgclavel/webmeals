package com.example.mobile.data;

import java.util.Date;

public class Residency {
	private int pk ;
	private Date start ;
	private Date end;
	private int ownedByRegime;
	private int contract;
	private int ownedByOption;
	private int guestRegime;
	private int ownedBy;
	private int regime;

	public Residency(){
		
	}

	public int getPk() {
		 return pk;
	}
	public Date getStart() {
		 return start;
	}
	public Date getEnd() {
		 return end;
	}
	public int getOwnedByRegime() {
		 return ownedByRegime;
	}
	public int getContract() {
		 return contract;
	}
	public int getOwnedByOption() {
		 return ownedByOption;
	}
	public int getGuestRegime() {
		 return guestRegime;
	}
	public int getOwnedBy() {
		 return ownedBy;
	}
	public int getRegime() {
		 return regime;
	}
	
	void setPk(int pk){
		this.pk = pk;
	}
	
	void setStart(Date start){
		this.start = start;
	}
	
	void setEnd(Date end){
		this.end = end;
	}
	
	void setOwnedByRegime(int ownedByRegime){
		this.ownedByRegime = ownedByRegime;
	}
	
	void setContract(int contract){
		this.contract = contract;
	}
	
	void setOwnedByOption(int ownedByOption){
		this.ownedByOption = ownedByOption;
	}
	void setGuestRegime(int guestRegime){
		this.guestRegime = guestRegime;
	}
	
	void setOwnedBy(int ownedBy){
		this.ownedBy = ownedBy;
	}
	void setRegime(int regime){
		this.regime = regime;
	}
	


	
	
	

}
