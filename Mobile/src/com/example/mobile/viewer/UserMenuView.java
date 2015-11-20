package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
//public class UserMenuView  extends VerticalComponentGroup {
public class UserMenuView extends NavigationView {
	
	public UserMenuView(){

			//CssLayout content = new CssLayout();
			//content.setWidth("100%");
			setCaption("Menu");
			setSizeFull();
			VerticalComponentGroup content = new VerticalComponentGroup();
			
			//componentGroup.setCaption("Menu");

			//NavigationBar top = new NavigationBar();
			Button logout = new Button();
			logout.setCaption("Exit");
			setRightComponent(logout);
			logout.addClickListener(new ExitBehavior());
			
			Button fake = new Button();
			fake.setCaption("");
			setLeftComponent(fake);
			logout.addClickListener(new ExitBehavior());
			//addComponent(top);
			
		     
		     
			//NavigationButton mealdailyselection = new NavigationButton("Meal selection (by day)", new MealSelectionSwipeView(Calendar.getInstance().getTime()));
			//content.addComponent(mealdailyselection);
			
			NavigationButton dailymeal = 
					new NavigationButton(new MealSelectionSwipeView(Calendar.getInstance().getTime()));
			dailymeal.setTargetViewCaption("Meal selection (by day)");
			NavigationButton weeklymeal = 
					new NavigationButton(new SelectionWeekView(Calendar.getInstance().getTime())); 
			weeklymeal.setTargetViewCaption("Meal selection (by week)");
			content.addComponent(dailymeal);
			content.addComponent(weeklymeal);
			
			setContent(content);
			
			
	}

}


