package com.example.mobile.data;

public class ContractOptionDay {
	private int contract;
	private int day;

	public ContractOptionDay(int contract, int day){
		this.contract = contract;
		this.day = day;
		
	}
	
	public int getContract(){
		return contract;
	}
	public int getDay(){
		return day;
	}
	
	public void setContract(int deadline){
		this.contract = deadline;
	}
	public void setDay(int day){
		this.day = day;
	}
}
