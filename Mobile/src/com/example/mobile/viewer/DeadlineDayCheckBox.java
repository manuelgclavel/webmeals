package com.example.mobile.viewer;

import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.ContractOption;
import com.example.mobile.data.ContractOptionDay;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.Residency;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DeadlineDayCheckBox extends CheckBox {
	private final MobileUI ui = (MobileUI) UI.getCurrent();
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	//private BeanItemContainer<Residency> periods;
	private BeanItemContainer<DeadlineDay> deadlinedays = 
				new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
	private BeanItemContainer<MealOptionDeadline> filterdeadlines;
		
	public DeadlineDayCheckBox  (final MealOptionDeadline deadline, 
			final int day, final Residency period,
			final BeanItemContainer<MealOptionDeadline> filterdeadlines){
		
		this.filterdeadlines = filterdeadlines;
		setValue(deadlinemealoptiondayisincluded(deadline, day));
	
	
	this.addValueChangeListener(new ValueChangeListener(){

		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			// TODO Auto-generated method stub
			syncronize();
			boolean chosen = (boolean) event.getProperty().getValue();
			boolean status = deadlinemealoptiondayisincluded(deadline, day);
			if (status){
				if (chosen){
					// true in database, true in checkbox
					//Notification.show("NOTHING CHANGES!");
				} else {
					// true in database, false in checkbox
					//Notification.show("DELETED!");
					ui.deleteMealOptionDeadlineDay(deadline, day);
					//ui.populateDeadlineDays(connectionPool, deadlinedays);
					syncronize();
					Notification.show("DONE!");
				}
			} else {
				if (chosen){
					// false in database, true in checkbox
					// first we delete this day in ALL deadlines for this period
					//ui.deleteMealOptionDeadlineDay(filterdeadlines, day);
					if (!(checkNoDeadlineForDay(filterdeadlines,day))){
						ui.insertMealOptionDeadlineDay(deadline, day);
						syncronize();
						Notification.show("DONE!");
					} else {
						Notification.show("Please, delete the existing deadline for this day first");
						event.getProperty().setValue(status);
					}
					// next, we insert this day in this deadline
					
					// finally, we syncronize
					
				} else {
					// false in database, false in checkbox
					//Notification.show("NOTHING CHANGES!");
				}
				
			}
		}
	});
	}
		//@Override
		//public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			// TODO Auto-generated method stub
			
			
			/**
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
			*/

		//} });
		
//}




private Boolean deadlinemealoptiondayisincluded(MealOptionDeadline deadline, int day) {
		// TODO Auto-generated method stub
	boolean included = false;
	ui.populateDeadlineDays(connectionPool, deadlinedays);
	DeadlineDay deadlineday;
	for (Iterator<DeadlineDay> j = deadlinedays.getItemIds().iterator(); j.hasNext();){
		deadlineday = (DeadlineDay) j.next();
		if (deadlineday.getDeadline() == deadline.getPk()){
			if (deadlineday.getDay() == day){
				included = true;
				break;
			}
		}
	}
	return included;
}

private Boolean checkNoDeadlineForDay(BeanItemContainer<MealOptionDeadline> filterdeadlines, int day){
	boolean existdeadline = false;
	MealOptionDeadline mealoptiondeadline;
	DeadlineDay deadlineday;
	for (Iterator<MealOptionDeadline> i = filterdeadlines.getItemIds().iterator(); i.hasNext();){
		mealoptiondeadline = (MealOptionDeadline) i.next();
		for (Iterator<DeadlineDay> j = deadlinedays.getItemIds().iterator(); j.hasNext();){
			deadlineday = (DeadlineDay) j.next();
			if (deadlineday.getDeadline() == mealoptiondeadline.getPk() && deadlineday.getDay() == day){
				existdeadline = true;
				break;
			}
		}
	}
	
	return existdeadline;
}
/**

private int deadlinedayinmealoption(MealOption mealoption, Contract selected, int day) {
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

*/



protected void syncronize() {
	// TODO Auto-generated method stub
	deadlinedays.removeAllItems();
	ui.populateDeadlineDays(connectionPool, deadlinedays);
}




}

