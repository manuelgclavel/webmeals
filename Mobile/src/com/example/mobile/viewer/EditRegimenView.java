package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Guest;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class EditRegimenView extends NavigationView {

	private final MobileUI  ui = (MobileUI) UI.getCurrent();
	private final JDBCConnectionPool connectionPool = ui.getConnectionPool();

	private BeanItemContainer<FoodRegime> regimens;
	private final TextField name;
	private final TextField description;
	
	public EditRegimenView(final FoodRegime selected) {
		
		setCaption("Edit Regimen");

		Button logout = new Button();
		logout.setCaption("Exit");
		setRightComponent(logout);
		logout.addClickListener(new ExitBehavior());
		
		Button back = new Button();
		back.setCaption("Back");
		setLeftComponent(back);
		back.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new CreateRegimenView());
			} });
		
		
		VerticalLayout content = new VerticalLayout();	
		content.setMargin(true);
		content.setSpacing(true);
		
		regimens = new BeanItemContainer<FoodRegime>(FoodRegime.class);
		ui.populateRegimens(ui.getConnectionPool(), regimens);
		

		name = new TextField("Name: ");
		if (!(selected.getName() == null)){
			name.setValue(selected.getName());
		} else {
			name.setValue("");
		}
		name.setSizeFull();
		name.setRequired(true);
		name.addValidator(new StringLengthValidator("Name cannot be empty", 1, 100, false));
		
		
		description = new TextField("Description: ");
		if (!(selected.getDescription()== null)){
			description.setValue(selected.getDescription());
		} else {
			description.setValue("");
		}
		description.setSizeFull();
		description.setRequired(true);
		description.addValidator(new StringLengthValidator("Description cannot be empty", 1, 100, false));
		
		
		content.addComponent(name);
		content.addComponent(description);
		
		Button updateButton = new Button("Update");
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(updateButton);
    	content.addComponent(buttons);
  
    	setContent(content);
    	
    
    					
    	updateButton.addClickListener(new ClickListener(){

    		@Override
    		public void buttonClick(ClickEvent event) {
    			// TODO Auto-generated method stub
    			if (name.isValid()){
    				if (description.isValid()) {
    					if (!(ui.existsRegimen(regimens, name.getValue()))){
    						ui.updateRegimen(selected.getPk(), name.getValue(), description.getValue());
    						Notification.show("DONE");
    						((MobileUI) UI.getCurrent()).getManager().navigateTo(new CreateRegimenView());
    					} else {
    						Notification.show("A regimen already exists with identical name. Please, introduce a different name.");
    					}
    				} else {
    					Notification.show("Please, introduce a non-empty description");
    				}
    			} else {
    				//login.setComponentError(new UserError("Bad login"));
    				Notification.show("Please, introduce a non-empty name");
    			}

    		}});
    	
    	
	}
	
	
}