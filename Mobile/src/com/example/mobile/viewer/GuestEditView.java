package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Guest;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class GuestEditView extends NavigationView {

	final private MobileUI  ui = (MobileUI) UI.getCurrent();
	final private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	public GuestEditView() {
		
		setCaption("Guest list");

		Button logout = new Button();
		logout.setCaption("Exit");
		setRightComponent(logout);
		logout.addClickListener(new ExitBehavior());
		
		Button back = new Button();
		back.setCaption("Admin menu");
		setLeftComponent(back);
		back.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new AdminMenuView());
			} });
		
		
		VerticalComponentGroup content = new VerticalComponentGroup();	

		BeanItemContainer<Guest> guests = new BeanItemContainer<Guest>(Guest.class);
		ui.populateGuests(connectionPool, guests);

		Table guestsTable = new Table("",guests);
		content.addComponent(guestsTable);		
		
		/** edit-a-guest */
		guestsTable.addItemClickListener(new ItemClickListener(){

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				getNavigationManager().navigateTo(
						new GuestMenuView ((int) event.getItem().getItemProperty("pk").getValue())); 
			}});	
		
		
		
		
		setContent(content);


	}
}
