package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class CookMenuView extends NavigationView {
	
	public CookMenuView(){	
		//CssLayout content = new CssLayout();
		//content.setWidth("100%");
		setCaption("Cook menu");
			
		VerticalComponentGroup layout = new VerticalComponentGroup();
		layout.setSizeFull();
		//componentGroup.setCaption("Menu");

		Button logout = new Button();
		logout.setCaption("Exit");
		setRightComponent(logout);
		logout.addClickListener(new ExitBehavior());
		
		Button fake = new Button();
		fake.setCaption("");
		setLeftComponent(fake);
		logout.addClickListener(new ExitBehavior());
		
	     
	  
		NavigationButton mealcount = 
				new NavigationButton("Meal count", new MealCountSwipeView(false));
		mealcount.setTargetViewCaption("Meal count");
		
		
		layout.addComponent(mealcount);
		
		
		setContent(layout);
		}

	}