package com.example.mobile;


import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
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

	
    @Override
    protected void init(VaadinRequest request)  {
    	
    	try {
			connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/ravenahl","root","admin");	
			NavigationManager manager =
					new NavigationManager(new LoginView());
					setContent(manager);
					
			
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
}