package com.example.mobile.data;

public class ContractResidency {
	private int contract;
	private int residency;
	
	public ContractResidency(int contract, int residency) {
		this.contract = contract;
		this.residency = residency;
		
	}

	public int getContract(){
		return this.contract;
	}
	public int getResidency(){
		return this.residency;
	}
	
	public void setContract(int contract){
		this.contract = contract;
	}
	public void setResidency(int residency){
		this.residency = residency;
	}
}


