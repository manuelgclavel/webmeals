package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.Residency;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class SelectionDateView extends VerticalComponentGroup {
	private MobileUI mobile = ((MobileUI) UI.getCurrent());
	private JDBCConnectionPool connectionPool = mobile.getConnectionPool();
	private BeanItemContainer<Residency> periods ;
	private BeanItemContainer<MealOptionDeadline> mealoptiondeadlines ;
	private BeanItemContainer<DeadlineDay> deadlinedays ;

	public SelectionDateView(GregorianCalendar c, int dinerpk, int dinertype) {
		// TODO Auto-generated constructor stub
	
		
		PreparedStatement ps;
		ResultSet result;
		DailyMealSelection dailymealselection;
		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		
		try {
			Connection conn = connectionPool.reserveConnection();

			/** Get the daily meal selection for this day and user (after creating it, if it does not exist */
			/** Check if it exists already */
			String property = "offeredTo";
			switch (dinertype){
			case 1 : 
				property = "selectedBy";
				break;
			case 2 : 
				property = "offeredTo";
				break;

			default : 
				break;
			}
			ps = conn.prepareStatement("SELECT count(*), pk from DailyMealSelection" + " " +  
					"WHERE Date(date) = Date(?)  and" + " " + property +  " = ?");
			ps.setDate(1, new java.sql.Date(c.getTime().getTime()));
			ps.setInt(2, dinerpk);
			result = ps.executeQuery();
			result.next();
			if (result.getInt(1) == 0){
					/** If it does not exists, created first and then save it */
				
				ps = conn.prepareStatement("INSERT INTO DailyMealSelection" + " " + 
						"(date," + property + ") values (Date(?),?)");
				ps.setDate(1, new java.sql.Date(c.getTime().getTime()));
				ps.setInt(2, dinerpk);
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

				//ps = conn.prepareStatement("SELECT count(*), pk, description, name, residence from Residency" + " " +
				//		"where ownedByRegime = ?" + " " +
				//		"and Date(date) <= ?" + " " +
				//		"and Date(date) >= ?");

				ps = conn.prepareStatement("SELECT count(*), FoodRegime.pk, description, name, residence" + " " +
						"FROM FoodRegime" + " " + 
						"LEFT JOIN" + " " +
						"Residency" + " " +
						"on FoodRegime.pk = Residency.regime" + " " +
						"where ownedByRegime = ?" + " " + 
						"and Date(Residency.start) <= Date(?)" + " " + 
						"and Date(Residency.end) >= Date(?)");
				ps.setInt(1, mobile.getUser().getPk());
				ps.setDate(2, new java.sql.Date(c.getTime().getTime()));
				ps.setDate(3, new java.sql.Date(c.getTime().getTime()));
				result = ps.executeQuery();
				result.next();
				FoodRegime regime = null;
				if (result.getInt(1)== 1){
					regime = new FoodRegime(result.getInt(2), result.getString(3), 
							result.getString(4), result.getInt(5));
				}
				result.close();
				ps.close();

				conn.close();
				connectionPool.releaseConnection(conn);


				/** Create a combobox for this meal */
				periods = new BeanItemContainer<Residency>(Residency.class);
				mobile.populateResidency(connectionPool, periods);
				
				mealoptiondeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
				mobile.populateMealOptionDeadlines(connectionPool, mealoptiondeadlines);
				
				deadlinedays = new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
				mobile.populateDeadlineDays(connectionPool, deadlinedays);
				
				//Label test = new Label(c.getTime().toString());
				
				//addComponent(test);
				for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();) {
					//Notification.show("HERE");
					Meal meal = j.next();
					NativeSelect options = 
							new MealOptionComboBox(meal, regime, c, dailymealselection, periods, mealoptiondeadlines, deadlinedays);	
					addComponent(options);
					
				}

			
		}
		catch (SQLException e) {
			throw new RuntimeException (e);
		}



	}
}
