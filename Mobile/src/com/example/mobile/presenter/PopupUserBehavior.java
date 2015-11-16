package com.example.mobile.presenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.mobile.MobileUI;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class PopupUserBehavior implements ClickListener {
	private BeanItemContainer<User> users;

	public PopupUserBehavior(BeanItemContainer<User> users) {
		this.users = users;
	}
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub

	
			// Show it relative to the navigation bar of
			// the current NavigationView.
			CssLayout layout = new CssLayout();
			//Label test = new Label(mealoption.getLiteral());
			//Label num = new Label(Integer.valueOf(mealoption.getPk()).toString());
			//Label count = new Label(Integer.valueOf(i))
			
			Table userstable = new Table("", users);
			userstable.setPageLength(userstable.size());
			userstable.setVisibleColumns(new Object[] {"name", "surname"});
			Popover popover = new Popover();
			//layout.addComponent(test);
			//layout.addComponent(num);
			
			layout.addComponent(userstable);
			popover.setContent(layout);   
			popover.showRelativeTo(event.getButton());
	}
}
