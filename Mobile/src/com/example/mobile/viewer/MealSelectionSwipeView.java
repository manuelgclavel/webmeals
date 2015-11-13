package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class MealSelectionSwipeView extends NavigationManager 
implements NavigationManager.NavigationListener {

	private Date selected;

	public MealSelectionSwipeView() {
		// Set up the initial views
		Calendar c = Calendar.getInstance();
		//this.selected = c.getTime();
		Date today;
		Date tomorrow;

		navigateTo(new MealSelectionDateView(c.getTime()));
		c.add(Calendar.DATE, 1);
		setNextComponent(new MealSelectionDateView(c.getTime()));
		addNavigationListener(this);			
	}


	@Override
	public void navigate(NavigationEvent event) {
		switch (event.getDirection()) {
		case FORWARD: {
			Calendar c = new GregorianCalendar();
			c.setTime(((MealSelectionDateView) event.getComponent()).getSelected());
			c.add(Calendar.DATE, +1);
			setNextComponent(new MealSelectionDateView(c.getTime())); 
		}
		case BACK:{
			Calendar c = new GregorianCalendar();
			c.setTime(((MealSelectionDateView) event.getComponent()).getSelected());
			c.add(Calendar.DATE, -1);
			setPreviousComponent(new MealSelectionDateView(c.getTime())); 
		}

		}
	}
}