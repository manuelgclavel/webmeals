package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Role;
import com.example.mobile.data.User;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class CreateUserView extends NavigationView {
	MobileUI ui = (MobileUI) UI.getCurrent();
	
	private final TextField name = new TextField("Name: ");
	private final TextField surname = new TextField("Surname: ");
	private final TextField login = new TextField("Login: ");
	private final TextField password = new TextField("Password: ");
	private final NativeSelect role = new NativeSelect("Role: ");
	
	private BeanItemContainer<User> users;

	public CreateUserView() {

		Button logout = new Button();
		logout.setCaption("Exit");
		setRightComponent(logout);
		logout.addClickListener(new ExitBehavior());
		
		Button back = new Button();
		back.setCaption("Admin menu");
		setLeftComponent(back);
		back.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new AdminMenuView());
			} });
		
		VerticalComponentGroup content = new VerticalComponentGroup();
		
	
		
		
		BeanItemContainer<Role> roles = new BeanItemContainer<Role>(Role.class);
		ui.populateRoles(roles);
		role.setContainerDataSource(roles);
		role.setItemCaptionPropertyId("literal");
		
		
		users = new BeanItemContainer<User>(User.class);
		ui.populateUsers(ui.getConnectionPool(), users);
		
		content.addComponent(name);
		content.addComponent(surname);
		content.addComponent(login);
		content.addComponent(password);
		content.addComponent(role);
		
		Button createButton = new Button("Add");
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(createButton);
    	content.addComponent(buttons);
  
    	setContent(content);
    	
    
    					
    	createButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (role.getValue() == null){
					Notification.show("Please, select a role");
					//role.setComponentError(new UserError("No role"));
				} else {
					if (!(ui.existsLogin(users, login.getValue()))){
						ui.createUser(name.getValue(), surname.getValue(), 
								login.getValue(), password.getValue(), 
								((Role) role.getValue()).getId(), ui.getResidence().getPk());
						Notification.show("DONE");
						initialize();
					} else {
						//login.setComponentError(new UserError("Bad login"));
						Notification.show("Login already exists. Please, introduce a different one.");
					}
				}
				
			}});
    	
		
		
		
	}
	
	public void initialize(){
		name.setValue("");
		surname.setValue("");
		login.setValue("");
		password.setValue("");
		role.setValue(null);
	}

}
