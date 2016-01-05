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
public class UserMenuView extends NavigationView {
	
	public UserMenuView(){

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
							new MealSelectionSwipeView(
									((MobileUI) UI.getCurrent()).getUser().getPk(),
									1));
			dailymeal.setTargetViewCaption("Meal selection (by day)");
			NavigationButton weeklymeal = 
					new NavigationButton(new SelectionWeekView()); 
					//new NavigationButton(new SelectionWeekView()); 
			weeklymeal.setTargetViewCaption("Meal selection (by week)");
			
			NavigationButton changepassword = 
					new NavigationButton(new ChangePasswordView()); 
			changepassword.setTargetViewCaption("Change password");
			
			
			
			content.addComponent(dailymeal);
			content.addComponent(weeklymeal);
			content.addComponent(changepassword);
			
			setContent(content);
			
			
	}

}


