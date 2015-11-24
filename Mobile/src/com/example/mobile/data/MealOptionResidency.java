package com.example.mobile.data;

public class MealOptionResidency {
	private int mealOption;
	private int residency;
	
	public MealOptionResidency(int mealOption, int residency) {
		this.mealOption = mealOption;
		this.residency = residency;
		
	}

	public int getMealOption(){
		return this.mealOption;
	}
	public int getResidency(){
		return this.residency;
	}
	
	public void setMealOption(int mealOption){
		this.mealOption = mealOption;
	}
	public void setResidency(int residency){
		this.residency = residency;
	}
}


