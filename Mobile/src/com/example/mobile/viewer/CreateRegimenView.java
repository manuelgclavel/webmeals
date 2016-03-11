package com.example.mobile.viewer;

import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Guest;
import com.example.mobile.data.Residency;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class CreateRegimenView extends NavigationView {
	MobileUI ui = (MobileUI) UI.getCurrent();
	
	private final TextField name;
	private final TextField description;
	private final Button addregimen;
	private final Button updateregimen;
	
	private BeanItemContainer<FoodRegime> regimens = new BeanItemContainer<FoodRegime>(FoodRegime.class);
	private final Table regimenstable;
	

	public CreateRegimenView() {

		
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
		
		
		Label message = new Label("To edit an existing regimen, simply select it on the table below.");
		content.addComponent(message);
		
    	
		name = new TextField("Name: ");
		name.setSizeFull();
		name.setRequired(true);
		name.addValidator(new StringLengthValidator("Name cannot be empty", 1, 100, false));
		
		
		description = new TextField("Description: ");
		description.setSizeFull();
		description.setRequired(true);
		description.addValidator(new StringLengthValidator("Description cannot be empty", 1, 100, false));
	
		regimenstable = new Table();
		
		addregimen = new Button("Add");
		updateregimen = new Button("Update");
		updateregimen.setEnabled(false);
		
				
		initialize();
	
		content.addComponent(regimenstable);
		content.addComponent(name);
		content.addComponent(description);
		content.addComponent(addregimen);
		content.addComponent(updateregimen);
		
		
		regimenstable.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				if (event.getProperty().getValue() == null){
					name.setValue("");
					description.setValue("");
					addregimen.setEnabled(true);
					updateregimen.setEnabled(false);
				} else {
					name.setValue(((FoodRegime) event.getProperty().getValue()).getName());
					description.setValue(((FoodRegime) event.getProperty().getValue()).getDescription());
					addregimen.setEnabled(false);
					updateregimen.setEnabled(true);
				}
			}});
    	
  
    	setContent(content);
    	
    
    					
    	addregimen.addClickListener(new ClickListener(){
    		@Override
    		public void buttonClick(ClickEvent event) {
    			// TODO Auto-generated method stub
    			if (name.isValid()){
    				if (description.isValid()) {
    					if (!(ui.existsRegimen(regimens, name.getValue()))){
    						ui.createRegimen(name.getValue(), description.getValue(), 
    								ui.getResidence().getPk());
    						Notification.show("DONE");
    						initialize();
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
    	
    	updateregimen.addClickListener(new ClickListener(){

    		@Override
    		public void buttonClick(ClickEvent event) {
    			// TODO Auto-generated method stub
    			FoodRegime selectedregime = ((FoodRegime) regimenstable.getValue());
    			if (name.isValid()){
    				if (description.isValid()) {
    					if (name.getValue().equals(selectedregime.getName())){
    						ui.updateRegimen(selectedregime.getPk(), name.getValue(), description.getValue());
    						Notification.show("DONE");
    						initialize();
    					} else {
    						if (!(ui.existsRegimen(regimens, name.getValue()))){
    							ui.updateRegimen(selectedregime.getPk(), name.getValue(), description.getValue());
    							Notification.show("DONE");
    							initialize();
    						} else {
    							Notification.show("A regimen already exists with identical name. Please, introduce a different name.");
    						}
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
	
	public void initialize(){
		name.setValue("");
		description.setValue("");
		
		regimenstable.removeAllItems();
		ui.populateRegimens(ui.getConnectionPool(), regimens);
		regimenstable.setContainerDataSource(regimens);
		regimenstable.setSortContainerPropertyId("name");
		regimenstable.setSortAscending(true);
		regimenstable.setSelectable(true);
		regimenstable.setVisibleColumns(new Object[] {"name","description"});
		regimenstable.setPageLength(regimens.size());
		regimenstable.setSizeFull();
	}

}
