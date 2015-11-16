package com.example.mobile.viewer;

import java.util.Iterator;
import java.sql.Connection;
//import java.sql.Date;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.mobile.MobileUI;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.User;
import com.example.mobile.presenter.PopupRegimeBehavior;
import com.example.mobile.presenter.PopupUserBehavior;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class CountView extends VerticalComponentGroup {
	private MobileUI mealcount;
	private JDBCConnectionPool connectionPool;

	public CountView (Date selected){

		this.mealcount = ((MobileUI) UI.getCurrent());
		this.connectionPool = mealcount.getConnectionPool();

		//setMargin(true);
		//setSpacing(true);

		try {
			Connection conn = connectionPool.reserveConnection();

			BeanItemContainer<Meal> meals = new BeanItemContainer<Meal>(Meal.class);
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal ORDER BY position");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				Meal nextMeal = new Meal(result.getInt(1), result.getInt(2), result.getString(3), result.getInt(4));
				meals.addItem(nextMeal);				
			}
			result.close();
			ps.close();

			BeanItemContainer<FoodRegime> regimenes = new BeanItemContainer<FoodRegime>(FoodRegime.class);
			ps = conn.prepareStatement("SELECT * FROM FoodRegime ORDER BY name");
			result = ps.executeQuery();
			while (result.next()){
				FoodRegime nextRegime = new FoodRegime(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4));
				regimenes.addItem(nextRegime);				
			}
			result.close();
			ps.close();

			ps = conn.prepareStatement("DROP VIEW if EXISTS DailyMealSelectionView");
			ps.execute();
			ps.close();
			ps= conn.prepareStatement("DROP VIEW IF EXISTS MealSelectionView");
			ps.execute();
			ps.close();
			ps = conn.prepareStatement("DROP VIEW IF EXISTS FullMealSelectionView");
			ps.execute();
			ps.close();

			//ps = conn.prepareStatement("CREATE VIEW DailyMealSelectionView as" + " " +
			//		"SELECT pk as dailymealselection, selectedBy as user, offeredTo as guest" + " " +
			//		"FROM DailyMealSelection WHERE Date(date) = ?");
			
			ps = conn.prepareStatement("CREATE VIEW DailyMealSelectionView AS" + " " +
					"SELECT pk as dailymealselection, selectedby as user, offeredTo as guest" + " " +
					"FROM DailyMealSelection WHERE Date(date) = ?");
			
			ps.setDate(1, new java.sql.Date(selected.getTime()));
			ps.execute();
			ps.close();

			ps= conn.prepareStatement("CREATE VIEW MealSelectionView AS" + " " +
					"SELECT MealSelection_ownedBy as mealselection, user, guest, dailymealselection " + " " +
					"FROM DailyMealSelection_selections__MealSelection_ownedBy " + " " +
					"INNER JOIN DailyMealSelectionView " + " " +
					"ON DailyMealSelectionView.dailymealselection=DailyMealSelection_selections");
			
			//ps= conn.prepareStatement("CREATE VIEW MealSelectionView AS" + " " +
			//		"SELECT MealSelection_ownedBy as mealselection, user, guest, dailymealselection" + " " +
			//		"FROM DailyMealSelection_selections__MealSelection_ownedBy " + " " +
			//		"WHERE DailyMealSelection_selections IN  (select *  from DailyMealSelectionView)");
			
			
			ps.execute();
			ps.close();

			//ps= conn.prepareStatement("CREATE VIEW FullMealSelectionView AS" + " " +
			//		"SELECT mealOption, foodRegime, meal, user, guest" + " " +
			//		"FROM MealSelection" + " " +
			//		"INNER JOIN MealSelectionView" + " " +
			//		"ON MealSelection.pk=MealSelectionView.mealselection");
			
			ps= conn.prepareStatement("CREATE VIEW FullMealSelectionView AS" + " " +
					"SELECT mealOption, foodRegime, user, guest" + " " +
					"FROM MealSelection" + " " +
					"INNER JOIN MealSelectionView" + " " +
					"ON MealSelection.pk = MealSelectionView.mealselection");
			ps.execute();
			ps.close();


			for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();) {
				Meal meal = j.next();
				Label mealName = new Label("<b>" + meal.getLiteral() + "</b>", ContentMode.HTML);
				addComponent(mealName);

				BeanItemContainer<MealOption> mealOptions = new BeanItemContainer<MealOption>(MealOption.class);
				ps = conn.prepareStatement("SELECT * FROM MealOption WHERE ownedBy = ? ORDER BY position");
				ps.setInt(1, meal.getPk());
				result = ps.executeQuery();
				while (result.next()){
					MealOption nextMealOption = new MealOption(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4), result.getInt(5));
					mealOptions.addItem(nextMealOption);				
				}
				result.close();
				ps.close();

				for (Iterator<MealOption> i = mealOptions.getItemIds().iterator(); i.hasNext();) {
					MealOption mealoption = i.next();
					//MealOption mealoption = i.;
					ps = conn.prepareStatement("SELECT count(*) FROM FullMealSelectionView WHERE mealOption = ?");
					ps.setInt(1, mealoption.getPk());
					//ps.setInt(1, 5);
					result = ps.executeQuery();
					result.next();
					int optionCount = result.getInt(1);
					
					result.close();
					ps.close();
					if (!(optionCount == 0)){
					/**
						BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);
						ps = conn.prepareStatement("select User.pk, User.surname, User.name from User" + " " +
								"INNER JOIN (select * from FullMealSelectionView where mealoption = ?) as MealOptionTempA" + " " +
								"ON User.pk=MealOptionTempA.user" + " " +
								"UNION" + " " + 
								"select Guest.pk, Guest.surname, Guest.name from Guest" + " " +
								"INNER JOIN (select * from FullMealSelectionView where mealoption = ?) as MealOptionTempB" + " " +
								"ON Guest.pk=MealOptionTempB.guest ORDER BY surname, name");
						ps.setInt(1, mealoption.getPk());
						ps.setInt(2, mealoption.getPk());
						result = ps.executeQuery();
						while (result.next()){
							User nextUser = new User(result.getInt(1), result.getString(2), result.getString(3));
							users.addItem(nextUser);				
						}
						result.close();
						ps.close();
						*/
						
						HorizontalLayout rowMealCount = new HorizontalLayout();
						rowMealCount.setSpacing(true);
						Button popup = new Button (mealoption.getInitial() + " : " + Integer.valueOf(optionCount).toString());
						
						popup.addClickListener(new PopupUserBehavior(selected, meal, mealoption));
						/** */
						rowMealCount.addComponent(popup);
						
						HorizontalLayout regimenesPanel = new HorizontalLayout();
						regimenesPanel.setSpacing(true);	
						
						for (Iterator<FoodRegime> k = regimenes.getItemIds().iterator(); k.hasNext();) {
							FoodRegime regime = k.next();
							ps = conn.prepareStatement("SELECT count(*) FROM FullMealSelectionView WHERE mealOption = ? and foodRegime = ?");
							ps.setInt(1, mealoption.getPk());
							ps.setInt(2, regime.getPk());
							result = ps.executeQuery();
							result.next();
							int regimenCount = result.getInt(1);
							result.close();
							ps.close();
							if (!(regimenCount == 0)){
								
								HorizontalLayout rowRegimeCount = new HorizontalLayout();
								rowRegimeCount.setSpacing(true);
								Button regimePopup = new Button ("#" + regime.getName() + " (" + Integer.valueOf(regimenCount).toString() + ")");
				
								regimePopup.addClickListener(new PopupRegimeBehavior(selected,meal,mealoption,regime));
								regimenesPanel.addComponent(regimePopup);	
							}
						}
						
						rowMealCount.addComponent(regimenesPanel);
						addComponent(rowMealCount);
					}
				}
			}
			connectionPool.releaseConnection(conn);
		}
		catch (SQLException e) {
			throw new RuntimeException (e);
		}
	}
	
}

