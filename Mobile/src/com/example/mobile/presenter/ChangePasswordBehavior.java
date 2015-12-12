package com.example.mobile.presenter;

import com.example.mobile.MobileUI;
import com.example.mobile.data.User;
import com.example.mobile.viewer.CloseSessionView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ChangePasswordBehavior implements ClickListener {
	MobileUI ui = (MobileUI) UI.getCurrent();
	JDBCConnectionPool connectionPool = ui.getConnectionPool();
	User user = ui.getUser();
	
	TextField newpassword;
	TextField confirmnewpassword;
	
	public ChangePasswordBehavior(TextField newpassword, TextField confirmnewpassword){
		this.newpassword  = newpassword;
		this.confirmnewpassword = confirmnewpassword;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (newpassword.getValue().equals(confirmnewpassword.getValue())){
			ui.updateUserPassword(connectionPool, user, newpassword.getValue());
			//Notification.show("Done");
			ui.setContent(new CloseSessionView("Please, log-in again with your new password"));
			UI.getCurrent().close();
		
		} else {
			Notification.show("Please, re-type the new password");
		}
	}

}
