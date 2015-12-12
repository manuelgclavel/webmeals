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
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
//public class LoginView extends NavigationManager {
	//public class LoginView extends VerticalComponentGroup {
	public class LoginView extends NavigationView {
	
	private final MobileUI currentUI = ((MobileUI) UI.getCurrent());
	private TextField testA;
	private PasswordField testB;
	
	public LoginView(){
		
		setCaption("Ravenahl's Meal Count");
		
		VerticalComponentGroup content = new VerticalComponentGroup();
		setWidth("100%");
		
		//NavigationBar top = new NavigationBar();
		Button enterButton = new Button("Enter");
		
    	testA = new TextField("Login: ");
    	testB = new PasswordField("Password: ");
		
		// Read previously stored cookie value 
    	Cookie loginCookie = currentUI.getCookieByName(LOGIN_COOKIE); 
    	Cookie passwordCookie = currentUI.getCookieByName(PASSWORD_COOKIE);
    	if (currentUI.getCookieByName(LOGIN_COOKIE) != null){
    		testA.setValue(loginCookie.getValue());
    	}
    	if (currentUI.getCookieByName(PASSWORD_COOKIE) != null) {
    		testB.setValue(passwordCookie.getValue());
    	}
    	
    	
		content.addComponent(testA);
    	content.addComponent(testB);
    	content.addComponent(enterButton);
    	HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(enterButton);
    	content.addComponent(buttons);
    	setContent(content);
    					
    	enterButton.addClickListener(new LoginBehavior());
    	
    	
	}
	
	
	
	private class LoginBehavior implements ClickListener {
		
		private LoginBehavior(){
		
		}
	

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
			String login = testA.getValue();
			String password = testB.getValue();
			//testB.setValue("adios");;
			authenticate(login, password);
			/** */
			if (currentUI.getUser() == null){
				Notification.show("Unauthorized login/password.", ERROR_MESSAGE);
			} else {
				Cookie loginCookie = currentUI.getCookieByName(LOGIN_COOKIE);			
				if (loginCookie != null) { 
					loginCookie.setValue(login);
				} else { 
					loginCookie = new Cookie(LOGIN_COOKIE, login);  
				}
				loginCookie.setMaxAge(COOKIE_MAX_AGE );
				loginCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
				VaadinService.getCurrentResponse().addCookie(loginCookie);
				
				Cookie passwordCookie = currentUI.getCookieByName(PASSWORD_COOKIE);
				if (passwordCookie != null) { 
					passwordCookie.setValue(password);
					} else { 
					passwordCookie = new Cookie(PASSWORD_COOKIE, password); 
				} 
				passwordCookie.setMaxAge(COOKIE_MAX_AGE );
				passwordCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
				VaadinService.getCurrentResponse().addCookie(passwordCookie); 
				
				/**
				 * 0 - user
				 * 1 - director
				 * 2 - administration
				 * 3 = system
				 * */
			
				if (currentUI.getRole().getId() == 0){
					getNavigationManager().navigateTo(new UserMenuView());
				} else { 
					if (currentUI.getRole().getId() == 1){			
						getNavigationManager().navigateTo(new AdminMenuView());
					} 
					else {
						if (currentUI.getRole().getId() == 3){
							getNavigationManager().navigateTo(new SystemMenuView());
						}
						else {
							Notification.show("Unauthorized role.", ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}
		
	
private void authenticate(String login, String password){
		
		//Notification.show(login + ":" + password);

		try {
			Connection conn = currentUI.getConnectionPool().reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("select count(*), pk, name, role, surname, residence from User where login = ? and password = ?");
			ps.setString(1, login);
			ps.setString(2, password);
			ResultSet result = ps.executeQuery();
			result.next();
			if (result.getInt(1) == 1) {
				User user = new User(result.getInt(2), result.getString(3), result.getInt(4),
						result.getString(5), login, password, result.getInt(6));
				currentUI.setUser(user);
				result.close();
				result.close();
				
				/** Recall that role cannot be NULL */
				Role role = new Role(user.getRole());
				currentUI.setRole(role);
				
				if (!(user.getResidence() == 0)){
					Residence residence = new Residence(user.getResidence());
					currentUI.setResidence(residence);
					ps = conn.prepareStatement("select count(*), name, lang, zone from Residence where pk = ?" );
					ps.setInt(1, residence.getPk());
					result = ps.executeQuery();
					result.next();
					if (result.getInt(1) == 1) {
						residence.setName(result.getString(2));
						residence.setLang(result.getString(3));
						residence.setZone(result.getString(4));
						result.close();
						}
				}
			}
			result.close();
			ps.close();
			currentUI.getConnectionPool().releaseConnection(conn);

		} catch (SQLException e) {
			throw new RuntimeException (e);
		}

	}
	
}

