package com.example.mobile;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import com.example.mobile.MobileUI;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.Guest;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.Residence;
import com.example.mobile.data.Role;
import com.example.mobile.data.User;
import com.example.mobile.viewer.LoginView;
import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@Theme("touchkit")
//@Widgetset("com.example.myapp.MyAppWidgetSet")
@Title("My Mobile App")

public class MobileUI extends UI {
	
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MobileUI.class, widgetset = "com.example.mobile.widgetset.MobileWidgetset")
	public static class MobileServlet extends TouchKitServlet {
	}
	
	private JDBCConnectionPool connectionPool;
	private User user;
	private Role role;
	private Residence residence; 
	private NavigationManager manager;
	
	public static final String LOGIN_COOKIE = "loginmeals";
	public static final String PASSWORD_COOKIE = "passwordmeals";
	public static final Integer COOKIE_MAX_AGE = 31536000; /** 1 year */
	

	
    @Override
    protected void init(VaadinRequest request)  {
    	
    	try {
			connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/ravenahl","root","admin");	
			manager = new NavigationManager(new LoginView());
			setContent(manager);
			//setContent(new LoginView());
					
			
		} catch (SQLException e) {
    	 throw new RuntimeException(e);
		}	
        
    	
    }


	public JDBCConnectionPool getConnectionPool() {
		return connectionPool;
	}

	public User getUser() {
		return user;
	}
	
	public NavigationManager getManager () {
		return manager;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Residence getResidence() {
		return residence;
	}

	public void setResidence(Residence residence) {
		this.residence = residence;
	}
	


	
	
	//public java.sql.Timestamp getCurrentTimeStamp() {
	//	java.util.Date today = new java.util.Date();
	//	return new java.sql.Timestamp(today.getTime());
	//}	
	public Cookie getCookieByName(String name) { 
		// Fetch all cookies from the request 
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

		// Iterate to find cookie by its name 
		for (Cookie cookie : cookies) { 
			if (name.equals(cookie.getName())) { 
				return cookie; 
			} 
		}

		return null; 
	} 
	
	public void populateDailyMeals(JDBCConnectionPool connectionPool, 
			BeanItemContainer<DailyMealSelection> dailymeals){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM DailyMealSelection");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				dailymeals.addItem(new DailyMealSelection(result.getInt(1), result.getDate(2), result.getInt(3),
						result.getInt(4)));
			}
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateUsers(JDBCConnectionPool connectionPool, 
			BeanItemContainer<User> users){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				users.addItem(new User(result.getInt(1), result.getString(2), result.getInt(3),
						result.getString(4), result.getString(5), result.getString(6),
						result.getInt(7)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateGuests(JDBCConnectionPool connectionPool, 
			BeanItemContainer<Guest> guests){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Guest");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				guests.addItem(new Guest(result.getInt(1), result.getString(2), 
						result.getString(3), result.getInt(4)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateMealSelections(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealSelection> mealselections){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM MealSelection");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealselections.addItem(new MealSelection(result.getInt(1), result.getInt(2), 
						result.getInt(3), result.getInt(4), result.getInt(5)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void populateMeals(JDBCConnectionPool connectionPool, 
			BeanItemContainer<Meal> meals){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				meals.addItem(new Meal(result.getInt(1), result.getInt(2), 
						result.getString(3), result.getInt(4)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void populateMealOptions(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealOption> mealoptions){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM MealOption");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealoptions.addItem(new MealOption(result.getInt(1), result.getInt(2), 
						result.getString(3), result.getString(4), result.getInt(5)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isValidMealOption(final int pkmealoption, final int pkmeal, 
			final BeanItemContainer<MealOption> mealoptions){
		boolean check = false;
		MealOption mealoption;
		for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
			mealoption = (MealOption) i.next();
			if (pkmealoption == mealoption.getPk() && pkmeal == mealoption.getOwnedBy()){
				check = true;
				break;
			}
		}
		return check;
	}
	
}