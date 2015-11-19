package com.example.mobile.presenter;

import com.vaadin.ui.VerticalLayout;
import com.example.mobile.MobileUI;
import com.example.mobile.data.User;
import com.example.mobile.viewer.UserPopupView;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class UserPopupBehavior implements ClickListener {
	
	private BeanItemContainer<User> users;
	
	public UserPopupBehavior(BeanItemContainer<User> users)  {
		this.users= users;
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		((MobileUI) UI.getCurrent()).getManager().navigateTo(new UserPopupView(new Table("", users)));
		
	}

}
