package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Meal;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class UserEditView extends NavigationView {
	private JDBCConnectionPool connectionPool;

	
	public UserEditView(){
		VerticalComponentGroup content = new VerticalComponentGroup();	
		connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();

		//setMargin(true);
		//setSpacing(true);

		
			Connection conn;
			try {
				conn = connectionPool.reserveConnection();
				BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM User");
				ResultSet result = ps.executeQuery();
				while (result.next()){
					users.addItem(new User(result.getInt(1), result.getString(2), result.getInt(3),
							result.getString(4), result.getString(5), result.getString(6),
							result.getInt(7)));
				}
				result.close();
				ps.close();
				connectionPool.releaseConnection(conn);
				
				Table usersTable = new Table("",users);
				content.addComponent(usersTable);
				setContent(content);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}