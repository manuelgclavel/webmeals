package com.example.mobile.viewer;

import java.util.Calendar;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Guest;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class GuestEditView extends NavigationView {

	final private MobileUI  ui = (MobileUI) UI.getCurrent();
	final private JDBCConnectionPool connectionPool = ui.getConnectionPool();

	public GuestEditView() {

		VerticalComponentGroup content = new VerticalComponentGroup();	

		BeanItemContainer<Guest> guests = new BeanItemContainer<Guest>(Guest.class);
		ui.populateGuests(connectionPool, guests);

		Table guestsTable = new Table("",guests);
		content.addComponent(guestsTable);
		
		
		
		
		/** invite-a-guest */
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
