package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealSelectionPlus;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class SummariesView extends NavigationView {
	final private MobileUI  ui = (MobileUI) UI.getCurrent();
	final private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	private BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);
	private BeanItemContainer<MealSelectionPlus> mealselections;
	private BeanItemContainer<Meal> meals = new BeanItemContainer<Meal>(Meal.class);
	private BeanItemContainer<MealOption> mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
	//private BeanItemContainer<FoodRegime> regimenes = new BeanItemContainer<FoodRegime>(FoodRegime.class);
	private IndexedContainer residents;
	
	
	
	final private Table residentsTable;
	final private DateField start;
	final private DateField end;


	public SummariesView(){
		
		ui.populateUsers(connectionPool, users);
		ui.populateMeals(connectionPool, meals);
		ui.populateMealOptions(connectionPool, mealoptions);
		//ui.populateFoodRegimes(connectionPool, regimenes);
		
		residents = new IndexedContainer();
		residentsTable = new Table("", residents);
		/** */
		residentsTable.setPageLength(residentsTable.size());
		residentsTable.addContainerProperty("Name", Label.class, null);
		residentsTable.addContainerProperty("Surname", Label.class, null);
		
		MealOption mealoption;
		for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
			mealoption = (MealOption) i.next();
			residentsTable.addContainerProperty(mealoption.getInitial(), Label.class, null);
		}
		residentsTable.addContainerProperty("Total", Label.class, null);
		
		residentsTable.setVisible(false);
		
		VerticalComponentGroup content = new VerticalComponentGroup();
		
		
		GregorianCalendar c = ui.createGCalendar();
		
		//c.set(Calendar.YEAR, c.get(Calendar.YEAR));
		//c.set(Calendar.MONTH, c.get(Calendar.MONTH));
		//c.set(Calendar.MONTH, Calendar.FEBRUARY);
		int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		start = new DateField("From: ");
		end = new DateField("To: ");
		Button calculate = new Button("Summary");
		
		c.set(Calendar.DAY_OF_MONTH, 1);
		start.setValue(c.getTime());
		c.set(Calendar.DAY_OF_MONTH, max);
		end.setValue(c.getTime());
		
		start.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				residentsTable.setVisible(false);
			}});
		
		end.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				residentsTable.setVisible(false);
			}});
		
		calculate.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!(start.getValue() == null)){
					if (!(end.getValue() == null)){
						residentsTable.setVisible(false);
						residents.removeAllItems();
						generateSummary(start.getValue(), end.getValue());
						residentsTable.setVisible(true);
					} else {
					 Notification.show("Please, select an ending date.");
					}
				}
				else {
					Notification.show("Please, select a starting date.");
				}
			}

			private void generateSummary(Date start, Date end) {
				// TODO Auto-generated method stub
				mealselections = new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
				ui.populateMealSelectionsPlusPlus(connectionPool, mealselections, start, end);
				User user;
				for(Iterator<User> i = ui.selectResidents(users).getItemIds().iterator(); i.hasNext();){
					user = i.next();
					// Create an item
					Object itemId = residents.addItem();
					// Get the item object
					Item item = residents.getItem(itemId);
					// Access a property in the item
					Property<Label> nameproperty = item.getItemProperty("Name");
					Property<Label> surnameproperty = item.getItemProperty("Surname");	
					Property<Label> totalproperty = item.getItemProperty("Total");	
					// Do something with the property
					nameproperty.setValue(new Label(user.getName()));
					surnameproperty.setValue(new Label(user.getSurname()));
					
					MealOption mealoption;
					for (Iterator<MealOption> m = mealoptions.getItemIds().iterator(); m.hasNext();){
						mealoption = (MealOption) m.next();
						Property<Label> option = item.getItemProperty(mealoption.getInitial());		
						option.setValue(
								new Label(Integer.valueOf(ui.selectMealOption(ui.selectSelectedBy(mealselections, user), mealoption).size()).toString()));
					}
					totalproperty.setValue(new Label(Integer.valueOf(ui.selectSelectedBy(mealselections, user).size()).toString()));
				}		
				
				
			}
		});

		
		//users = ui.selectResidents(users);
		//residentsTable = new Table("", ui.selectResidents(users));
		//Table.setVisibleColumns("surname", "name");
		//usersTable.sort(new Object[] {"surname", "name"}, new boolean[] {true, true});
		//usersTable.setPageLength(usersTable.size());
		//usersTable.setVisible(false);
		
		content.addComponent(start);
		content.addComponent(end);
		content.addComponent(calculate);
		content.addComponent(residentsTable);
		setContent(content);
	}
}