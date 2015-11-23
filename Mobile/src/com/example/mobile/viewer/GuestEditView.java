package com.example.mobile.viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	private JDBCConnectionPool connectionPool;

	public GuestEditView() {
		
		VerticalComponentGroup content = new VerticalComponentGroup();	
		connectionPool = ((MobileUI) UI.getCurrent()).getConnectionPool();
		
			Connection conn;
			try {
				conn = connectionPool.reserveConnection();
				BeanItemContainer<Guest> guests = new BeanItemContainer<Guest>(Guest.class);
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM Guest");
				ResultSet result = ps.executeQuery();
				while (result.next()){
					guests.addItem(new Guest(result.getInt(1), result.getString(2), result.getString(3),
							result.getInt(4)));
				}
				
				result.close();
				ps.close();
				connectionPool.releaseConnection(conn);
				
				Table guestsTable = new Table("",guests);
				content.addComponent(guestsTable);
				setContent(content);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
}
