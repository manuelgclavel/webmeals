package com.example.mobile.viewer;

import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;

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
			new NavigationButton("Meal count", new MealCountSwipeView(true));
	mealcount.setTargetViewCaption("Meal count");
	
	NavigationButton createusers =
			new NavigationButton("Users", new CreateUserView());
	createusers.setTargetViewCaption("Users");
	
	//NavigationButton editusers =
	//		new NavigationButton("Edit users", new UserEditView());
	//editusers.setTargetViewCaption("Edit users");
	
	
	NavigationButton createguests =
			new NavigationButton("Create guests", new CreateGuestView());
	createguests.setTargetViewCaption("Create guests");
	
	NavigationButton editguests =
			new NavigationButton("Edit guests", new GuestEditView());
	editguests.setTargetViewCaption("Edit guests");
	
	NavigationButton editmeals =
			new NavigationButton("Meals", new MealEditView());
	editmeals.setTargetViewCaption("Meals");
	
	NavigationButton createregimens =
			new NavigationButton("Regimens", new CreateRegimenView());
	editmeals.setTargetViewCaption("Regimens");
	
	
	NavigationButton contractsedit =
			new NavigationButton("Contracts", new ContractsEditView());
	contractsedit.setTargetViewCaption("Contract");
	
	NavigationButton summaries =
			new NavigationButton("Summaries", new SummariesView());
	contractsedit.setTargetViewCaption("Summaries");
	
	
	//NavigationButton checks =
	//		new NavigationButton("Checks", new DatabaseCheckView());
	//editmeals.setTargetViewCaption("Database checks");
	
	
	
	
	layout.addComponent(mealcount);
	layout.addComponent(createusers);
	//layout.addComponent(editusers);
	layout.addComponent(createguests);
	layout.addComponent(editguests);
	layout.addComponent(editmeals);
	layout.addComponent(createregimens);
	layout.addComponent(contractsedit);
	layout.addComponent(summaries);
	//layout.addComponent(checks);
	
	
	setContent(layout);
	}

}