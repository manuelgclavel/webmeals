package com.example.mobile.presenter;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.Cookie;

import com.example.mobile.MobileUI;
import static com.example.mobile.MobileUI.LOGIN_COOKIE;
import static com.example.mobile.MobileUI.PASSWORD_COOKIE;
import static com.example.mobile.MobileUI.COOKIE_MAX_AGE;

import com.example.mobile.data.Residence;
import com.example.mobile.data.Role;
import com.example.mobile.data.User;
import com.example.mobile.viewer.AdminMenuView;
import com.example.mobile.viewer.MealSelectionSwipeView;
import com.example.mobile.viewer.SelectionDateView;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class LoginBehavior implements NavigationButtonClickListener {
	private String login;
	private String password;
	private MobileUI currentUI = (MobileUI) UI.getCurrent();
	

	public LoginBehavior(TextField login, PasswordField password){
		this.login = login.getValue();
		this.password = password.getValue();
		
	}
	@Override
	public void buttonClick(NavigationButtonClickEvent event) {}
	
	

}