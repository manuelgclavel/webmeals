package com.example.mobile.viewer;

import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.MealSelectionPlus;
import com.example.mobile.data.Residency;
import com.example.mobile.data.User;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;

@SuppressWarnings("serial")
public class EditUserView extends NavigationView {

	private final MobileUI  ui = (MobileUI) UI.getCurrent();
	private final JDBCConnectionPool connectionPool = ui.getConnectionPool();

	private User selected;
	private BeanItemContainer<User> users;
	private final TextField name;
	private final TextField surname;
	private final TextField login;
	//private final TextField password = new TextField("Password: ");
	//private final NativeSelect role;
	
	private Table periodstable = new Table();
	private Table regimenstable = new Table();
	private BeanItemContainer<Residency> contractperiods = new BeanItemContainer<Residency>(Residency.class);
	private BeanItemContainer<Residency> regimeperiods = new BeanItemContainer<Residency>(Residency.class);
	private final BeanItemContainer<MealSelectionPlus> mealselections = new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
	
	
	private final BeanItemContainer<FoodRegime> regimens = new BeanItemContainer<FoodRegime>(FoodRegime.class);
	private final BeanItemContainer<Contract> contracts = new BeanItemContainer<Contract>(Contract.class);
	
	
	private DateField startnewcontract;
	private DateField endnewcontract;
	private NativeSelect contractscombo;
	private Button addcontract;
	private Button updatecontract;
	
	private DateField startnewregimen;
	private DateField endnewregimen;
	private NativeSelect regimenscombo;
	private Button addregimen;
	private Button updateregimen;
	
	public EditUserView(User user) {
		
		this.selected = user;
		setCaption("Edit User");

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
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new CreateUserView());
			} });
		
		
		VerticalLayout content = new VerticalLayout();	
		content.setMargin(true);
		content.setSpacing(true);
		
		users = new BeanItemContainer<User>(User.class);
		ui.populateUsers(ui.getConnectionPool(), users);
		

		name = new TextField("Name: ");
		if (!(selected.getName() == null)){
			name.setValue(selected.getName());
		} else {
			name.setValue("");
		}
		name.setSizeFull();
		name.setRequired(true);
		name.addValidator(new StringLengthValidator("Name cannot be empty", 1, 100, false));
		
		surname = new TextField("Surname: ");
		if (!(selected.getSurname() == null)){
			surname.setValue(selected.getSurname());
		} else {
			surname.setValue("");
		}
		surname.setSizeFull();
		surname.setRequired(true);
		surname.addValidator(new StringLengthValidator("Surname cannot be empty", 1, 100, false));
		
		login = new TextField("Login: ");
		if (!(selected.getSurname() == null)){
			login.setValue(selected.getLogin());
		} else {
			login.setValue("");
		}
		login.setSizeFull();
		login.setRequired(true);
		login.addValidator(new StringLengthValidator("Login cannot be empty", 1, 100, false));
		
		
		
		
		content.addComponent(name);
		content.addComponent(surname);
		content.addComponent(login);
		
		
		Button updateButton = new Button("Update");
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(updateButton);
    	content.addComponent(buttons);
    	
		initializeContracts(periodstable);
    	content.addComponent(periodstable);
		
    	// ADD CONTRACT   	
    	startnewcontract = new DateField("Start: ");
    	startnewcontract.setRequired(true);
    	endnewcontract = new DateField("End: "); 
    	endnewcontract.setRequired(true);
    	
    	ui.populateContracts(connectionPool, contracts);
    	contractscombo = new NativeSelect("Contract");
    	contractscombo.setContainerDataSource(contracts);
    	contractscombo.setItemCaptionPropertyId("name");
    	contractscombo.setRequired(true);
    	addcontract = new Button("Add");
    	updatecontract = new Button("Update");
    	updatecontract.setEnabled(false);
	
    	content.addComponent(startnewcontract);
    	content.addComponent(endnewcontract);
    	content.addComponent(contractscombo);
    	content.addComponent(addcontract);
    	content.addComponent(updatecontract);
    	//
    	
    	initializeRegimes(regimenstable);
    	content.addComponent(regimenstable);
    	
    	startnewregimen = new DateField("Start: ");
    	startnewregimen.setRequired(true);
    	endnewregimen = new DateField("End: "); 
    	endnewregimen.setRequired(true);
    	
    	ui.populateFoodRegimes(connectionPool, regimens);
    	regimenscombo = new NativeSelect("Regime");
    	regimenscombo.setContainerDataSource(regimens);
    	regimenscombo.setItemCaptionPropertyId("name");
    	regimenscombo.setRequired(true);
    	
		addregimen = new Button("Add");
		updateregimen = new Button("Update");
		updateregimen.setEnabled(false);
	
    	content.addComponent(startnewregimen);
    	content.addComponent(endnewregimen);
    	content.addComponent(regimenscombo);
    	content.addComponent(addregimen);
    	content.addComponent(updateregimen);
    	
    	regimenstable.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				if (event.getProperty().getValue() == null){
					startnewregimen.setValue(null);
					endnewregimen.setValue(null);
					regimenscombo.setValue(null);
					addregimen.setEnabled(true);
					updateregimen.setEnabled(false);
				} else {
					startnewregimen.setValue(((Residency) event.getProperty().getValue()).getStart());
					endnewregimen.setValue(((Residency) event.getProperty().getValue()).getEnd());
					FoodRegime regime;
					int selectedregime = ((Residency) event.getProperty().getValue()).getRegime();
					for (Iterator<FoodRegime> r  = regimens.getItemIds().iterator(); r.hasNext();){
						regime = r.next();
						if (regime.getPk() == selectedregime){
							regimenscombo.setValue(regime);
							break;
						}
					}
					addregimen.setEnabled(false);
					updateregimen.setEnabled(true);
				}
			}});
    	
    	addcontract.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
				if (startnewcontract.isValid()){
					if (endnewcontract.isValid()){
						if (contractscombo.isValid()){
							if (!(startnewcontract.getValue().after(endnewcontract.getValue()))){
								if (!(ui.periodOverlaps(startnewcontract.getValue(), 
										endnewcontract.getValue(), ui.filteredByUserWithContract(contractperiods,selected)))){
									ui.insertContractPeriod(startnewcontract.getValue(), 
											endnewcontract.getValue(),
											(Contract) contractscombo.getValue(),
											selected);			
									Notification.show("DONE");
									startnewcontract.setValue(null);
									endnewcontract.setValue(null);
									contractscombo.setValue(null);
									initializeContracts(periodstable);
									
								} else {
									Notification.show("The selected period overlaps with existing contract periods for this resident.");
								}
							} else {
								Notification.show("Empty contract period. Please, change either the starting date or the ending date.");
							}					
						} else {
							Notification.show("Please, select a contract.");
						}					

					} else {
						Notification.show("Please, select the ending date.");
					}

				} else {
					Notification.show("Please, select the starting date.");
				}

			}});

    	
   
    	addregimen.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
				if (startnewregimen.isValid()){
					if (endnewregimen.isValid()){
						if (regimenscombo.isValid()){
							if (!(startnewregimen.getValue().after(endnewregimen.getValue()))){
								if (!(ui.periodOverlaps(startnewregimen.getValue(), 
										endnewregimen.getValue(), ui.filteredByUserWithRegime(regimeperiods,selected)))){
									// update meal selections of the new given period with new regime
									mealselections.removeAllItems();
									ui.populateUserPeriodMealSelections(connectionPool, mealselections,
											startnewregimen.getValue(), endnewregimen.getValue(), selected);
									ui.updateRegimeMealSelection(mealselections,
											(FoodRegime) regimenscombo.getValue());
									// update residency with new regime period
									ui.insertRegimenPeriod(startnewregimen.getValue(), 
											endnewregimen.getValue(),
											(FoodRegime) regimenscombo.getValue(),
											selected);			
									Notification.show("DONE");
									startnewregimen.setValue(null);
									endnewregimen.setValue(null);
									regimenscombo.setValue(null);
									initializeRegimes(regimenstable);
									
								} else {
									Notification.show("The selected period overlaps with existing regime periods for this resident.");
								}
							} else {
								Notification.show("Empty date period. Please, change either the starting date or the ending date.");
							}					
						} else {
							Notification.show("Please, select a regimen.");
						}					

					} else {
						Notification.show("Please, select the ending date.");
					}

				} else {
					Notification.show("Please, select the starting date.");
				}

			}});

    	updateregimen.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
				Residency selectedperiod = ((Residency) regimenstable.getValue());
				if (startnewregimen.isValid()){
					if (endnewregimen.isValid()){
						if (regimenscombo.isValid()){
							if (!(startnewregimen.getValue().after(endnewregimen.getValue()))){
								if (!(ui.periodOverlaps(startnewregimen.getValue(), 
										endnewregimen.getValue(), 
										ui.filteredByUserWithRegimeMinus(regimeperiods,selected, selectedperiod)))){
									// obtain meal selections for the selected period
									mealselections.removeAllItems();
									ui.populateUserPeriodMealSelections(connectionPool, mealselections,
											selectedperiod.getStart(), selectedperiod.getEnd(),selected);
									ui.updateRegimeMealSelection(mealselections, null);
									// update meal selections of the new given period with new regime
									mealselections.removeAllItems();
									ui.populateUserPeriodMealSelections(connectionPool, mealselections,
											startnewregimen.getValue(), endnewregimen.getValue(), selected);
									ui.updateRegimeMealSelection(mealselections,
											(FoodRegime) regimenscombo.getValue());
									// update residency with new regime period
									ui.updateRegimenPeriod(startnewregimen.getValue(), 
											endnewregimen.getValue(),
											(FoodRegime) regimenscombo.getValue(),
											selectedperiod);			
									//Notification.show("DONE");
									startnewregimen.setValue(null);
									endnewregimen.setValue(null);
									regimenscombo.setValue(null);
									initializeRegimes(regimenstable);
									
								} else {
									Notification.show("The selected period overlaps with existing regime periods for this resident.");
								}
							} else {
								Notification.show("Empty date period. Please, change either the starting date or the ending date.");
							}					
						} else {
							Notification.show("Please, select a regimen.");
						}					

					} else {
						Notification.show("Please, select the ending date.");
					}

				} else {
					Notification.show("Please, select the starting date.");
				}

			}});

    	
    	setContent(content);
    	
    
    					
    	updateButton.addClickListener(new ClickListener(){

    		@Override
    		public void buttonClick(ClickEvent event) {
    			// TODO Auto-generated method stub
    			if (name.isValid()){
    				if (surname.isValid()) {
    					if (!(ui.existsLogin(users, login.getValue()))){
    						ui.updateUser(selected.getPk(), name.getValue(), surname.getValue(), login.getValue());
    						Notification.show("DONE");
    						//((MobileUI) UI.getCurrent()).getManager().navigateTo(new CreateRegimenView());
    					} else {
    						Notification.show("A user already exists with identical login. Please, introduce a different login.");
    					}
    				} else {
    					Notification.show("Please, introduce a non-empty surnam");
    				}
    			} else {
    				//login.setComponentError(new UserError("Bad login"));
    				Notification.show("Please, introduce a non-empty name");
    			}

    		}});
    	
    	
	}

	
	

	private void initializeContracts(Table periodstable) {
		// TODO Auto-generated method stub
		contractperiods.removeAllItems();
		ui.populateResidency(ui.getConnectionPool(), contractperiods);
    	periodstable.setContainerDataSource(ui.filteredByUserWithContract(contractperiods,selected));
    	periodstable.setSelectable(true);
		periodstable.setVisibleColumns("start", "end", "contract");
		periodstable.sort(new Object[] {"start", "end"}, new boolean[] {true, true});
		periodstable.setPageLength(periodstable.size());
		periodstable.setSizeFull();
		
	}




	protected void initializeRegimes(Table regimenstable) {
		// TODO Auto-generated method stub
		regimeperiods.removeAllItems();
		ui.populateResidency(ui.getConnectionPool(), regimeperiods);
		regimenstable.setContainerDataSource(ui.filteredByUserWithRegime(regimeperiods,selected));
		regimenstable.setSelectable(true);
		regimenstable.setVisibleColumns("start", "end", "regime");
		regimenstable.sort(new Object[] {"start", "end"}, new boolean[] {true, true});
		regimenstable.setPageLength(regimenstable.size());
		regimenstable.setSizeFull();
		
	}
	
	
}