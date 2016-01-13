package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.Residency;
import com.example.mobile.presenter.MealOptionComboBoxBehavior;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealOptionComboBox extends NativeSelect {

	final public MobileUI ui = (MobileUI) UI.getCurrent();
	final public JDBCConnectionPool connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();
	final public Meal mealselected;
	final public DailyMealSelection dailymealselection;
	final public FoodRegime activeregime;
	final public BeanItemContainer<Residency> periods;
	final public BeanItemContainer<MealOptionDeadline> mealoptiondeadlines;
	final public BeanItemContainer<DeadlineDay> deadlinedays;
	
	private MealSelection curMealSelection;
	private MealOption curMealOption;
	private GregorianCalendar gcalendar;
	
	private String test;
	

	public MealOptionComboBox(Meal meal, FoodRegime regime, GregorianCalendar calendar, DailyMealSelection dailymeal, 
			BeanItemContainer<Residency> periods, 
			BeanItemContainer<MealOptionDeadline> mealoptiondeadlines, 
			BeanItemContainer<DeadlineDay> deadlinedays){
		this.mealselected = meal;
		this.dailymealselection = dailymeal;
		this.activeregime = regime;
		this.periods = periods;
		this.mealoptiondeadlines = mealoptiondeadlines;
		this.deadlinedays = deadlinedays;
		this.setGcalendar(calendar);
		
		
		
		this.setNullSelectionAllowed(true);
		
		
		Connection conn;
		try {
			conn = connectionPool.reserveConnection();
			BeanItemContainer<MealOption> mealOptions = 
					new BeanItemContainer<MealOption>(MealOption.class);
			PreparedStatement ps;
			ResultSet result;
			ps = conn.prepareStatement("SELECT pk, position, initial, literal, ownedBy FROM MealOption WHERE ownedBy = ? ORDER BY position");
			ps.setInt(1, mealselected.getPk());
			result = ps.executeQuery();
			
			while (result.next()){
				mealOptions.addItem(new MealOption(result.getInt(1), result.getInt(2),
						result.getString(3), result.getString(4), result.getInt(5)));
			}
			result.close();
			ps.close();
			
			
			
			
			this.setContainerDataSource(mealOptions);
			this.setCaption(mealselected.getLiteral());

			this.setImmediate(true);
			this.setNullSelectionAllowed(true);
			this.setItemCaptionPropertyId("initial");
			
			
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

			//MealOption curMealOption = null;
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
				for (Iterator<MealOption> k = mealOptions.getItemIds().iterator(); k.hasNext();) {
					MealOption row = k.next();
					if (row.getPk() == curMealOption.getPk()){
						this.setValue(row);
					}
				}
			} else{
				this.setValue(null);
			}

			
			
			conn.close();
			connectionPool.releaseConnection(conn);

			
						
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addValueChangeListener(new MealOptionComboBoxBehavior(
				gcalendar, dailymealselection, activeregime, mealselected,
				this));		
		
		
		
	}




	public GregorianCalendar getGcalendar() {
		return gcalendar;
	}




	public void setGcalendar(GregorianCalendar gcalendar) {
		this.gcalendar = gcalendar;
	}

}	 
