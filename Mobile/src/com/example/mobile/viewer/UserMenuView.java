package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
public class UserMenuView  extends VerticalComponentGroup {

	public UserMenuView(){

		//CssLayout content = new CssLayout();
		//content.setWidth("100%");
		this.setSizeFull();
		this.setCaption("Menu");

		//VerticalComponentGroup componentGroup = new VerticalComponentGroup();
		//componentGroup.setCaption("Menu");

		NavigationButton mealcount = new NavigationButton("Meal selection");
		mealcount.addClickListener(new NavigationButtonClickListener(){

			@Override
			public void buttonClick(NavigationButtonClickEvent event) {
				// TODO Auto-generated method stub
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new MealSelectionSwipeView(Calendar.getInstance().getTime()));
				//setContent(new MealCountView());
			}});

		addComponent(mealcount);

		/** */
		Button logout = new Button("Logout");
		logout.addClickListener(new ExitBehavior());
		addComponent(logout);

		/** */

	}

}


