package com.example.mobile.presenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.example.mobile.MobileUI;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class PopupRegimeBehavior implements ClickListener {
	private MobileUI mealcount;
	private JDBCConnectionPool connectionPool;

	private Date dayselected;
	private Meal meal;
	private MealOption mealoption;
	private FoodRegime regime;

	public PopupRegimeBehavior(Date selected, Meal meal, MealOption mealoption, FoodRegime regime) {
		this.mealcount = ((MobileUI) UI.getCurrent());
		this.connectionPool = mealcount.getConnectionPool();
		this.dayselected = selected;
		this.meal = meal;
		this.mealoption = mealoption;
		this.regime = regime;
		

	}
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
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
			
			ps.setDate(1, new java.sql.Date(dayselected.getTime()));
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


			ps = conn.prepareStatement("SELECT count(*) FROM FullMealSelectionView WHERE mealOption = ?");
			ps.setInt(1, mealoption.getPk());
			//ps.setInt(1, 5);
			result = ps.executeQuery();
			result.next();
			int optionCount = result.getInt(1);

			result.close();
			ps.close();
			if (!(optionCount == 0)){
				
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
			
				

				// Show it relative to the navigation bar of
				// the current NavigationView.
				CssLayout layout = new CssLayout();
				//Label test = new Label(mealoption.getLiteral());
				//Label num = new Label(Integer.valueOf(mealoption.getPk()).toString());
				//Label count = new Label(Integer.valueOf(i))

				Table userstable = new Table("", regimeusers);
				userstable.setPageLength(userstable.size());
				userstable.setVisibleColumns(new Object[] {"name", "surname"});
				Popover popover = new Popover();
				//layout.addComponent(test);
				//layout.addComponent(num);

				layout.addComponent(userstable);
				popover.setContent(layout);   
				popover.showRelativeTo(event.getButton());

						
			}
			connectionPool.releaseConnection(conn);
		}
		catch (SQLException e) {
			throw new RuntimeException (e);
		}	
	}
}
