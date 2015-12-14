package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class GuestMenuView extends NavigationView {
	
	public GuestMenuView(int guestpk){

			setCaption("Menu");
			setSizeFull();
			VerticalComponentGroup content = new VerticalComponentGroup();
			
			Button logout = new Button();
			logout.setCaption("Exit");
			setRightComponent(logout);
			logout.addClickListener(new ExitBehavior());
			
			Button fake = new Button();
			fake.setCaption("");
			setLeftComponent(fake);
			logout.addClickListener(new ExitBehavior());
			
			NavigationButton dailymeal = 
					new NavigationButton(
							new MealSelectionSwipeView(Calendar.getInstance().getTime(), 
									guestpk, 2));
			dailymeal.setTargetViewCaption("Meal selection (by day)");
			//NavigationButton weeklymeal = 
			//		new NavigationButton(new SelectionWeekView(Calendar.getInstance().getTime())); 
			//weeklymeal.setTargetViewCaption("Meal selection (by week)");
			
			
			
			content.addComponent(dailymeal);
			//content.addComponent(weeklymeal);
		
			setContent(content);
				
	}
}
