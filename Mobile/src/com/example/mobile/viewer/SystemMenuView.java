package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;


@SuppressWarnings("serial")
public class SystemMenuView extends NavigationView {

	public SystemMenuView(){	

		setCaption("System menu");

		VerticalComponentGroup layout = new VerticalComponentGroup();
		layout.setSizeFull();
		
		Button logout = new Button();
		logout.setCaption("Exit");
		setRightComponent(logout);
		logout.addClickListener(new ExitBehavior());

		Button fake = new Button();
		fake.setCaption("");
		setLeftComponent(fake);


		NavigationButton checks =
				new NavigationButton("Checks", new DatabaseCheckView());
		checks.setTargetViewCaption("Database checks");
		layout.addComponent(checks);

		setContent(layout);
	}

}