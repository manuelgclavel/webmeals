package com.example.mobile.viewer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class CloseSessionView extends CssLayout {
	
	public CloseSessionView(){
		this.setSizeFull();
		Notification.show("Bye!");
	}
	
	public CloseSessionView(String message){
		this.setSizeFull();
		Notification.show(message);
	}
	
}
