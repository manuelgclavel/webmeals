package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
public class UserMenuView  extends NavigationView {

	public UserMenuView(){

			//CssLayout content = new CssLayout();
			//content.setWidth("100%");
			this.setCaption("Menu");
				
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
			
		     
		     
			NavigationButton mealselection = new NavigationButton("Meal selection");
			mealselection.addClickListener(new NavigationButtonClickListener(){

				@Override
				public void buttonClick(NavigationButtonClickEvent event) {
					// TODO Auto-generated method stub
					((MobileUI) UI.getCurrent()).getManager().navigateTo(new MealSelectionSwipeView(Calendar.getInstance().getTime()));
					//setContent(new MealCountView());
				}});
			
			layout.addComponent(mealselection);
			
			setContent(layout);
			
	}

}


