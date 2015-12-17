package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.MealOptionResidency;
import com.example.mobile.data.Residency;
import com.example.mobile.presenter.ExitBehavior;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealEditView extends NavigationView {

	private MobileUI ui = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	private BeanItemContainer<Meal> meals;
	private BeanItemContainer<MealOption> mealoptions;
	private BeanItemContainer<Residency> periods; 
	private BeanItemContainer<MealOptionResidency> mealoptionperiods;
	private BeanItemContainer<MealOptionDeadline> mealoptiondeadlines; 
	private BeanItemContainer<MealOptionDeadline> filterdeadlines; 
	private BeanItemContainer<DeadlineDay> deadlinedays;
	private IndexedContainer deadlinesinfo;

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

		periods = new BeanItemContainer<Residency>(Residency.class);
		ui.populateResidency(connectionPool, periods);

		mealoptionperiods = new BeanItemContainer<MealOptionResidency>(MealOptionResidency.class);
		ui.populateMealOptionResidency(connectionPool, mealoptionperiods);

		mealoptiondeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
		ui.populateMealOptionDeadlines(connectionPool, mealoptiondeadlines);

		filterdeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);

		deadlinedays = new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
		ui.populateDeadlineDays(connectionPool, deadlinedays);


		Table mealsTable = new Table("",meals);
		mealsTable.setSortContainerPropertyId("position");
		mealsTable.setSortAscending(true);
		//mealsTable.setSizeFull();
		//mealsTable.setImmediate(true);
		mealsTable.setSelectable(true);
		mealsTable.setVisibleColumns(new Object[] {"literal"});
		mealsTable.setPageLength(mealsTable.size());



		Table mealoptionsTable = new Table("",mealoptions);
		mealoptionsTable.setSortContainerPropertyId("position");
		mealoptionsTable.setSortAscending(true);
		//mealsTable.setSizeFull();
		//mealoptionsTable.setImmediate(true);
		mealoptionsTable.setSelectable(true);
		mealoptionsTable.setVisibleColumns(new Object[] {"initial", "literal"});
		final Filter noneFilter = 
				new SimpleStringFilter("pk", Integer.valueOf(-1).toString(), false, false);
		mealoptions.addContainerFilter(noneFilter);
		mealoptionsTable.setPageLength(mealoptionsTable.size());


		Table periodsTable = new Table("",periods);
		periodsTable.setSortContainerPropertyId("start");
		periodsTable.setSortAscending(true);
		//mealsTable.setSizeFull();
		//periodsTable.setImmediate(true);
		periodsTable.setSelectable(true);
		periodsTable.setVisibleColumns(new Object[] {"start", "end"});
		periods.addContainerFilter(noneFilter);
		periodsTable.setPageLength(periodsTable.size());


		content.addComponent(mealsTable);
		content.addComponent(mealoptionsTable);
		content.addComponent(periodsTable);

		/** */
		deadlinesinfo = new IndexedContainer();
		Table mydeadlinesTable = new Table("", deadlinesinfo);
		mydeadlinesTable.setPageLength(deadlinesinfo.size());
		Calendar h = Calendar.getInstance();
		int myfirstdayofweek = h.getFirstDayOfWeek();
		h.set(Calendar.DAY_OF_WEEK, myfirstdayofweek);
		for (int i = 0; i <= 6; i++){
			mydeadlinesTable.addContainerProperty(ui.displayDay(h.get(Calendar.DAY_OF_WEEK)), Label.class, null);
			h.add(Calendar.DATE, 1);
		}
		mydeadlinesTable.addContainerProperty("-Days", Label.class, null);
		mydeadlinesTable.addContainerProperty("+Hour", Label.class, null);
		mydeadlinesTable.addContainerProperty("+Min", Label.class, null);

		content.addComponent(mydeadlinesTable);
		/** */



		mealsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				mealoptions.removeAllContainerFilters();
				Meal mealselected = (Meal) event.getItemId();
				Filter filterbyMeal = 
						//new Compare.Equal("ownedBy", Integer.valueOf(mealselected.getPk()).toString());
						new Compare.Equal("ownedBy", mealselected.getPk());
				mealoptions.addContainerFilter(filterbyMeal);
				periods.addContainerFilter(noneFilter);
				filterdeadlines.removeAllItems();
				deadlinesinfo.removeAllItems();

			}});

		mealoptionsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				periods.removeAllContainerFilters();
				MealOption mealoptionselected = (MealOption) event.getItemId();
				Filter filterbyMealOption = 
						new Compare.Equal("ownedByOption", mealoptionselected.getPk());
				periods.addContainerFilter(filterbyMealOption);		
				filterdeadlines.removeAllItems();
				deadlinesinfo.removeAllItems();

			}});

		periodsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				mealoptionperiods.removeAllContainerFilters();
				filterdeadlines.removeAllItems();
				deadlinesinfo.removeAllItems();
				Residency periodselected = (Residency) event.getItemId();
				Filter filterbyPeriod = 
						new Compare.Equal("residency", periodselected.getPk());
				mealoptionperiods.addContainerFilter(filterbyPeriod);
				/** */
				for (Iterator<MealOptionDeadline> j = mealoptiondeadlines.getItemIds().iterator(); j.hasNext();){
					MealOptionDeadline deadline = (MealOptionDeadline) j.next();
					for (Iterator<MealOptionResidency> i = mealoptionperiods.getItemIds().iterator(); i.hasNext();) {
						MealOptionResidency period = (MealOptionResidency) i.next();
						if (deadline.getOwnedBy() == period.getResidency()){
							filterdeadlines.addItem(deadline);
						}
					}

				}	
				refreshDeadlinesInfo();

			}});




		setContent(content);



	}


	protected void refreshDeadlinesInfo() {
		// TODO Auto-generated method stub
		for (Iterator<MealOptionDeadline> h = filterdeadlines.getItemIds().iterator(); h.hasNext();) {
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


	/**
	 private String displayDay(int dayofweek) {
	 
		// TODO Auto-generated method stub
		String day = "";
		switch (dayofweek) {
		case (1):
			day = "Mon";
		break;
		case (2):
			day = "Tue";
		break;
		case (3): 
			day = "Wed";
		break;
		case (4):
			day = "Thu";
		break;
		case (5):
			day = "Fri";
		break;
		case (6):
			day = "Sat";
		break;
		case (7):
			day = "Sun";
		break;
		}
		return day;
	}
	*/


}
