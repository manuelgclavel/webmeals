package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.MealOptionDeadline;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MinuteView extends HorizontalLayout {
	private Label minutesLabel;
	private Button plus;
	private Button minus;
	private Integer minutes;
	
	private MobileUI ui = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	private MealOptionDeadline mealoptiondeadline;

	
	
	public MinuteView(MealOptionDeadline selected){
		mealoptiondeadline = selected;
		minutes = mealoptiondeadline.getCminute();
		minutesLabel = new Label(displayMinutes(minutes));
		//minutesLabel.setContentMode(ContentMode.HTML);
		plus = new Button("+");
		minus = new Button("-");
		
		addComponent(minutesLabel);
		addComponent(plus);
		addComponent(minus);
		
		
		plus.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (minutes < 59) {
					minutes = minutes + 1;
				} else {
					minutes = 0;
				}
				minutesLabel.setValue(displayMinutes(minutes));
				ui.updateMealOptionDeadlineMinutes(connectionPool, mealoptiondeadline, minutes);
				
			}});

		minus.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (minutes > 0) {
					minutes = minutes - 1;
				} else {
					minutes = 59;
				}
				minutesLabel.setValue(displayMinutes(minutes));
				ui.updateMealOptionDeadlineMinutes(connectionPool, mealoptiondeadline, minutes);
				
			}});
	}
		
		private String displayMinutes(Integer minutes) {
			// TODO Auto-generated method stub
			String display = "";
			if (minutes >=0 && minutes <= 9){
				//display = "<center>" + "0" + minutes.toString() + "</center>";
				display = "0" + minutes.toString();
			} else {
				//display =  "<center>" + minutes.toString() + "</center>";
				display =  minutes.toString();
			}
			return display;
		}
	
	

}
