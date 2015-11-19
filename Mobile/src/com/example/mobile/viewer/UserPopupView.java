package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserPopupView extends NavigationView {
	
	public UserPopupView(Table userstable){
		setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		userstable.setVisibleColumns(new Object[] {"name", "surname"});
		userstable.setPageLength(userstable.size());
		layout.addComponent(userstable);
		setContent(layout);
	}
}
