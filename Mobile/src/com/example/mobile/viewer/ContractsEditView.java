package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.ContractOption;
import com.example.mobile.data.ContractResidency;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ContractsEditView extends NavigationView {
	

	private MobileUI ui = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	private BeanItemContainer<Contract> contracts;
	private BeanItemContainer<ContractOption> contractoptions;
	private BeanItemContainer<ContractResidency> contractperiods;
	//private BeanItemContainer<Meal> meals;
	//private BeanItemContainer<MealOption> mealoptions;
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
		
		
		Table contractsTable = new Table("", contracts);
		contractsTable.setSortContainerPropertyId("name");
		contractsTable.setSortAscending(true);
		contractsTable.setSelectable(true);
		contractsTable.setVisibleColumns(new Object[] {"name"});
		contractsTable.setPageLength(contractsTable.size());
		

		Table contractoptionsTable = new Table("",contractoptions);
		//contractoptionsTable.setSortContainerPropertyId("position");
		//contractoptionsTable.setSortAscending(true);
		//mealsTable.setSizeFull();
		//mealoptionsTable.setImmediate(true);
		contractoptionsTable.setSelectable(true);
		contractoptionsTable.setVisibleColumns(new Object[] {"mealOption"});
		final Filter noneFilter = 
				new SimpleStringFilter("pk", Integer.valueOf(-1).toString(), false, false);
		contractoptions.addContainerFilter(noneFilter);
		contractoptionsTable.setPageLength(contractoptionsTable.size());
		
		
		contractsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				contractoptions.removeAllContainerFilters();
				Contract contractselected = (Contract) event.getItemId();
				Filter filterbyContract = 
						new Compare.Equal("includedIn", contractselected.getPk());
				contractoptions.addContainerFilter(filterbyContract);
				//periods.addContainerFilter(noneFilter);
				//filterdeadlines.removeAllItems();
				//deadlinesinfo.removeAllItems();

			}});
		
		content.addComponent(contractsTable);
		content.addComponent(contractoptionsTable);
		setContent(content);
		

	}
}