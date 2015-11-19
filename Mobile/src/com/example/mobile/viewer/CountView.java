package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealCount;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.User;
import com.example.mobile.presenter.UserPopupBehavior;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class CountView extends VerticalComponentGroup {
	private JDBCConnectionPool connectionPool;
	private BeanItemContainer<Meal> meals;
	private BeanItemContainer<MealOption> mealoptions;
	private BeanItemContainer<FoodRegime> regimenes;
	
	public CountView (Date selected){
		this.connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();

		//setMargin(true);
		//setSpacing(true);

		try {
			Connection conn = connectionPool.reserveConnection();

			meals = new BeanItemContainer<Meal>(Meal.class);
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal ORDER BY position");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				Meal nextMeal = new Meal(result.getInt(1), result.getInt(2), result.getString(3), result.getInt(4)); 
						
				meals.addItem(nextMeal);				
			}
			result.close();
			ps.close();

			mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
			ps = conn.prepareStatement("SELECT * FROM MealOption ORDER BY position");
			result = ps.executeQuery();
			while (result.next()){
				MealOption nextMealOption = new MealOption(result.getInt(1), result.getInt(2), result.getString(3), 
						result.getString(4), result.getInt(5));
				mealoptions.addItem(nextMealOption);				
			}
			result.close();
			ps.close();
			
			regimenes = new BeanItemContainer<FoodRegime>(FoodRegime.class);
			ps = conn.prepareStatement("SELECT * FROM FoodRegime ORDER BY name");
			result = ps.executeQuery();
			while (result.next()){
				FoodRegime nextRegime = new FoodRegime(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4));
				regimenes.addItem(nextRegime);				
			}
			result.close();
			ps.close();

			BeanItemContainer<MealCount> mealcounts;
			mealcounts = new BeanItemContainer<MealCount>(MealCount.class);
			ps = conn.prepareStatement("select  mealOption, foodRegime, meal, daily, username, " + " " +
					"usersurname, selectedBy," + " " + 
					"offeredTo, guestname, guestsurname from MealSelection" + " " +
					"INNER JOIN" + " " +
					"(select TEMP2.daily as daily, TEMP2.username as username, " + " " +
					"TEMP2.usersurname as usersurname, TEMP2.selectedBy as selectedBy," + " " +   
					"TEMP2.offeredTo as offeredTo," + " " +
					"Guest.name as guestname, " + " " +
					"Guest.surname as guestsurname from Guest" + " " + 
					"RIGHT JOIN" + " " +
					"(select TEMP1.pk as daily, TEMP1.offeredTo as offeredTo, " + " " +
					"User.name as username, User.surname as usersurname," + " " +
					"TEMP1.selectedBy as selectedBy from User" + " " + 
					"RIGHT JOIN" + " " + 
					"(select * from DailyMealSelection where Date(date) = ?) as TEMP1" + " " + 
					"on  User.pk = TEMP1.selectedBy) as TEMP2" + " " +
					"on Guest.pk = TEMP2.offeredTo) as TEMP3" + " " +
					"on MealSelection.ownedBy = TEMP3.daily");			
			ps.setDate(1, new java.sql.Date(selected.getTime()));
			result = ps.executeQuery();
			
			while (result.next()){
				MealCount nextCount = new MealCount(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4),
						result.getString(5), result.getString(6), result.getInt(7), result.getInt(8),
						result.getString(9), result.getString(10));
				mealcounts.addItem(nextCount);				
			}
			result.close();
			ps.close();
			for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();) {
				Meal meal = j.next();
				Label mealName = new Label("<b>" + meal.getLiteral() + "</b>", ContentMode.HTML);
				addComponent(mealName);
				mealoptions.removeAllContainerFilters();
				mealoptions.addContainerFilter(new Compare.Equal("ownedBy", meal.getPk()));
				
				for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();) {
					MealOption mealoption = i.next();
					mealcounts.removeAllContainerFilters();
					mealcounts.addContainerFilter(new Compare.Equal("mealOption", mealoption.getPk()));
					int optionCount = mealcounts.size();
					if (!(optionCount == 0)){
						
						HorizontalLayout rowMealCount = new HorizontalLayout();
						rowMealCount.setSpacing(true);
						Button popup = new Button (mealoption.getInitial() + " : " + Integer.valueOf(optionCount).toString());
						
						BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);					
						for (Iterator<MealCount> h = mealcounts.getItemIds().iterator(); h.hasNext();) {
							MealCount mealcount = h.next();
							users.addItem(new User(mealcount.getUsersurname(),mealcount.getUsername()));
						}
								
						//popup.addClickListener(new UserPopupBehavior(new Table("", users)));
						popup.addClickListener(new UserPopupBehavior(users));
						/** */
						rowMealCount.addComponent(popup);
						
						HorizontalLayout regimenesPanel = new HorizontalLayout();
						regimenesPanel.setSpacing(true);
						
						
						for (Iterator<FoodRegime> k = regimenes.getItemIds().iterator(); k.hasNext();) {
							FoodRegime regime = k.next();
							mealcounts.removeAllContainerFilters();
							mealcounts.addContainerFilter(new And(new Compare.Equal("mealOption", mealoption.getPk()),
																new Compare.Equal("foodRegime", regime.getPk())));
							int regimeCount = mealcounts.size();
							if (!(regimeCount == 0)){
								
								HorizontalLayout rowRegimeCount = new HorizontalLayout();
								rowRegimeCount.setSpacing(true);
								Button regimepopup = new Button ("#" + regime.getName() + " (" + Integer.valueOf(regimeCount).toString() + ")");
								
								BeanItemContainer<User> regimeusers = new BeanItemContainer<User>(User.class);					
								for (Iterator<MealCount> h = mealcounts.getItemIds().iterator(); h.hasNext();) {
									MealCount mealcount = h.next();
									regimeusers.addItem(new User(mealcount.getUsersurname(),mealcount.getUsername()));
								}
								
								//regimepopup.addClickListener(new UserPopupBehavior(new Table("", regimeusers)));
								regimepopup.addClickListener(new UserPopupBehavior(regimeusers));
								
								regimenesPanel.addComponent(regimepopup);	
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