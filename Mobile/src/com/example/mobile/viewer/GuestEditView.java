package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Guest;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
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
		setContent(content);


	}
}
