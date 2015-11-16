package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.Meal;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class SelectionDateView extends VerticalComponentGroup {
	final private MobileUI mobile;
	final private JDBCConnectionPool connectionPool;
	//final private Date dayselected;


	/** 
	 * 
	 * @param date
	 * We assume that selected !== null and user !== null
	 * 
	 */
	public SelectionDateView(java.util.Date date){

		this.mobile = ((MobileUI) UI.getCurrent());
		this.connectionPool = mobile.getConnectionPool();
		//this.dayselected = new java.sql.Date(date.getTime());

		PreparedStatement ps;
		ResultSet result;
		DailyMealSelection dailymealselection;
		
		try {
			Connection conn = connectionPool.reserveConnection();

			/** Get the daily meal selection for this day and user (after creating it, if it does not exist */
				/** Check if it exists already */
			ps = conn.prepareStatement("SELECT count(*), pk from DailyMealSelection" + " " +  
					"WHERE Date(date) = ?  and selectedBy = ?");
			//ps.setDate(1, dayselected); 
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, mobile.getUser().getPk()); 
			result = ps.executeQuery();
			result.next();
			if (result.getInt(1) == 0){
					/** If it does not exists, created first and then save it */
				ps = conn.prepareStatement("INSERT INTO DailyMealSelection" + " " + 
						"(date,selectedBy) values (?,?)");
				//ps.setDate(1, dayselected);
				ps.setDate(1, new java.sql.Date(date.getTime()));
				ps.setInt(2, mobile.getUser().getPk()); 
				ps.executeUpdate();
				result.close();
				ps.close();
				conn.commit();
				ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
				result = ps.executeQuery();
				result.next();
				dailymealselection = new DailyMealSelection(result.getInt(1));
				result.close();
				ps.close();
				
			} else {
					/** If it exists, save it */
				dailymealselection = new DailyMealSelection(result.getInt(2));
				result.close();
				ps.close();
			}
			
			/** Get the meal options available for this meal */

			BeanItemContainer<Meal> meals = new BeanItemContainer<Meal>(Meal.class);
	
			ps = conn.prepareStatement("SELECT * FROM Meal ORDER BY position");
			result = ps.executeQuery();
			while (result.next()){
				Meal nextMeal = new Meal(result.getInt(1), result.getInt(2), result.getString(3), result.getInt(4));
				meals.addItem(nextMeal);				
			}
			result.close();
			ps.close();
			
			/** */
			//VerticalComponentGroup combogroup = new VerticalComponentGroup();
			
			/** Create a combobox for this meal */
			for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();) {
				//Notification.show("HERE");
				Meal meal = j.next();
				//ComboBox options = new MealOptionComboBox(meal, dayselected, mobile.getUser(), dailymealselection, connectionPool);
				//NativeSelect options = new MealOptionComboBox(meal, dayselected, dailymealselection);
				NativeSelect options = new MealOptionComboBox(meal, date, dailymealselection);	
				addComponent(options);
				//combogroup.addComponent(new Label(meal.getLiteral()));
			}

			/** release connection */
			//addComponent(combogroup);
			//content.addComponent(combogroup);
			//setContent(combogroup);
			connectionPool.releaseConnection(conn);

		}
		catch (SQLException e) {
			throw new RuntimeException (e);
		}

	}
}
