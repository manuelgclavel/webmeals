package com.example.mobile.viewer;

import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.ContractOption;
import com.example.mobile.data.ContractOptionDay;
import com.example.mobile.data.MealOption;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ContractOptionDayCheckBox extends CheckBox {
	private final MobileUI ui = (MobileUI) UI.getCurrent();
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	
	private BeanItemContainer<ContractOption> contractoptions;
			//new BeanItemContainer<ContractOption>(ContractOption.class);
	private BeanItemContainer<ContractOptionDay> contractoptiondays;
			//new BeanItemContainer<ContractOptionDay>(ContractOptionDay.class);
			
	//private boolean stored;
	
	public ContractOptionDayCheckBox(
			final MealOption mealoption, final Contract selected, final int day,
			BeanItemContainer<ContractOption> contractoptions, 
			BeanItemContainer<ContractOptionDay> contractoptiondays){
		this.contractoptions = contractoptions;
		this.contractoptiondays = contractoptiondays;
		//syncronize();
		
		setValue(contractmealoptiondayisincluded(mealoption, selected, day));
		
		addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				// TODO Auto-generated method stub
				syncronize();
				boolean chosen = (boolean) event.getProperty().getValue();
				int status = mealoptiondayincontract(mealoption, selected, day);

				switch (status) {
				case (1) : 
					if (chosen) {
						ui.insertContractMealOptionDay(mealoption, selected, day);	
						syncronize();
						Notification.show("DONE!");
					} else {
						Notification.show("Nothing to do!");
					};
					break;
				case (2) : 
					if (chosen) {					
						ui.insertContractMealOptionOnlyDay(selected,
								findContractOption(mealoption, selected, day),
								day);
						syncronize();
						Notification.show("DONE!");
						
					} else {
						ui.deleteContractMealOptionOnlyDay(selected,
								findContractOption(mealoption, selected, day),
								day);
						syncronize();
						Notification.show("DONE!");
					};
					break;
				case (3) :
					if (chosen) { 
						Notification.show("Nothing to do");
					} else {
						ui.deleteContractMealOptionOnlyDay(selected,
								findContractOption(mealoption, selected, day),
								day);
						syncronize();
						Notification.show("DONE!");
					};
					break;
				default :
					break;
				}
				

			} });
	}
	
	

	protected void syncronize() {
		// TODO Auto-generated method stub
		contractoptions.removeAllItems();
		contractoptiondays.removeAllItems();
		ui.populateContractOptions(connectionPool, contractoptions);
		ui.populateContractOptionDays(connectionPool, contractoptiondays);

	}



	private boolean contractmealoptiondayisincluded(MealOption mealoption, Contract selected, int day) {
		// TODO Auto-generated method stub
		boolean included = false;
		ContractOption contractoption;
		ContractOptionDay contractoptionday;
		for(Iterator<ContractOption> i = contractoptions.getItemIds().iterator(); i.hasNext() ;){
			contractoption = i.next();
			if (contractoption.getIncludedIn() == selected.getPk()){
				if (contractoption.getMealOption() == mealoption.getPk()){
					for(Iterator<ContractOptionDay> j = contractoptiondays.getItemIds().iterator(); j.hasNext();){
						contractoptionday = j.next();
						if (contractoptionday.getContract() == contractoption.getPk()){
							if (contractoptionday.getDay() == day){
								included = true;
								break;
							}
						}
					}
				}
			}
		}
		
		return included;
	}
	
	
	
	private int mealoptiondayincontract(MealOption mealoption, Contract selected, int day) {
		// TODO Auto-generated method stub
		int status = 1;
		ContractOption contractoption;
		ContractOptionDay contractoptionday;
		for(Iterator<ContractOption> i = contractoptions.getItemIds().iterator(); i.hasNext() ;){
			contractoption = i.next();
			if (contractoption.getIncludedIn() == selected.getPk()){
				if (contractoption.getMealOption() == mealoption.getPk()){
					status = 2;
					for(Iterator<ContractOptionDay> j = contractoptiondays.getItemIds().iterator(); j.hasNext();){
						contractoptionday = j.next();
						if (contractoptionday.getContract() == contractoption.getPk()){
							if (contractoptionday.getDay() == day){
								status = 3;
								break;
							}
						}
					}
				}
			}
		}
		
		return status;
	}
	
	
	

	private ContractOption findContractOption(MealOption mealoption, Contract selected, int day) {
		// TODO Auto-generated method stub
		ContractOption result = null;
		ContractOption contractoption;
		for(Iterator<ContractOption> i = contractoptions.getItemIds().iterator(); i.hasNext() ;){
			contractoption = i.next();
			if (contractoption.getIncludedIn() == selected.getPk()){
				if (contractoption.getMealOption() == mealoption.getPk()){
					result = contractoption;
					break;

				}
			}
		}

		return result;
	}



}
