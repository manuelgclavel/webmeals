package com.example.mobile.presenter;

import java.sql.Connection;
//import java.sql.Date;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.User;
import com.example.mobile.viewer.MealOptionComboBox;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealOptionComboBoxBehavior implements ValueChangeListener {

	final private JDBCConnectionPool connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();
	final private User curUser = ((MobileUI) UI.getCurrent()).getUser();
	final private java.util.Date dayselected;
	final private DailyMealSelection dailymealselection;
	final private Meal mealselected;
	final private MealSelection curMealSelection;
	final private MealOption curMealOption;
	final private MealOptionComboBox combobox;

	private MealOption selMealOption;

	public MealOptionComboBoxBehavior(Date dayselected, 
			DailyMealSelection dailymealselection, 
			Meal mealselected, MealSelection curMealSelection, 
			MealOption curMealOption, MealOptionComboBox combobox){
		
		this.curMealSelection = curMealSelection;
		this.curMealOption = curMealOption;
		this.dayselected = dayselected;
		this.mealselected = mealselected;
		this.dailymealselection = dailymealselection;
		this.combobox = combobox;

	}


	@Override
	public void valueChange (ValueChangeEvent event) {
		// TODO Auto-generated method stub

		PreparedStatement ps;
		ResultSet result;

		/**
		 * Get the SELECTED meal option, which may be null, i.e., the undefined value in the combo-box
		 */
		
		selMealOption = (MealOption) event.getProperty().getValue();
		//if (selMealOption == null) {
		//	Notification.show("YES!");
		//};

		//if (curMealOption == null){
		//	Notification.show("VAMOS!");
		//}

		Boolean proceed = false;
		if ((curMealOption == null) & (selMealOption == null)) {
			proceed = false; 
		}
		else if ((curMealOption == null) & !(selMealOption == null)){
			proceed = true ; 
		}
		else if (!(curMealOption == null) & (selMealOption == null)) {
			proceed = true;
		}
		else if (!(curMealOption == null) & !(selMealOption == null)) {
			proceed = !(curMealOption.getPk() == selMealOption.getPk());
		}


		if (proceed){
			try {
				Connection conn = connectionPool.reserveConnection();


				/**
				 * Check that the selection is AUTHORIZED
				 * RECALL: Monday = 0,... Sunday=6, as in WEEKDAY()
				 */

				/**
				 * Get the deadline for the current meal option and day of the week
				 */
				MealOptionDeadline curOptionDeadline = null;
				if (!(curMealOption == null)){
					ps = conn.prepareStatement("select count(*), pk, cday, chour, cminute, ownedBy from MealOptionDeadline where pk in" + " " +
							"(select entity from MealOptionDeadline_days" + " " +
							"where elements = WEEKDAY(Date(?)) and entity in (" + " " +
							"select pk from MealOptionDeadline where pk in (" + " " +
							"select MealOptionDeadline_ownedBy from" + " " + 
							"MealOptionDeadline_ownedBy__Residency_deadlines where Residency_deadlines" + " " +
							"IN (select pk from Residency where ownedByOption = ?" + " " +
							"and Date(?) >= Date(start)  and Date(?) <= Date(end)))))");

					ps.setDate(1, new java.sql.Date(dayselected.getTime()));
					ps.setInt(2, curMealOption.getPk());
					ps.setDate(3, new java.sql.Date(dayselected.getTime()));
					ps.setDate(4, new java.sql.Date(dayselected.getTime()));

					result = ps.executeQuery();
					result.next();
					if (result.getInt(1) > 0){
						curOptionDeadline = new MealOptionDeadline(result.getInt(2), result.getInt(3), 
								result.getInt(4), result.getInt(5), result.getInt(6));
					}
					result.close();
					ps.close();
				}
				/**
				 * Get the deadline for the given meal option and day of the week
				 */
				MealOptionDeadline selOptionDeadline = null;
				if (!(selMealOption == null)){
					ps = conn.prepareStatement("select count(*), pk, cday, chour, cminute, ownedBy from MealOptionDeadline where pk in" + " " +
							"(select entity from MealOptionDeadline_days" + " " +
							"where elements = WEEKDAY(Date(?)) and entity in (" + " " +
							"select pk from MealOptionDeadline where pk in (" + " " +
							"select MealOptionDeadline_ownedBy from" + " " + 
							"MealOptionDeadline_ownedBy__Residency_deadlines where Residency_deadlines" + " " +
							"IN (select pk from Residency where ownedByOption = ?" + " " +
							"and Date(?) >= Date(start)  and Date(?) <= Date(end)))))");

					ps.setDate(1, new java.sql.Date(dayselected.getTime()));
					ps.setInt(2, selMealOption.getPk());
					ps.setDate(3, new java.sql.Date(dayselected.getTime()));
					ps.setDate(4, new java.sql.Date(dayselected.getTime()));

					result = ps.executeQuery();
					result.next();

					if (result.getInt(1) > 0){
						selOptionDeadline = new MealOptionDeadline(result.getInt(2), result.getInt(3), 
								result.getInt(4), result.getInt(5), result.getInt(6));
					}
					result.close();
					ps.close();			
				}


				if (checkDeadlines(curOptionDeadline, selOptionDeadline)){

					if (!(curMealSelection == null)) {
						ps = conn.prepareStatement("UPDATE MealSelection SET mealoption = ? where pk = ?");
						if (!(selMealOption == null)){
							ps.setInt(1, selMealOption.getPk());
							ps.setInt(2, curMealSelection.getPk());
							ps.executeUpdate();
						} else {
							//Notification.show("THERE!");
							//ps.setString(1, "NULL");
							//ps.setInt(2, curMealSelection.getPk());
							Notification.show("WHAT?");
							ps = conn.prepareStatement("DELETE FROM MealSelection where pk = ?");
							ps.setInt(1, curMealSelection.getPk());
							ps.executeUpdate();
						}
						
					} 
					else {	
						/** Note that, in this case, selMealOption cannot be null */
						ps = conn.prepareStatement("INSERT INTO MealSelection" + " " + 	"(mealOption,meal, ownedBy) values (?,?,?)");
						ps.setInt(1, selMealOption.getPk());
						ps.setInt(2, mealselected.getPk());
						ps.setInt(3, dailymealselection.getPk());
						ps.executeUpdate();

						ps = conn.prepareStatement("INSERT INTO DailyMealSelection_selections__MealSelection_ownedBy" + " " +  
								"(DailyMealSelection_selections,MealSelection_ownedBy) values (?,LAST_INSERT_ID())");
						ps.setInt(1, dailymealselection.getPk());
						ps.executeUpdate();

						ps = conn.prepareStatement("INSERT INTO DailyMealSelection_selectedBy__User_dailyMeals" + " " + 
								"(DailyMealSelection_selectedBy, User_dailyMeals) values (?,?)");
						ps.setInt(1, dailymealselection.getPk());
						ps.setInt(2, curUser.getPk());
						ps.executeUpdate();

					}
					ps.close();
					conn.commit();
					Notification.show("DONE!");

				} 
				else { 

					/** WORKING HERE **/
					refreshMealSelection();
					Notification.show("MESSAGE: Sorry, you are late.");
					/**
							Notification.show(Integer.valueOf(deadline.get(Calendar.DAY_OF_MONTH)).toString()
									+ " " 
									+ Integer.valueOf(deadline.get(Calendar.HOUR_OF_DAY)).toString()
									+ ":"
									+ Integer.valueOf(deadline.get(Calendar.MINUTE)).toString()
									+ " < "
									+ Integer.valueOf(current.get(Calendar.DAY_OF_MONTH)).toString()
									+ " " 
									+ Integer.valueOf(current.get(Calendar.HOUR_OF_DAY)).toString()
									+ ":" 
									+ Integer.valueOf(current.get(Calendar.MINUTE)).toString(),	
									Notification.Type.WARNING_MESSAGE);	
					 */						

				}

				connectionPool.releaseConnection(conn);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


private void refreshMealSelection(){
	try {
		Connection conn = connectionPool.reserveConnection();
		PreparedStatement ps;
		ResultSet result;
		ps = conn.prepareStatement("SELECT count(*), mealoption FROM MealSelection" + " " +
				"WHERE meal = ? and pk in " + " " +
				"(SELECT * FROM" + " " + 
				"(SELECT MealSelection_ownedBy from DailyMealSelection_selections__MealSelection_ownedBy" + " " +
				"WHERE DailyMealSelection_selections = " + " " +
				"(SELECT pk from DailyMealSelection" + " " +  
				"WHERE Date(date) = ?  and selectedBy = ?)) AS TEMP)");
		ps.setInt(1, mealselected.getPk());
		ps.setDate(2, new java.sql.Date(dayselected.getTime()));
		ps.setInt(3, curUser.getPk());
		result = ps.executeQuery();
		result.next();
		/** RECALL that the stored value may be NULL, i.e., different from all meal options **/
		if (result.getInt(1) > 0){
			MealOption row;
			for (Iterator<MealOption> k = (Iterator<MealOption>) combobox.getContainerDataSource().getItemIds().iterator(); k.hasNext();) {
				row = k.next();
				if (row.getPk() == result.getInt(2)){
					combobox.setValue(row);				
				}
			}			
		} else {
			combobox.setValue(null);
		}
		result.close();
		ps.close();	
		connectionPool.releaseConnection(conn);

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}	


private Boolean checkDeadlines(MealOptionDeadline curOption, MealOptionDeadline selOption){
	final String timezone = ((MobileUI) UI.getCurrent()).getResidence().getZone();
	final Calendar current = Calendar.getInstance(TimeZone.getTimeZone(timezone));
	Calendar curDeadline = null;
	Calendar selDeadline = null;
	Boolean check = false;


	if (!(curOption == null)){
		curDeadline = Calendar.getInstance(TimeZone.getTimeZone(timezone));	
		curDeadline.setTimeInMillis(dayselected.getTime());
		curDeadline.set(curDeadline.get(Calendar.YEAR), 
				curDeadline.get(Calendar.MONDAY), 
				curDeadline.get(Calendar.DAY_OF_MONTH), 
				0, 0, 0);
		curDeadline.add(Calendar.MILLISECOND, - curDeadline.get(Calendar.MILLISECOND));
		curDeadline.add(Calendar.DAY_OF_MONTH, - curOption.getCday());
		curDeadline.add(Calendar.HOUR_OF_DAY, + curOption.getChour());
		curDeadline.add(Calendar.MINUTE, + curOption.getCminute());
	}

	if (!(selOption == null)){
		selDeadline = Calendar.getInstance(TimeZone.getTimeZone(timezone));	
		selDeadline.setTimeInMillis(dayselected.getTime());
		selDeadline.set(selDeadline.get(Calendar.YEAR), 
				selDeadline.get(Calendar.MONDAY), 
				selDeadline.get(Calendar.DAY_OF_MONTH), 
				0, 0, 0);
		selDeadline.add(Calendar.MILLISECOND, - selDeadline.get(Calendar.MILLISECOND));
		selDeadline.add(Calendar.DAY_OF_MONTH, - selOption.getCday());
		selDeadline.add(Calendar.HOUR_OF_DAY, + selOption.getChour());
		selDeadline.add(Calendar.MINUTE, + selOption.getCminute());
	}

	if ((curOption == null) & (selOption == null)) {
		check = true; }
	else if ((curOption == null) & !(selOption == null)){
		check = current.before(selDeadline); 
	}
	else if (!(curOption == null) & (selOption == null)) {
		check = current.before(curDeadline);
	}
	else if (!(curOption == null) & !(selOption == null)) {
		check = current.before(curDeadline) & current.before(selDeadline);
	}
	return check;
}
	

}
