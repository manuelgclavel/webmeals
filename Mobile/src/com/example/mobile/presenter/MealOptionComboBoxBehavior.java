package com.example.mobile.presenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.FoodRegime;
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
	private MobileUI ui = (MobileUI) UI.getCurrent();
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	private User curUser = ui.getUser();
	private DailyMealSelection dailymealselection;
	private Meal mealselected;
	private MealSelection curMealSelection;
	private MealOption curMealOption;
	private FoodRegime activeregime;
	private MealOptionComboBox combobox;

	private MealOption selMealOption;
	private GregorianCalendar gcalendar = ui.createGCalendarNoTime();

	public MealOptionComboBoxBehavior(GregorianCalendar calendar, DailyMealSelection dailymealselection, 
			FoodRegime regime,
			Meal mealselected, 
			MealOptionComboBox component){

		this.mealselected = mealselected;
		this.dailymealselection = dailymealselection;
		this.activeregime = regime;
		this.combobox = component;
		this.gcalendar.setTime(calendar.getTime());

	}


	@Override
	public void valueChange (ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
		//Notification.show(gcalendar.getTime().toString());
		
		try{
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps;
			ResultSet result;

			/**
			 * select in combo-box the current stored option
			 */

			//MealSelection curMealSelection = null;
			curMealSelection = null;

			ps = conn.prepareStatement("SELECT count(*), pk, mealOption, foodRegime FROM MealSelection" + " " +
					"WHERE meal = ? and ownedBy = ?");
			ps.setInt(1, mealselected.getPk());
			ps.setInt(2, dailymealselection.getPk());
			result = ps.executeQuery();
			result.next();
			if (result.getInt(1) > 0){
				curMealSelection = new MealSelection(result.getInt(2), 
						result.getInt(3), result.getInt(4), mealselected.getPk(), dailymealselection.getPk());

			}

			result.close();
			ps.close();

			curMealOption = null;

			if (!(curMealSelection == null)){
				ps = conn.prepareStatement("SELECT count(*), pk, position, initial, literal, ownedBy FROM MealOption" + " " +
						"WHERE pk = ?");
				ps.setInt(1, curMealSelection.getMealOption());
				result = ps.executeQuery();
				result.next();
				if (result.getInt(1) > 0){
					curMealOption = new MealOption(result.getInt(2), 
							result.getInt(3), result.getString(4), result.getString(5), mealselected.getPk());
				}
			}

			/**
			 * Get the SELECTED meal option, which may be null, i.e., the undefined value in the combo-box
			 */

			selMealOption = (MealOption) event.getProperty().getValue();
			
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

				String foodregime;
				/** PREPARED FOOD REGIME */
				if (activeregime == null ){
					foodregime = "NULL";
				} else {
					foodregime = Integer.valueOf(activeregime.getPk()).toString();
				}

				/**
				 * Check that the selection is AUTHORIZED
				 * RECALL: Monday = 0,... Sunday=6, as in WEEKDAY()
				 */

				/**
				 * Get the deadline for the current meal option and day of the week
				 */
				MealOptionDeadline curOptionDeadline = null;
				if (!(curMealOption == null)){
					curOptionDeadline = combobox.ui.selectMealOptionDeadlineByDay(combobox.deadlinedays, gcalendar,
							combobox.ui.selectMealOptionDeadlineByPeriod(combobox.mealoptiondeadlines, 
									combobox.ui.selectPeriodsByDateAndMealOption(combobox.periods, gcalendar, curMealOption)));

	
					
				}
				/**
				 * Get the deadline for the given meal option and day of the week
				 */
				MealOptionDeadline selOptionDeadline = null;
				if (!(selMealOption == null)){
					selOptionDeadline = combobox.ui.selectMealOptionDeadlineByDay(combobox.deadlinedays,gcalendar,
							combobox.ui.selectMealOptionDeadlineByPeriod(combobox.mealoptiondeadlines, 
									combobox.ui.selectPeriodsByDateAndMealOption(combobox.periods, gcalendar, selMealOption)));

				}
				

				if (checkDeadlines(curOptionDeadline, selOptionDeadline)){

					if (!(curMealSelection == null)) {
						if (!(selMealOption == null)){
							ps = conn.prepareStatement("UPDATE MealSelection SET mealoption = ?, foodregime= ? where pk = ?");
							ps.setInt(1, selMealOption.getPk());
							ps.setString(2, foodregime);
							ps.setInt(3, curMealSelection.getPk());
							ps.executeUpdate();
							ps.close();

						} else {
							ps = conn.prepareStatement("DELETE FROM MealSelection where pk = ?");
							ps.setInt(1, curMealSelection.getPk());
							ps.executeUpdate();
							ps.close();
						}

					} 
					else {	
						/** Note that, in this case, selMealOption cannot be null */
						ps = conn.prepareStatement("INSERT INTO MealSelection" + " " + 	"(mealOption, foodregime, meal, ownedBy) values (?,?,?,?)");
						ps.setInt(1, selMealOption.getPk());
						ps.setString(2, foodregime);
						ps.setInt(3, mealselected.getPk());
						ps.setInt(4, dailymealselection.getPk());
						ps.executeUpdate();
						ps.close();

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
					conn.close();
					connectionPool.releaseConnection(conn);
					Notification.show("DONE!");

				} 
				else { 
					/** */
					GregorianCalendar current = ui.createGCalendarWithTime();
					GregorianCalendar curDeadline = ui.createGCalendarNoTime();
					GregorianCalendar selDeadline = ui.createGCalendarNoTime();
					
					if (!(curOptionDeadline == null)){
						curDeadline.setTime(gcalendar.getTime());
						curDeadline.add(Calendar.DATE, - curOptionDeadline.getCday());
						curDeadline.set(Calendar.HOUR_OF_DAY, curOptionDeadline.getChour());
						curDeadline.set(Calendar.MINUTE, curOptionDeadline.getCminute());
					}
					if  (!(selOptionDeadline == null)){
						selDeadline.setTime(gcalendar.getTime());
						selDeadline.add(Calendar.DATE, - selOptionDeadline.getCday());
						selDeadline.set(Calendar.HOUR_OF_DAY, selOptionDeadline.getChour());
						selDeadline.set(Calendar.MINUTE, selOptionDeadline.getCminute());
					}
					
					String preNote = "The selected day is: " + ui.displayDate(gcalendar) + ". ";
					String currentNote = "Now is: " + ui.displayFullDate(current) + ". ";
					String previousNote = "";
					if (!(curMealOption == null)){
						if (!(curOptionDeadline == null)){
							previousNote = "Previous selection can only be changed before: " + ui.displayFullDate(curDeadline) + ". ";
						}
					}
					String nextNote = "";
					if (!(selMealOption == null)){
						if (!(selOptionDeadline == null)){
							nextNote = "New selection can only be chosen before: "	+ ui.displayFullDate(selDeadline) + ".";
						} else {
							nextNote = "New selection cannot be chosen for the selected day.";
						}
					} 
					Notification.show("MESSAGE: Sorry. " + 
					preNote + currentNote + previousNote + nextNote);
					
					
					
					
					conn.close();
					connectionPool.releaseConnection(conn);
					if (!(curMealSelection == null)){
						
						for (Iterator<MealOption> k = (Iterator<MealOption>) combobox.getItemIds().iterator(); k.hasNext();) {
							MealOption row = k.next();
							if (row.getPk() == curMealOption.getPk()){
								combobox.setValue(row);
							}
						}
					} else{
						combobox.setValue(null);
					}
					
					
					
					//
					//
				}


			} 
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	

	private Boolean checkDeadlines(MealOptionDeadline curOption, MealOptionDeadline selOption){
		GregorianCalendar current = ui.createGCalendarWithTime();
		GregorianCalendar curDeadline = null;
		GregorianCalendar selDeadline = null;
		Boolean check = false;

		if (!(curOption == null)){
			curDeadline = ui.createGCalendarNoTime();
			curDeadline.setTime(gcalendar.getTime());
			curDeadline.add(Calendar.DAY_OF_MONTH, - curOption.getCday());
			curDeadline.set(Calendar.HOUR_OF_DAY, curOption.getChour());
			curDeadline.set(Calendar.MINUTE, curOption.getCminute());
		}

		if (!(selOption == null)){
			selDeadline = ui.createGCalendarNoTime();
			selDeadline.setTime(gcalendar.getTime());
			selDeadline.add(Calendar.DAY_OF_MONTH, - selOption.getCday());
			selDeadline.set(Calendar.HOUR_OF_DAY, selOption.getChour());
			selDeadline.set(Calendar.MINUTE, selOption.getCminute());
		}

		if ((curOption == null) & (selOption == null)) {
			if (!(curMealOption ==  null) || !(selMealOption == null)){
				// one of the two has no deadline rule assigned
				check = false;
			} else {
				// no change
				check = true; 
			}
		}
		else if ((curOption == null) & !(selOption == null)){
			if (!(curMealOption == null)){
				// curMealOption has no deadline rule assigned
				check = false;
			} else {
				// curMealOption was not assigned yet
				check = current.before(selDeadline); 
			}
		}
		else if (!(curOption == null) & (selOption == null)) {
			if (!(selMealOption == null)){
				// selMealOption has no deadline rule assigned
				check = false;
			} else {
				// selMealOption was not assigned yet
				check = current.before(curDeadline);
			} 
		}
		else if (!(curOption == null) & !(selOption == null)) {
			check = current.before(curDeadline) & current.before(selDeadline);
		}
		return check;
	}


}
