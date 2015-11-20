package com.example.mobile.viewer;

import static com.example.mobile.MobileUI.COOKIE_MAX_AGE;
import static com.example.mobile.MobileUI.LOGIN_COOKIE;
import static com.example.mobile.MobileUI.PASSWORD_COOKIE;
import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Cookie;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Residence;
import com.example.mobile.data.Role;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
//public class LoginView extends NavigationManager {
	//public class LoginView extends VerticalComponentGroup {
	public class LoginView extends NavigationView {
	
	private final MobileUI currentUI = ((MobileUI) UI.getCurrent());
	private TextField loginField;
	private PasswordField passwordField;
	
	public LoginView(){
		
		//addNavigationListener((NavigationListener) this);
		
		VerticalComponentGroup content = new VerticalComponentGroup();
		setWidth("100%");
		
		//NavigationBar top = new NavigationBar();
		Button enterButton = new Button("Enter");
		Label nihil = new Label("");
		setLeftComponent(nihil);
		setRightComponent(enterButton);
		//content.addComponent(top);
		
		//VerticalComponentGroup vertical =
    	//		new VerticalComponentGroup();
    	
    	loginField = new TextField("Login: ");
		passwordField = new PasswordField("Password: ");
		// Read previously stored cookie value 
    	Cookie loginCookie = currentUI.getCookieByName(LOGIN_COOKIE); 
    	Cookie passwordCookie = currentUI.getCookieByName(PASSWORD_COOKIE);
    	if (currentUI.getCookieByName(LOGIN_COOKIE) != null){
    		loginField.setValue(loginCookie.getValue());
    	}
    	if (currentUI.getCookieByName(PASSWORD_COOKIE) != null) {
    		passwordField.setValue(passwordCookie.getValue());
    	}
    	
    	
		content.addComponent(loginField);
    	content.addComponent(passwordField);
    	//HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	//buttons.addComponent(enterButton);
    	//content.addComponent(buttons);
    	
    	//content.addComponent(vertical);
    	//addComponent(content);
    	//addComponent(content);
    	setContent(content);
    				
    	/** */	
    	
    	enterButton.addClickListener(new ClickListener(){
    		

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub		
				authenticate(loginField.getValue(), passwordField.getValue());
				/** */
				if (currentUI.getUser() == null){
					Notification.show("Unauthorized login/password.", ERROR_MESSAGE);
				} else {
					Cookie loginCookie = currentUI.getCookieByName(LOGIN_COOKIE);			
					if (loginCookie != null) { 
						loginCookie.setValue(loginField.getValue());
					} else { 
						loginCookie = new Cookie(LOGIN_COOKIE, loginField.getValue());  
					}
					loginCookie.setMaxAge(COOKIE_MAX_AGE );
					loginCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
					VaadinService.getCurrentResponse().addCookie(loginCookie);
					
					Cookie passwordCookie = currentUI.getCookieByName(PASSWORD_COOKIE);
					if (passwordCookie != null) { 
						passwordCookie.setValue(passwordField.getValue());
						} else { 
						passwordCookie = new Cookie(PASSWORD_COOKIE, passwordField.getValue()); 
					} 
					passwordCookie.setMaxAge(COOKIE_MAX_AGE );
					passwordCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
					VaadinService.getCurrentResponse().addCookie(passwordCookie); 
					
					/** */
				
					if (currentUI.getRole().getId() == 0){
						//currentUI.setContent(new MealSelectionSwipeView(Calendar.getInstance().getTime()));
						getNavigationManager().navigateTo(new UserMenuView());
						//currentUI.setContent(new UserMenuView());
					} else { 
						if (currentUI.getRole().getId() == 1){			
							//currentUI.getManager().navigateTo(new AdminMenuView());;
							//currentUI.setContent(new AdminMenuView());
							getNavigationManager().navigateTo(new AdminMenuView());
						} else {
							Notification.show("Unauthorized role.", ERROR_MESSAGE);
						}
					}
				}
				
			}});
    	
	}
	
	
		
	
private void authenticate(String login, String password){
		
		//Notification.show(login + ":" + password);

		try {
			Connection conn = currentUI.getConnectionPool().reserveConnection();
			PreparedStatement ps = conn.prepareStatement("select count(*),pk,role from User where login = ? and password = ?");
			ps.setString(1, login);
			ps.setString(2, password);
			ResultSet result = ps.executeQuery();
			result.next();
			if (result.getInt(1) == 1) {
				User user = new User(result.getInt(2));
				currentUI.setUser(user);
				result.close();
				ps = conn.prepareStatement("select name, role, surname, residence from User where pk = ?" );
				ps.setInt(1, user.getPk());
				result = ps.executeQuery();
				result.next();
				user.setName(result.getString(1));
				user.setRole(result.getInt(2));
				user.setSurname(result.getString(3));
				user.setResidence(result.getInt(4));
				result.close();
				
				Role role = new Role(user.getRole());
				currentUI.setRole(role);
				
				Residence residence = new Residence(user.getResidence());
				currentUI.setResidence(residence);
				ps = conn.prepareStatement("select name, lang, zone from Residence where pk = ?" );
				ps.setInt(1, residence.getPk());
				result = ps.executeQuery();
				result.next();
				residence.setName(result.getString(1));
				residence.setLang(result.getString(2));
				residence.setZone(result.getString(3));
				result.close();				
			}
			result.close();
			ps.close();
			currentUI.getConnectionPool().releaseConnection(conn);

		} catch (SQLException e) {
			throw new RuntimeException (e);
		}

	}
	
}

