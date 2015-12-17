package com.example.mobile.viewer;

import com.example.mobile.data.Contract;
import com.example.mobile.data.MealOption;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class ContractOptionDayCheckBox extends CheckBox {
	private MealOption mealoption;
	private Contract contract;
	private int day;
	
	public ContractOptionDayCheckBox(MealOption mealoption, Contract selected, int day){
		this.mealoption = mealoption;
		this.contract = selected;
		this.day = day;
		
		this.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				// TODO Auto-generated method stub
				ui.insertContractOptionDay)
				Notification.show("DONE");
				
			} });
		
	}
	
	

}
