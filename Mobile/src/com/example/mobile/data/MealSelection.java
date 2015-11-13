package com.example.mobile.data;

public class MealSelection {

	private int pk ;
	private int mealOption ;
	private int foodRegime ;
	private int meal ;
	private int ownedBy ;
	
	public MealSelection(int pk, int mealOption, int foodRegime, int meal, int ownedBy){
		this.pk = pk;
		this.mealOption = mealOption;
		this.foodRegime = foodRegime;
		this.meal = meal;
		this.ownedBy = ownedBy;
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
}
