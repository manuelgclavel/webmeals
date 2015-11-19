package com.example.mobile;


import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import com.example.mobile.MobileUI;
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
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

//package com.vaadin.touchkitsampler;


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
}