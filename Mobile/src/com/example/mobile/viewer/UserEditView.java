package com.example.mobile.viewer;

import com.example.mobile.MobileUI;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class UserEditView extends NavigationView {
	final private MobileUI  ui = (MobileUI) UI.getCurrent();
	final private JDBCConnectionPool connectionPool = ui.getConnectionPool();


	public UserEditView(){
		VerticalComponentGroup content = new VerticalComponentGroup();

		BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);
		ui.populateUsers(connectionPool, users);

		Table usersTable = new Table("",users);
		usersTable.setVisibleColumns("surname", "name", "login", "role");
		usersTable.sort(new Object[] {"surname", "name"}, new boolean[] {true, true});
		usersTable.setPageLength(usersTable.size());
		
		content.addComponent(usersTable);
		setContent(content);
	}
}