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
	
	NavigationButton createusers =
			new NavigationButton("Create users", new CreateUserView());
	createusers.setTargetViewCaption("Create users");
	
	NavigationButton editusers =
			new NavigationButton("Edit users", new UserEditView());
	editusers.setTargetViewCaption("Edit users");
	
	
	NavigationButton createguests =
			new NavigationButton("Create guests", new CreateGuestView());
	createguests.setTargetViewCaption("Create guests");
	
	NavigationButton editguests =
			new NavigationButton("Edit guests", new GuestEditView());
	editguests.setTargetViewCaption("Edit guests");
	
	NavigationButton editmeals =
			new NavigationButton("Meals", new MealEditView());
	editmeals.setTargetViewCaption("Meals");
	
	//NavigationButton contractsedit =
	//		new NavigationButton("Contracts", new ContractEditView());
	//contractsedit.setTargetViewCaption("Contract");
	
	
	/**
	NavigationButton checks =
			new NavigationButton("Checks", new DatabaseCheckView());
	editmeals.setTargetViewCaption("Database checks");
	*/
	
	
	
	layout.addComponent(mealcount);
	layout.addComponent(createusers);
	layout.addComponent(editusers);
	layout.addComponent(createguests);
	layout.addComponent(editguests);
	layout.addComponent(editmeals);
	//layout.addComponent(contractsedit);
	//layout.addComponent(checks);
	
	
	setContent(layout);
	}

}