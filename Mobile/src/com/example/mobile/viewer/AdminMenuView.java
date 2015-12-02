package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class AdminMenuView extends NavigationView {
	
	public AdminMenuView(){	
	//CssLayout content = new CssLayout();
	//content.setWidth("100%");
	setCaption("Admin menu");
		
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
			new NavigationButton("Meal count", new MealCountSwipeView(Calendar.getInstance().getTime()));
	mealcount.setTargetViewCaption("Meal count");
	NavigationButton editusers =
			new NavigationButton("Users", new UserEditView());
	editusers.setTargetViewCaption("Users");
	
	NavigationButton editguests =
			new NavigationButton("Guests", new GuestEditView());
	editguests.setTargetViewCaption("Guests");
	
	NavigationButton editmeals =
			new NavigationButton("Meals", new MealEditView());
	editmeals.setTargetViewCaption("Meals");
	
	NavigationButton checks =
			new NavigationButton("Checks", new DatabaseCheckView());
	editmeals.setTargetViewCaption("Database checks");
	
	
	
	layout.addComponent(mealcount);
	layout.addComponent(editusers);
	layout.addComponent(editguests);
	layout.addComponent(editmeals);
	layout.addComponent(checks);
	
	
	setContent(layout);
	}

}