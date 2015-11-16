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
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

			ps = conn.prepareStatement("CREATE VIEW DailyMealSelectionView as" + " " +
					"SELECT pk as dailymealselection, selectedBy as user, offeredTo as guest" + " " +
					"FROM DailyMealSelection WHERE Date(date) = ?");
			ps.setDate(1, new java.sql.Date(selected.getTime()));
			ps.execute();
			ps.close();

			ps= conn.prepareStatement("CREATE VIEW MealSelectionView AS" + " " +
					"SELECT MealSelection_ownedBy as mealselection, user, guest, dailymealselection " + " " +
					"FROM DailyMealSelection_selections__MealSelection_ownedBy " + " " +
					"INNER JOIN DailyMealSelectionView " + " " +
					"ON DailyMealSelectionView.dailymealselection=DailyMealSelection_selections");
			ps.execute();
			ps.close();

			ps= conn.prepareStatement("CREATE VIEW FullMealSelectionView AS" + " " +
					"SELECT mealOption, foodRegime, meal, user, guest" + " " +
					"FROM MealSelection" + " " +
					"INNER JOIN MealSelectionView" + " " +
					"ON MealSelection.pk=MealSelectionView.mealselection");
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
					String optionName = mealoption.getLiteral();
					ps = conn.prepareStatement("SELECT count(*) FROM FullMealSelectionView WHERE mealOption = ?");
					ps.setInt(1, mealoption.getPk());
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


						//VerticalComponentGroup popupContent = new VerticalComponentGroup();
						//Table userstable = new Table("", users);
						//userstable.setVisibleColumns(new Object[] {"name", "surname"});
						//popupContent.addComponent(userstable);


						HorizontalLayout rowMealCount = new HorizontalLayout();
						rowMealCount.setSpacing(true);
						Label label = new Label(optionName);
						Label popup = new Label(Integer.valueOf(optionCount).toString());
						//PopupView popup = new PopupView(Integer.valueOf(optionCount).toString(), popupContent);

						/** */
						Button test = new Button("Test");
						test.addClickListener(new ClickListener() {
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								 
								try {
									PreparedStatement ps;
									ResultSet result;
									Connection conn;
									conn = ((MobileUI) UI.getCurrent()).getConnectionPool().reserveConnection();
									ps = conn.prepareStatement("select User.pk, User.surname, User.name from User");
									//ps.setInt(1, mealoption.getPk());
									//ps.setInt(2, mealoption.getPk());


									result = ps.executeQuery();
									Popover popover = new Popover();	
									BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);

									while (result.next()){
										User nextUser = new User(result.getInt(1), result.getString(2), result.getString(3));
										users.addItem(nextUser);				
									}
									result.close();
									ps.close();

									// Show it relative to the navigation bar of
									// the current NavigationView.
									Table userstable = new Table("", users);
									userstable.setVisibleColumns(new Object[] {"name", "surname"});
									popover.setContent(userstable);   
									popover.showRelativeTo(event.getButton());
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								 
							}
						});
						
						
						rowMealCount.addComponent(label);
						rowMealCount.addComponent(popup);
						rowMealCount.addComponent(test);
						


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

								BeanItemContainer<User> regimeusers = new BeanItemContainer<User>(User.class);
								ps = conn.prepareStatement("select User.pk, User.surname, User.name from User" + " " +
										"INNER JOIN (select * from FullMealSelectionView where mealoption = ? and foodRegime = ?) as MealOptionTempA" + " " +
										"ON User.pk=MealOptionTempA.user" + " " +
										"UNION" + " " + 
										"select Guest.pk, Guest.surname, Guest.name from Guest" + " " +
										"INNER JOIN (select * from FullMealSelectionView where mealoption = ? and foodRegime = ?) as MealOptionTempB" + " " +
										"ON Guest.pk=MealOptionTempB.guest ORDER BY surname, name");
								ps.setInt(1, mealoption.getPk());
								ps.setInt(2, regime.getPk());
								ps.setInt(3, mealoption.getPk());
								ps.setInt(4, regime.getPk());
								result = ps.executeQuery();
								while (result.next()){
									User nextUser = new User(result.getInt(1), result.getString(2), result.getString(3));
									regimeusers.addItem(nextUser);				
								}
								result.close();
								ps.close();

								//VerticalComponentGroup regimeuserspopup = new VerticalComponentGroup();
								//Table regimeuserstable = new Table("", regimeusers);
								//regimeuserstable.setVisibleColumns(new Object[] {"name","surname"});
								//regimeuserspopup.addComponent(regimeuserstable);

								Label regimeNameLabel = new Label("<b>" + regime.getName() + "</b>", ContentMode.HTML);
								//regimeNameLabel.setWidth(50, Unit.PIXELS);
								Label regimePopup = new Label("(" + Integer.valueOf(regimenCount).toString() + ")");
								
								//PopupView regimePopup = new PopupView("(" + Integer.valueOf(regimenCount).toString() + ")", regimeuserspopup);
								//regimePopup.setWidth(10, Unit.PIXELS);

								regimenesPanel.addComponent(regimeNameLabel);
								regimenesPanel.addComponent(regimePopup);	
							}
						}
						
						rowMealCount.addComponent(regimenesPanel);
						addComponent(rowMealCount);
					}
				}
			}
			conn.close();
		}
		catch (SQLException e) {
			throw new RuntimeException (e);
		}
	}
}

