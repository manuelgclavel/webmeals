package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.User;
import com.example.mobile.presenter.ChangePasswordBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ChangePasswordView extends NavigationView {
	
	public ChangePasswordView(){
		
		setCaption("Change password");
		
		VerticalComponentGroup layout = new VerticalComponentGroup();
		layout.setSizeFull();
		
		// Have a bean
		User user = ((MobileUI) UI.getCurrent()).getUser();
		
		TextField newpassword = new TextField("Type new password");
		TextField confirmnewpassword = new TextField("Re-type new password");
		Button change = new Button("OK");
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(change);
    	
    	change.addClickListener(new ChangePasswordBehavior(newpassword, confirmnewpassword));
    	
    	
    	layout.addComponent(newpassword);
    	layout.addComponent(confirmnewpassword);
    	layout.addComponent(buttons);
    	
    	
    	setContent(layout);
    	
		        
		// Form for editing the bean
		//final BeanFieldGroup<User> binder =
		//        new BeanFieldGroup<User>(User.class);
		//binder.setItemDataSource(user);
		//layout.addComponent(binder.buildAndBind("Name", "name"));
		//layout.addComponent(binder.buildAndBind("Surname", "surname"));
		//layout.addComponent(binder.buildAndBind("Login", "login"));
		//layout.addComponent(binder.buildAndBind("Password", "password"));
		// Buffer the form content
		//binder.setBuffered(true);
		//layout.addComponent(new Button("OK"));
		
		
	}

}
