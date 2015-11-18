package com.example.mobile.data;

public class MealCount {
	private int mealOption;
	private int foodRegime;
	private int meal;
	private int daily;
	private String username;
	private String usersurname;
	private int selectedBy;
	private int offeredTo;
	private String guestname;
	private String guestsurname;
	
	public MealCount (int mealOption, int foodRegime, int meal, int daily, 
			String username, String usersurname, int selectedBy, int offeredTo, 
			String guestname, String guestsurname){
		this.mealOption = mealOption;
		this.foodRegime = foodRegime;
		this.meal = meal;
		this.daily = daily;
		this.username = username;
		this.usersurname = usersurname;
		this.selectedBy = selectedBy;
		this.offeredTo = offeredTo;
		this.guestname = guestname;
		this.guestsurname = guestsurname;
		
	}
	
	public int getMealOption(){
		return this.mealOption;
	}
	public int getFoodRegime(){
		return this.foodRegime;
	}
	public int getMeal(){
		return this.meal;
	}
	public int getDaily(){
		return this.daily;
	}
	public String getUsername(){
		return this.username;
	}
	public String getUsersurname(){
		return this.usersurname;
	}
	public int getSelectedBy(){
		return this.selectedBy;
	}
	public int getOfferedTo(){
		return this.offeredTo;
	}
	public String getGuestname(){
		return this.guestname;
	}
	public String getGuestsurname(){
		return this.guestsurname;
	}
	
	
	public void setMealOption(int mealOption){
		this.mealOption = mealOption;
	}
	
	public void setFoodRegime(int foodRegime){
		this.foodRegime= foodRegime;
	}
	public void setMeal(int meal){
		this.meal = meal;
	}
	public void setDaily(int daily){
		this.daily = daily;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setUsersurname(String usersurname){
		this.usersurname = usersurname;
	}
	public void setSelectedBy(int selectedBy){
		this.selectedBy = selectedBy;
	}
	public void setOfferedTo(int offeredTo){
		this.offeredTo = offeredTo;
	}
	public void setGuestname(String guestname){
		this.guestname = guestname;
	}
	public void setGuestsurname(String guestsurname){
		this.guestsurname = guestsurname;
	}
	

}
