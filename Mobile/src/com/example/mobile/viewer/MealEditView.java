package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Guest;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealEditView extends NavigationView {
	private JDBCConnectionPool connectionPool ;
	
	public MealEditView(){
		VerticalComponentGroup content = new VerticalComponentGroup();	
		connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();
		
			Connection conn;
			try {
				conn = connectionPool.reserveConnection();
				BeanItemContainer<Meal> meals = new BeanItemContainer<Meal>(Meal.class);
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal");
				ResultSet result = ps.executeQuery();
				while (result.next()){
					meals.addItem(new Meal(result.getInt(1), result.getInt(2), result.getString(3),
							result.getInt(4)));
				}
				result.close();
				ps.close();
				
				BeanItemContainer<MealOption> mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
				ps = conn.prepareStatement("SELECT * FROM MealOption");
				result = ps.executeQuery();
				while (result.next()){
					mealoptions.addItem(new MealOption(result.getInt(1), result.getInt(2), result.getString(3),
							result.getString(4), result.getInt(5)));
				}
				result.close();
				ps.close();
				
				
				
				
				connectionPool.releaseConnection(conn);
				
				Table mealsTable = new Table("",meals);
				content.addComponent(mealsTable);
				
				Table mealoptionsTable = new Table("",mealoptions);
				content.addComponent(mealoptionsTable);
				
				setContent(content);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	

}
