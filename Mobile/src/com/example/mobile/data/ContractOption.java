package com.example.mobile.data;

public class ContractOption {
	private int pk ;
	private int mealoption ;
	private int includedin;

	public ContractOption (int pk, int mealoption, int includedin) {
		this.pk = pk;
		this.mealoption = mealoption;
		this.includedin = includedin;
	}

	public int getPk() {
		return pk;
	}

	public int getMealOption() {
		return mealoption;
	}

	public int getIncludedIn() {
		return includedin;
	}



	void setPk(int pk){
		this.pk = pk;
	}

	void setMealOption(int mealoption){
		this.mealoption = mealoption;
	}

	
	void setIncludedIn(int includedin){
		this.includedin = includedin;
	}
}
