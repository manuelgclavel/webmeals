package com.example.mobile.data;

public class DeadlineDay {
	private int deadline;
	private int day;

	public DeadlineDay(int deadline, int day){
		this.deadline = deadline;
		this.day = day;
		
	}
	
	public int getDeadline(){
		return deadline;
	}
	public int getDay(){
		return day;
	}
	
	public void setDeadline(int deadline){
		this.deadline = deadline;
	}
	public void setDay(int day){
		this.day = day;
	}
}
