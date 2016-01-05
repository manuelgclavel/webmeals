package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import com.example.mobile.MobileUI;
import com.example.mobile.viewer.ContractOptionDayCheckBox;
import com.example.mobile.data.Contract;
import com.example.mobile.data.ContractOption;
import com.example.mobile.data.ContractOptionDay;
import com.example.mobile.data.ContractResidency;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.Residency;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ContractsEditView extends NavigationView {
	

	private MobileUI ui = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	private BeanItemContainer<Contract> contracts;
	private BeanItemContainer<ContractOption> contractoptions;
	private BeanItemContainer<ContractOptionDay> contractoptiondays;
	private BeanItemContainer<Meal> meals;
	private BeanItemContainer<MealOption> mealoptions;
	private IndexedContainer contractmealoptions;
	
	String timezone = ui.getResidence().getZone();
	

	public ContractsEditView(){
		VerticalComponentGroup content = new VerticalComponentGroup();	
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
		
		contracts = new BeanItemContainer<Contract>(Contract.class);
		ui.populateContracts(connectionPool, contracts);
		contractoptions = new BeanItemContainer<ContractOption>(ContractOption.class);
		ui.populateContractOptions(connectionPool, contractoptions);
		
		contractoptiondays = new BeanItemContainer<ContractOptionDay>(ContractOptionDay.class);
		ui.populateContractOptionDays(connectionPool, contractoptiondays);
		
		meals = new BeanItemContainer<Meal>(Meal.class);
		ui.populateMeals(connectionPool, meals);
		mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
		ui.populateMealOptions(connectionPool, mealoptions);

	
		
		Table contractsTable = new Table("", contracts);
		contractsTable.setSortContainerPropertyId("name");
		contractsTable.setSortAscending(true);
		contractsTable.setSelectable(true);
		contractsTable.setVisibleColumns(new Object[] {"name"});
		contractsTable.setPageLength(contractsTable.size());
		
		
		
		
		contractmealoptions = new IndexedContainer();
		Table contractmealoptionsTable = new Table("", contractmealoptions);
		/** */
		contractmealoptionsTable.setPageLength(contractmealoptionsTable.size());
		Calendar h = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		int myfirstdayofweek = h.getFirstDayOfWeek();
		h.set(Calendar.DAY_OF_WEEK, myfirstdayofweek);
		contractmealoptionsTable.addContainerProperty("Meal", Label.class, null);
		contractmealoptionsTable.addContainerProperty("Meal option", Label.class, null);
		for (int i = 0; i <= 6; i++){
			contractmealoptionsTable.addContainerProperty(ui.displayDay(h.get(Calendar.DAY_OF_WEEK)), CheckBox.class, null);
			h.add(Calendar.DATE, 1);
		}
		
		contractsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				Contract contractselected = (Contract) event.getItemId();
				refreshContractOptionsInfo(contractselected);
				

			}});
		
		content.addComponent(contractsTable);
		content.addComponent(contractmealoptionsTable);
		setContent(content);
		

	}
	
	protected void refreshContractOptionsInfo(Contract selected) {
		// TODO Auto-generated method stub
		Meal meal;
		MealOption mealoption;
		for(Iterator<MealOption> m = mealoptions.getItemIds().iterator(); m.hasNext();){
			mealoption = m.next();
			meal = ui.findMealOfMealOption(meals, mealoption);
			// Create an item
			Object itemId = contractmealoptions.addItem();
			// Get the item object
			Item item = contractmealoptions.getItem(itemId);
			// Access a property in the item
			Property<Label> mealproperty = item.getItemProperty("Meal");
			Property<Label> mealoptionproperty = item.getItemProperty("Meal option");		
			// Do something with the property
			mealproperty.setValue(new Label(meal.getLiteral()));
			mealoptionproperty.setValue(new Label(mealoption.getInitial()));
			
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timezone));
			int firstdayofweek = c.getFirstDayOfWeek();
			c.set(Calendar.DAY_OF_WEEK, firstdayofweek);

			
			for (int i = 0; i <= 6; i++){
				Property<CheckBox> day = item.getItemProperty(ui.displayDay(c.get(Calendar.DAY_OF_WEEK)));
				ContractOptionDayCheckBox checkbox = new ContractOptionDayCheckBox(mealoption,selected,i,
						contractoptions, contractoptiondays
						);				
				day.setValue(checkbox);
				c.add(Calendar.DATE, 1);
			}
		}
	}

	
	
	
}