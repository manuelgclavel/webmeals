package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.Residency;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealEditView extends NavigationView {

	private MobileUI ui = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	private BeanItemContainer<Meal> meals;
	private BeanItemContainer<MealOption> mealoptions;
	private BeanItemContainer<Residency> periods; 
	private BeanItemContainer<MealOptionDeadline> mealoptiondeadlines; 
	private BeanItemContainer<MealOptionDeadline> filterdeadlines; 
	private IndexedContainer deadlinesinfo;
	
	private Table mealsTable;
	private Table mealoptionsTable;
	private Table periodsTable;
	private Table deadlinesTable;
	
	

	public MealEditView(){
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

		meals = new BeanItemContainer<Meal>(Meal.class);
		ui.populateMeals(connectionPool, meals);

		mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
		ui.populateMealOptions(connectionPool, mealoptions);
		
		mealoptiondeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);

		periods = new BeanItemContainer<Residency>(Residency.class);
		ui.populateResidency(connectionPool, periods);
	
		filterdeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);

		
		mealsTable = new Table("",meals);
		mealsTable.setSortContainerPropertyId("position");
		mealsTable.setSortAscending(true);
		mealsTable.setSelectable(true);
		mealsTable.setVisibleColumns(new Object[] {"literal"});
		mealsTable.setPageLength(mealsTable.size());



		mealoptionsTable = new Table("",mealoptions);
		mealoptionsTable.setSortContainerPropertyId("position");
		mealoptionsTable.setSortAscending(true);
		mealoptionsTable.setSelectable(true);
		mealoptionsTable.setVisibleColumns(new Object[] {"initial", "literal"});
		final Filter noneFilter = 
				new SimpleStringFilter("pk", Integer.valueOf(-1).toString(), false, false);
		mealoptions.addContainerFilter(noneFilter);
		mealoptionsTable.setPageLength(mealoptionsTable.size());


		periodsTable = new Table("",periods);
		periodsTable.setSortContainerPropertyId("start");
		periodsTable.setSortAscending(true);
		periodsTable.setSelectable(true);
		periodsTable.setVisibleColumns(new Object[] {"start", "end"});
		periods.addContainerFilter(noneFilter);
		periodsTable.setPageLength(periodsTable.size());


		content.addComponent(mealsTable);
		content.addComponent(mealoptionsTable);
		content.addComponent(periodsTable);

		

		deadlinesinfo = new IndexedContainer();
		deadlinesTable = new Table("", deadlinesinfo);
		deadlinesTable.setSelectable(true);
		deadlinesTable.setPageLength(deadlinesinfo.size());
		
		GregorianCalendar d = ui.createGCalendarNoTime();
		d.set(Calendar.DAY_OF_WEEK, d.getFirstDayOfWeek());
		
		for (int i = 0; i <= 6; i++){
			deadlinesTable.addContainerProperty(ui.displayDay(d.get(Calendar.DAY_OF_WEEK)), CheckBox.class, null);
			d.add(Calendar.DATE, 1);
		}
		deadlinesTable.addContainerProperty("MealOptionDeadline", MealOptionDeadline.class, null);
		deadlinesTable.addContainerProperty("-Days", DayView.class, null);
		deadlinesTable.addContainerProperty("Hour", HourView.class, null);
		deadlinesTable.addContainerProperty("Min", MinuteView.class, null);
		
		
		
		
		Object[] visiblecolumns = new Object[10];
		d.set(Calendar.DAY_OF_WEEK, d.getFirstDayOfWeek());
		for (int i = 0; i <= 6; i++){
			visiblecolumns[i] = ui.displayDay(d.get(Calendar.DAY_OF_WEEK));
			d.add(Calendar.DATE, 1);
		}
		visiblecolumns[7] = "-Days";
		visiblecolumns[8] = "Hour";
		visiblecolumns[9] = "Min";
		deadlinesTable.setVisibleColumns(visiblecolumns);
		content.addComponent(deadlinesTable);
		
		HorizontalButtonGroup rulebuttons = new HorizontalButtonGroup();
		Button addrule = new Button("Add");
		Button deleterule = new Button("Delete");
		rulebuttons.addComponent(addrule);
		rulebuttons.addComponent(deleterule);
		
		addrule.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Residency selected = (Residency) periodsTable.getValue();
				if (!(selected == null)){
					ui.insertMealOptionDeadline(selected);
					refreshDeadlinesInfo((Residency) selected);
					
				} else {
					Notification.show("Please, select first a period.");
				}
			}});
		
		deleterule.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Residency periodselected = (Residency) periodsTable.getValue();
				if (!(periodselected == null)){
					Integer selected = (Integer) deadlinesTable.getValue();
					MealOptionDeadline mealoptiondeadline;
					if (!(selected == null)){
						mealoptiondeadline = ((MealOptionDeadline) deadlinesTable.getItem(selected).getItemProperty("MealOptionDeadline").getValue());
						ui.deleteMealOptionDeadline(mealoptiondeadline);
						refreshDeadlinesInfo((Residency) periodselected);
					} else {
						Notification.show("Please, select first a rule.");
					}
				} else {
					Notification.show("Please, select first a period.");
				}
			}});
		
		content.addComponent(rulebuttons);



		mealsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				mealoptions.removeAllContainerFilters();
				periods.removeAllContainerFilters();
				filterdeadlines.removeAllItems();
				deadlinesinfo.removeAllItems();
				if (!(mealsTable.isSelected(event.getItemId()))){
					Filter filterbyMeal = 
							new Compare.Equal("ownedBy", ((Meal) event.getItemId()).getPk());
					mealoptions.addContainerFilter(filterbyMeal);
					mealoptionsTable.select(null);
					periods.addContainerFilter(noneFilter);
					periodsTable.select(null);
					
				} else {
					mealoptions.addContainerFilter(noneFilter);
					mealoptionsTable.select(null);
					periods.addContainerFilter(noneFilter);
					periodsTable.select(null);
				}
				
			}});

		mealoptionsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				periods.removeAllContainerFilters();
				filterdeadlines.removeAllItems();
				deadlinesinfo.removeAllItems();
				if (!(mealoptionsTable.isSelected(event.getItemId()))){
					Filter filterbyMealOption = 
							new Compare.Equal("ownedByOption", ((MealOption) event.getItemId()).getPk());
					periods.addContainerFilter(filterbyMealOption);
					
				} else{
					periods.addContainerFilter(noneFilter);
					periodsTable.select(null);
					
				}
					
				

			}});

		periodsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				//mealoptionperiods.removeAllContainerFilters();
				if (!(periodsTable.isSelected(event.getItemId()))){
					refreshDeadlinesInfo((Residency) event.getItemId());
				} else {
					filterdeadlines.removeAllItems();
					deadlinesinfo.removeAllItems();
				}

			}});


		setContent(content);

	}



	protected void refreshDeadlinesInfo(Residency periodselected) {
		// TODO Auto-generated method stub
		MealOptionDeadline deadline;
		mealoptiondeadlines.removeAllItems();
		ui.populateMealOptionDeadlines(connectionPool, mealoptiondeadlines);
		filterdeadlines.removeAllItems();
		deadlinesinfo.removeAllItems();
		for (Iterator<MealOptionDeadline> i = mealoptiondeadlines.getItemIds().iterator(); i.hasNext();){
			deadline =  i.next();
			if (deadline.getOwnedBy() == periodselected.getPk()){
					filterdeadlines.addItem(deadline);
				}
			}	
		
		for (Iterator<MealOptionDeadline> h = filterdeadlines.getItemIds().iterator(); h.hasNext();) {
			deadline = (MealOptionDeadline) h.next();

			// Create an item
			Object itemId = deadlinesinfo.addItem();

			// Get the item object
			Item item = deadlinesinfo.getItem(itemId);

			// Access a property in the item
			Property<MealOptionDeadline> mealoptiondeadline = item.getItemProperty("MealOptionDeadline");
			Property<DayView> cdays = item.getItemProperty("-Days");
			Property<HourView> chour = item.getItemProperty("Hour");
			Property<MinuteView> cmin = item.getItemProperty("Min");

			// Do something with the property
			mealoptiondeadline.setValue(deadline);
			cdays.setValue(new DayView(deadline));
			chour.setValue(new HourView(deadline));
			cmin.setValue(new MinuteView(deadline));



			GregorianCalendar c = ui.createGCalendarNoTime();
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
			
			
			for (int i = 0; i <= 6; i++){
				Property<CheckBox> day = item.getItemProperty(ui.displayDay(c.get(Calendar.DAY_OF_WEEK)));
				DeadlineDayCheckBox checkbox = 
						new DeadlineDayCheckBox(deadline, ui.encodeDay(c.get(Calendar.DAY_OF_WEEK)), periodselected, filterdeadlines);			
				day.setValue(checkbox);
				c.add(Calendar.DATE, 1);
			}
		}
	}
}
