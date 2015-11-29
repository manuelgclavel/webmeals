package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.MealOptionResidency;
import com.example.mobile.data.Residency;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealEditView extends NavigationView {
	private JDBCConnectionPool connectionPool ;
	
	private BeanItemContainer<Meal> meals;
	private BeanItemContainer<MealOption> mealoptions;
	private BeanItemContainer<Residency> periods; 
	private BeanItemContainer<MealOptionResidency> mealoptionresidencies;
	private BeanItemContainer<MealOptionDeadline> deadlines; 
	private BeanItemContainer<MealOptionDeadline> filterdeadlines; 
	private BeanItemContainer<DeadlineDay> deadlinedays;
	private IndexedContainer deadlinesinfo;
	
	public MealEditView(){
		VerticalComponentGroup content = new VerticalComponentGroup();	
		connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();
		
			Connection conn;
			try {
				conn = connectionPool.reserveConnection();
				meals = new BeanItemContainer<Meal>(Meal.class);
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal");
				ResultSet result = ps.executeQuery();
				while (result.next()){
					meals.addItem(new Meal(result.getInt(1), result.getInt(2), result.getString(3),
							result.getInt(4)));
				}
				result.close();
				ps.close();
				
				mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
				ps = conn.prepareStatement("SELECT * FROM MealOption");
				result = ps.executeQuery();
				while (result.next()){
					mealoptions.addItem(new MealOption(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getInt(5)));
				}
				result.close();
				ps.close();
				

				periods = new BeanItemContainer<Residency>(Residency.class);
				ps = conn.prepareStatement("SELECT * FROM Residency");
				result = ps.executeQuery();
				while (result.next()){
					periods.addItem(new Residency(result.getInt(1), result.getDate(2), result.getDate(3),
							result.getInt(4), result.getInt(5), result.getInt(6), result.getInt(7),
							result.getInt(8), result.getInt(9))); 
				}
				result.close();
				ps.close();
				
				mealoptionresidencies = new BeanItemContainer<MealOptionResidency>(MealOptionResidency.class);
				ps = conn.prepareStatement("SELECT MealOptionDeadline_ownedBy as ownedBy, Residency_deadlines as deadlines FROM MealOptionDeadline_ownedBy__Residency_deadlines");
				result = ps.executeQuery();
				while (result.next()){
					mealoptionresidencies.addItem(new MealOptionResidency(result.getInt(1), result.getInt(2))); 
				}
				result.close();
				ps.close();
				
				//Notification.show(Integer.valueOf(mealoptionresidencies.size()).toString());
				
				
				
				deadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
				ps = conn.prepareStatement("SELECT pk, cday, chour, cminute, literal, ownedBy FROM MealOptionDeadline");
				result = ps.executeQuery();
				while (result.next()){
					deadlines.addItem(new MealOptionDeadline(result.getInt(1), result.getInt(2), result.getInt(3),
							result.getInt(4), result.getString(5), result.getInt(6))); 
				}
				result.close();
				ps.close();
				
				filterdeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
				
				deadlinedays = new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
				ps = conn.prepareStatement("SELECT * FROM MealOptionDeadline_days");
				result = ps.executeQuery();
				while (result.next()){
					deadlinedays.addItem(new DeadlineDay(result.getInt(1), result.getInt(2))); 
				}
				result.close();
				ps.close();
				
				
				
				
				connectionPool.releaseConnection(conn);
				
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
				Calendar h = Calendar.getInstance();
				int myfirstdayofweek = h.getFirstDayOfWeek();
				h.set(Calendar.DAY_OF_WEEK, myfirstdayofweek);
				for (int i = 0; i <= 6; i++){
					mydeadlinesTable.addContainerProperty(displayDay(h.get(Calendar.DAY_OF_WEEK)), Label.class, null);
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
						mealoptionresidencies.removeAllContainerFilters();
						filterdeadlines.removeAllItems();
						deadlinesinfo.removeAllItems();
						Residency periodselected = (Residency) event.getItemId();
						Filter filterbyPeriod = 
								new Compare.Equal("residency", periodselected.getPk());
						mealoptionresidencies.addContainerFilter(filterbyPeriod);
						/** */
						for (Iterator<MealOptionDeadline> j = deadlines.getItemIds().iterator(); j.hasNext();){
							MealOptionDeadline deadline = (MealOptionDeadline) j.next();
							for (Iterator<MealOptionResidency> i = mealoptionresidencies.getItemIds().iterator(); i.hasNext();) {
								MealOptionResidency period = (MealOptionResidency) i.next();
								if (deadline.getOwnedBy() == period.getResidency()){
									filterdeadlines.addItem(deadline);
								}
						}
							
						}	
						refreshDeadlinesInfo();
						
					}});
			
					
				
				
				setContent(content);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
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
			Property<Label> day = item.getItemProperty(displayDay(c.get(Calendar.DAY_OF_WEEK)));
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


}
