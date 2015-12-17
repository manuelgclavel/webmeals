package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Iterator;

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
	
	//private BeanItemContainer<ContractResidency> contractperiods;
	
	//private BeanItemContainer<Residency> periods; 
	//private BeanItemContainer<MealOptionResidency> mealoptionperiods;
	//private BeanItemContainer<MealOptionDeadline> mealoptiondeadlines; 
	//private BeanItemContainer<MealOptionDeadline> filterdeadlines; 
	//private BeanItemContainer<DeadlineDay> deadlinedays;
	//private IndexedContainer deadlinesinfo;

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

		
		
		//periods = new BeanItemContainer<Residency>(Residency.class);
		//ui.populateResidency(connectionPool, periods);
		
		
		
		Table contractsTable = new Table("", contracts);
		contractsTable.setSortContainerPropertyId("name");
		contractsTable.setSortAscending(true);
		contractsTable.setSelectable(true);
		contractsTable.setVisibleColumns(new Object[] {"name"});
		contractsTable.setPageLength(contractsTable.size());
		

		Table contractoptionsTable = new Table("",contractoptions);
		contractoptionsTable.setSelectable(true);
		contractoptionsTable.setVisibleColumns(new Object[] {"mealOption"});
		final Filter noneFilter = 
				new SimpleStringFilter("pk", Integer.valueOf(-1).toString(), false, false);
		contractoptions.addContainerFilter(noneFilter);
		contractoptionsTable.setPageLength(contractoptionsTable.size());
		
		
		
		contractmealoptions = new IndexedContainer();
		Table contractmealoptionsTable = new Table("", contractmealoptions);
		contractmealoptionsTable.setPageLength(contractmealoptionsTable.size());
		Calendar h = Calendar.getInstance();
		int myfirstdayofweek = h.getFirstDayOfWeek();
		h.set(Calendar.DAY_OF_WEEK, myfirstdayofweek);
		contractmealoptionsTable.addContainerProperty("Meal option", Label.class, null);
		for (int i = 0; i <= 6; i++){
			contractmealoptionsTable.addContainerProperty(ui.displayDay(h.get(Calendar.DAY_OF_WEEK)), CheckBox.class, null);
			h.add(Calendar.DATE, 1);
		}
		//mealoptionsTable.addContainerProperty("-Days", Label.class, null);
		//mealoptionsTable.addContainerProperty("+Hour", Label.class, null);
		//mealoptionsTable.addContainerProperty("+Min", Label.class, null);


		
		
		
		//Table contractperiodsTable = new Table("",periods);
		//contractperiodsTable.setSelectable(true);
		//contractperiodsTable.setVisibleColumns(new Object[] {"start", "end"});
		//periods.addContainerFilter(noneFilter);
		//contractperiodsTable.setPageLength(contractperiodsTable.size());
		
		contractsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				Contract contractselected = (Contract) event.getItemId();
				refreshContractOptionsInfo(contractselected);
				
				//contractoptions.removeAllContainerFilters();
				//Filter optionsfilterbyContract = 
				//		new Compare.Equal("includedIn", contractselected.getPk());
				//contractoptions.addContainerFilter(optionsfilterbyContract);
				
				//periods.removeAllContainerFilters();
				//Filter periodsfilterbyContract = 
				//		new Compare.Equal("contract", contractselected.getPk());
				//periods.addContainerFilter(periodsfilterbyContract);

			}});
		
		content.addComponent(contractsTable);
		content.addComponent(contractmealoptionsTable);
		//content.addComponent(contractoptionsTable);
		//content.addComponent(contractperiodsTable);
		setContent(content);
		

	}
	
	protected void refreshContractOptionsInfo(Contract selected) {
		// TODO Auto-generated method stub
		MealOption mealoption;
		for(Iterator<MealOption> m = mealoptions.getItemIds().iterator(); m.hasNext();){
			mealoption = m.next();
			// Create an item
			Object itemId = contractmealoptions.addItem();
			// Get the item object
			Item item = contractmealoptions.getItem(itemId);
			// Access a property in the item
			Property<Label> mealoptionproperty = item.getItemProperty("Meal option");		
			// Do something with the property
			mealoptionproperty.setValue(new Label(mealoption.getInitial()));
			
			Calendar c = Calendar.getInstance();
			int firstdayofweek = c.getFirstDayOfWeek();
			c.set(Calendar.DAY_OF_WEEK, firstdayofweek);

			
			for (int i = 0; i <= 6; i++){
				Property<CheckBox> day = item.getItemProperty(ui.displayDay(c.get(Calendar.DAY_OF_WEEK)));
				ContractOptionDayCheckBox checkbox = new ContractOptionDayCheckBox(mealoption,selected,i);
				day.setValue(checkbox);
				if (mealoptionisincluded(mealoption, selected, i)){
					checkbox.setValue(true);
				} else {
					checkbox.setValue(false);
				}
				c.add(Calendar.DATE, 1);
			}
		}
	}
	
	
	private boolean mealoptionisincluded(MealOption mealoption, Contract selected, int day) {
		// TODO Auto-generated method stub
		boolean isincluded = false;
		ContractOption contractoption;
		ContractOptionDay contractoptionday;
		for(Iterator<ContractOption> i = contractoptions.getItemIds().iterator(); (i.hasNext() && !(isincluded));){
			contractoption = i.next();
			if (contractoption.getIncludedIn() == selected.getPk()){
				if (contractoption.getMealOption() == mealoption.getPk()){
					for(Iterator<ContractOptionDay> j = contractoptiondays.getItemIds().iterator(); j.hasNext();){
						contractoptionday = j.next();
						if (contractoptionday.getContract() == contractoption.getPk()){
							if (contractoptionday.getDay() == day){
								isincluded = true;
								break;
							}
						}
					}
				}
			}
		}
		
		return isincluded;
	}
	/**
		for (Iterator<MealOption> h = filterdeadlines.getItemIds().iterator(); h.hasNext();) {
			MealOptionDeadline deadline = (MealOptionDeadline) h.next();

			// Create an item
			Object itemId = deadlinesinfo.addItem();

			// Get the item object
			Item item = deadlinesinfo.getItem(itemId);

			// Access a property in the item
			Property<Label> cdays = item.getItemProperty("-Days");
			Property<Label> chour = item.getItemProperty("+Hour");
			Property<Label> cmin = item.getItemProperty("+Min");

			// Do something with the property
			cdays.setValue(new Label(Integer.valueOf(deadline.getCday()).toString()));
			chour.setValue(new Label(Integer.valueOf(deadline.getChour()).toString()));
			cmin.setValue(new Label(Integer.valueOf(deadline.getCminute()).toString()));



			Calendar c = Calendar.getInstance();
			int firstdayofweek = c.getFirstDayOfWeek();
			c.set(Calendar.DAY_OF_WEEK, firstdayofweek);

			for (int i = 0; i <= 6; i++){
				Property<Label> day = item.getItemProperty(ui.displayDay(c.get(Calendar.DAY_OF_WEEK)));
				for (Iterator<DeadlineDay> j = deadlinedays.getItemIds().iterator(); j.hasNext();){
					DeadlineDay deadlineday = (DeadlineDay) j.next();
					if (deadlineday.getDeadline() == deadline.getPk()){
						if (deadlineday.getDay() == i){
							//day.setValue(new Label(Integer.valueOf(deadlineday.getDay()).toString()));
							day.setValue(new Label("X"));
						}
					}
				}
				c.add(Calendar.DATE, 1);
			}
			
		}
	}
*/

}