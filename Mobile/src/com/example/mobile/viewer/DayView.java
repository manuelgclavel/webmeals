package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.MealOptionDeadline;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class DayView extends HorizontalLayout {
	
	private Label daysLabel;
	private Button plus;
	private Button minus;
	private Integer days;
	
	private MobileUI ui = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	private MealOptionDeadline mealoptiondeadline;

	
	
	public DayView(MealOptionDeadline selected){
		mealoptiondeadline = selected;
		days = mealoptiondeadline.getCday();
		daysLabel = new Label(displayDays(days));
		//minutesLabel.setContentMode(ContentMode.HTML);
		plus = new Button("+");
		minus = new Button("-");
		
		addComponent(daysLabel);
		addComponent(plus);
		addComponent(minus);
		
		
		plus.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (days < 6) {
					days = days + 1;
				} else {
					days = 0;
				}
				daysLabel.setValue(displayDays(days));
				ui.updateMealOptionDeadlineDays(connectionPool, mealoptiondeadline, days);
				
			}});

		minus.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (days > 0) {
					days = days - 1;
				} else {
					days = 6;
				}
				daysLabel.setValue(displayDays(days));
				ui.updateMealOptionDeadlineDays(connectionPool, mealoptiondeadline, days);
				
			}});
	}
		
		private String displayDays(Integer days) {
			// TODO Auto-generated method stub
			String display = "";
			if (days >=0 && days <= 6){
				//display = "<center>" + "0" + minutes.toString() + "</center>";
				display = "0" + days.toString();
			} else {
				//display =  "<center>" + minutes.toString() + "</center>";
				display =  days.toString();
			}
			return display;
		}
	
	

}
