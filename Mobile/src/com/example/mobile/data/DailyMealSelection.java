package com.example.mobile.data;

import java.sql.Date;

public class DailyMealSelection {
	private int pk;
	private Date date;
	private int selectedBy;
	private int offeredTo;
	
	public DailyMealSelection(int pk){
		this.pk = pk;
	}
	
	public DailyMealSelection(int pk, Date date, int selectedBy, int offeredTo) {
		// TODO Auto-generated constructor stub
		this.pk = pk;
		this.date = date;
		this.selectedBy = selectedBy;
		this.offeredTo = offeredTo;
		
	}

	public int getPk(){
		return this.pk;
	}
	
	public Date getDate(){
		return this.date;
		
	}
	
	public int getSelectedBy(){
		return this.selectedBy;
		
	}
	public int getOfferedTo(){
		return this.offeredTo;
		
	}
	
	
	public void setPk(int pk){
		this.pk = pk;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	public void setSelectedBy(int selectedBy){
		this.selectedBy = selectedBy;
	}
	public void setOfferedTo(int offeredTo){
		this.offeredTo = offeredTo;
	}
	

}
