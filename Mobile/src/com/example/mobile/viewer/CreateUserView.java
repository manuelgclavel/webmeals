package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Role;
import com.example.mobile.data.User;
import com.example.mobile.data.Residency;

import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class CreateUserView extends NavigationView {
	MobileUI ui = (MobileUI) UI.getCurrent();
	
	private final TextField name;
	private final TextField surname;
	private final TextField login;
	//private final TextField password = new TextField("Password: ");
	private final NativeSelect role;
	private final CheckBox filter;
	
	private Table userstable;
	private BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);
	private BeanItemContainer<Residency> periods = new BeanItemContainer<Residency>(Residency.class);

	public CreateUserView() {
		
		ui.populateUsers(ui.getConnectionPool(), users);
		ui.populateResidency(ui.getConnectionPool(), periods);
		

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
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		
		Label message1 = new Label("To create a user, fill the following fields and click the bottom 'Add' below.");
		content.addComponent(message1);
		
		
		name = new TextField("Name: ");
		name.setSizeFull();
		surname = new TextField("Surname: ");
		surname.setSizeFull();
		login = new TextField("Login: ");
		login.setSizeFull();
		role = new NativeSelect("Role: ");
		role.setSizeFull();
		
		
		content.addComponent(name);
		content.addComponent(surname);
		content.addComponent(login);
		content.addComponent(role);
		
		Button createButton = new Button("Add");
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(createButton);
    	content.addComponent(buttons);
  
		
    	
		Label message2 = new Label("To edit an existing user, simply select it on the table below.");
		content.addComponent(message2);
		
		filter = new CheckBox("Only current residents", true);
		
		userstable = new Table("");
		initializeuserstable(ui.filterOnlyCurrentUsers(users,periods));
		
		
		filter.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				if (!(filter.getValue())){
					initializeuserstable(users);
				} else {
					initializeuserstable(ui.filterOnlyCurrentUsers(users,periods));
				}
			}});
		
		content.addComponent(filter);
		BeanItemContainer<Role> roles = new BeanItemContainer<Role>(Role.class);
		ui.populateRoles(roles);
		role.setContainerDataSource(roles);
		role.setItemCaptionPropertyId("literal");
		
		
		
		

		
		content.addComponent(userstable);
		
		userstable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				User userselected = (User) event.getItemId();
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new EditUserView(userselected));
			}});
		
		
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
								login.getValue(), "", 
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
	
	private void initialize(){
		name.setValue("");
		surname.setValue("");
		login.setValue("");
		//password.setValue("");
		role.setValue(null);
	}
	
	private void initializeuserstable(BeanItemContainer<User> users){
		userstable.removeAllItems();
		userstable.setContainerDataSource(users);
		userstable.setSelectable(true);
		userstable.setVisibleColumns("surname", "name", "login");
		userstable.sort(new Object[] {"surname", "name"}, new boolean[] {true, true});
		userstable.setPageLength(userstable.size());
		//userstable.setPageLength(10);
		userstable.setSizeFull();
		
	}

}
