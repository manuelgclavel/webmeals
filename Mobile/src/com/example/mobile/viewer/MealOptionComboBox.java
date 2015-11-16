package com.example.mobile.viewer;

import java.sql.Connection;
//import java.sql.Date;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.User;
import com.example.mobile.presenter.MealOptionComboBoxBehavior;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealOptionComboBox extends NativeSelect {

	final private JDBCConnectionPool connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();
	final private java.util.Date dayselected;
	final private Meal mealselected;
	final private DailyMealSelection dailymealselection;
	
	private MealSelection curMealSelection;
	private MealOption curMealOption;
	



	MealOptionComboBox(Meal meal, java.util.Date selected, DailyMealSelection dailymeal){
		this.dayselected = new java.sql.Date(selected.getTime()); 
		this.mealselected = meal;
		this.dailymealselection = dailymeal;

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

			MealOption nextMealOption;
			while (result.next()){
				nextMealOption = new MealOption(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4), result.getInt(5));
				mealOptions.addItem(nextMealOption);				
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

			MealSelection curMealSelection = null;


			ps = conn.prepareStatement("SELECT count(*), pk, mealOption, foodRegime FROM MealSelection" + " " +
					"WHERE meal = ? and ownedBy = ?");
			ps.setInt(1, meal.getPk());
			ps.setInt(2, dailymealselection.getPk());
			result = ps.executeQuery();
			result.next();
			if (result.getInt(1) > 0){
				curMealSelection = new MealSelection(result.getInt(2), 
						result.getInt(3), result.getInt(4), meal.getPk(), dailymealselection.getPk());

			}

			result.close();
			ps.close();

			MealOption curMealOption = null;

			if (!(curMealSelection == null)){
				ps = conn.prepareStatement("SELECT count(*), pk, position, initial, literal, ownedBy FROM MealOption" + " " +
						"WHERE pk = ?");
				ps.setInt(1, curMealSelection.getMealOption());
				result = ps.executeQuery();
				result.next();
				if (result.getInt(1) > 0){
					curMealOption = new MealOption(result.getInt(2), 
							result.getInt(3), result.getString(4), result.getString(5), meal.getPk());
				}
				for (Iterator<MealOption> k = mealOptions.getItemIds().iterator(); k.hasNext();) {
					MealOption row = k.next();
					//Notification.show("THERE");
					if (row.getPk() == curMealOption.getPk()){
						this.setValue(row);
					}
				}
			} else{
				this.setValue(null);
			}

			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.addValueChangeListener(new MealOptionComboBoxBehavior(
				dayselected, dailymealselection, mealselected, curMealSelection, 
				curMealOption, this));			
	}

}	 
