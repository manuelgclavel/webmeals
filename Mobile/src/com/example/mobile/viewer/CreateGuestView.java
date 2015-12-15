package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Guest;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class CreateGuestView extends NavigationView {
	MobileUI ui = (MobileUI) UI.getCurrent();
	
	private final TextField name = new TextField("Name: ");
	private final TextField surname = new TextField("Surname: ");
	
	private BeanItemContainer<Guest> guests;

	public CreateGuestView() {

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
		
		
		guests = new BeanItemContainer<Guest>(Guest.class);
		ui.populateGuests(ui.getConnectionPool(), guests);
		
		content.addComponent(name);
		content.addComponent(surname);
		
		Button createButton = new Button("Add");
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(createButton);
    	content.addComponent(buttons);
  
    	setContent(content);
    	
    
    					
    	createButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!(ui.existsGuest(guests, name.getValue(), surname.getValue()))){
						ui.createGuest(name.getValue(), surname.getValue(), 
								ui.getResidence().getPk());
						Notification.show("DONE");
						initialize();
					} else {
						//login.setComponentError(new UserError("Bad login"));
						Notification.show("A guest already exists with identical name/surname. Please, introduce a different name/surname.");
					}
				
			}});
    	
		
		
		
	}
	
	public void initialize(){
		name.setValue("");
		surname.setValue("");
	}

}
