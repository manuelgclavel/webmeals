package com.example.mobile.data;

import java.util.Date;

public class MealSelectionPlus {

	private int pk ;
	private int mealOption ;
	private int foodRegime ;
	private int meal ;
	private int ownedBy ;
	private int selectedBy;
	private int offeredTo;
	private Date date;
	
	public MealSelectionPlus(int pk, int mealOption, int foodRegime, int meal, int ownedBy, 
			int selectedBy, int offeredTo){
		this.pk = pk;
		this.mealOption = mealOption;
		this.foodRegime = foodRegime;
		this.meal = meal;
		this.ownedBy = ownedBy;
		this.selectedBy = selectedBy;
		this.offeredTo = offeredTo;
	}
	
	public MealSelectionPlus(int pk, int mealOption, int foodRegime, int meal, int ownedBy, 
			int selectedBy, int offeredTo, Date date){
		this.pk = pk;
		this.mealOption = mealOption;
		this.foodRegime = foodRegime;
		this.meal = meal;
		this.ownedBy = ownedBy;
		this.selectedBy = selectedBy;
		this.offeredTo = offeredTo;
		this.date = date;
	}

	public int getPk() {
		return pk;
	}
	
	public int getMealOption() {
		return mealOption;
	}

	public int getFoodRegime() {
		return foodRegime;
	}
	public int getMeal() {
		return meal;
	}
	
	public int getOwnedBy() {
		return ownedBy;
	}
	
	public int getSelectedBy() {
		return selectedBy;
	}
	
	public int getOfferedTo() {
		return offeredTo;
	}
	
	public Date getDate(){
		return this.date;
		
	}
	
	

	void setPk(int pk){
		this.pk = pk;
	}
	
	void setMealOption(int mealOption){
		this.mealOption = mealOption;
	}
	
	void setFoodRegime(int foodRegime){
		this.foodRegime = foodRegime;
	}
	
	void setMeal(int meal){
		this.meal = meal;
	}
	
	void setOwnedBy(int ownedBy){
		this.ownedBy = ownedBy;
	}
	
	void setSelectedBy(int selectedBy){
		this.selectedBy = selectedBy;
	}
	
	void setOfferedTo(int offeredTo){
		this.offeredTo = offeredTo;
	}
	

	public void setDate(Date date){
		this.date = date;
	}
}
