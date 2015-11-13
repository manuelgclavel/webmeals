package com.example.mobile.viewer;

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
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class LoginView extends NavigationView {
	
	private static final String LOGIN_COOKIE = "loginmeals";
	private static final String PASSWORD_COOKIE = "passwordmeals";
	private static final Integer COOKIE_MAX_AGE = 31536000; /** 1 year */
	
	private final MobileUI currentUI = ((MobileUI) UI.getCurrent());


	

	
	@SuppressWarnings("serial")
	public LoginView(){
		

		VerticalComponentGroup content = new VerticalComponentGroup();
		content.setWidth("100%");
		
		VerticalComponentGroup vertical =
    			new VerticalComponentGroup();
    	
    	final TextField loginField = new TextField("Login: ");
		final PasswordField passwordField = new PasswordField("Password: ");
		//final NavigationButton enterButton = new NavigationButton("Enter");
		
		Button enterButton = new Button("Enter");
		
		//loginField.setImmediate(true);
		//passwordField.setImmediate(true);
	
		//final Button enterButton = new Button("Enter");
		
		
		//HorizontalButtonGroup buttons = new HorizontalButtonGroup();
		//buttons.addComponent(enterButton);
		
		//vertical.addComponent(buttons);
    	vertical.addComponent(loginField);
    	vertical.addComponent(passwordField);
    	HorizontalButtonGroup buttons = new HorizontalButtonGroup();
    	buttons.addComponent(enterButton);
    	vertical.addComponent(buttons);
    	
    	content.addComponent(vertical);
    	
    	
    	//buttons.addComponent(new Button("Enter"));
    	//buttons.addComponent(new Button("Cancel"));
    	//vertical.addComponent(buttons);
    	setContent(content);
    	
    	
    	// Read previously stored cookie value 
    	Cookie loginCookie = getCookieByName(LOGIN_COOKIE); 
    	Cookie passwordCookie = getCookieByName(PASSWORD_COOKIE);
    	if (getCookieByName(LOGIN_COOKIE) != null){
    		loginField.setValue(loginCookie.getValue());
    	}
    	if (getCookieByName(PASSWORD_COOKIE) != null) {
    		passwordField.setValue(passwordCookie.getValue());
    	}
    	
    				
    	/** */	
    	
    	
		enterButton.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				final String login = loginField.getValue();
				final String password = passwordField.getValue();
				final MobileUI currentUI = (MobileUI) UI.getCurrent();
				
				/** Get authenticated */		
				authenticate(login, password);
				/** */
				if (currentUI.getUser() == null){
					Notification.show("Unauthorized login/password.", ERROR_MESSAGE);
				} else {
					Cookie loginCookie = getCookieByName(LOGIN_COOKIE);			
					if (loginCookie != null) { 
						loginCookie.setValue(login);
					} else { 
						loginCookie = new Cookie(LOGIN_COOKIE, login);  
					}
					loginCookie.setMaxAge(COOKIE_MAX_AGE );
					loginCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
					VaadinService.getCurrentResponse().addCookie(loginCookie);
					
					Cookie passwordCookie = getCookieByName(PASSWORD_COOKIE);
					if (passwordCookie != null) { 
						passwordCookie.setValue(password);
						} else { 
						passwordCookie = new Cookie(PASSWORD_COOKIE, login); 
					} 
					passwordCookie.setMaxAge(COOKIE_MAX_AGE );
					passwordCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
					VaadinService.getCurrentResponse().addCookie(passwordCookie); 
					
					/** */
				
					if (currentUI.getRole().getId() == 0){
						//currentUI.setContent(new MealSelectionView()); 
						getNavigationManager().navigateTo(new MealSelectionView());
					} else { 
						if (currentUI.getRole().getId() == 1){							
							//currentUI.setContent(new MenuAdmin());
							getNavigationManager().navigateTo(new AdminMenuView());
						} else {
							Notification.show("Unauthorized role.", ERROR_MESSAGE);
						}
					}
					
				}
				
			}

			//@Override
			//public void buttonClick(NavigationButtonClickEvent event) {
				// TODO Auto-generated method stub
				
			//}
		});

    }
	
	private Cookie getCookieByName(String name) { 
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
	
	public void authenticate(String login, String password){

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

