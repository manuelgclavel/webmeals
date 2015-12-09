package com.example.mobile.viewer;

import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Guest;
import com.example.mobile.data.MealSelectionPlus;
import com.example.mobile.data.User;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserPopupView extends NavigationView {
	
	private MobileUI ui = (MobileUI) UI.getCurrent();
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	private BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);
	private BeanItemContainer<Guest> guests = new BeanItemContainer<Guest>(Guest.class);

	public UserPopupView(BeanItemContainer<MealSelectionPlus> mealselections){
		
		ui.populateUsers(connectionPool, users);
		ui.populateGuests(connectionPool, guests);
		
		Table userstable = new Table("", collectDiners(mealselections));
		setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		userstable.setVisibleColumns(new Object[] {"surname", "name"});
		userstable.setSortAscending(true);
		userstable.sort(new Object[] {"surname", "name"}, new boolean[] {true, true});
		userstable.setPageLength(userstable.size());
		layout.addComponent(userstable);
		setContent(layout);
	}

	private BeanItemContainer<User> collectDiners(BeanItemContainer<MealSelectionPlus> selectedmealselections) {
		// TODO Auto-generated method stub
		BeanItemContainer<User> diners = new BeanItemContainer<User>(User.class);
		MealSelectionPlus mealselection;
		User user;
		Guest guest;
		
		for (Iterator<MealSelectionPlus> h = selectedmealselections.getItemIds().iterator(); h.hasNext();) {
			mealselection = h.next();
			if (!(mealselection.getSelectedBy() == 0)){ 
				for (Iterator<User> u = users.getItemIds().iterator(); u.hasNext();){
					user = u.next();
					if (mealselection.getSelectedBy() == user.getPk()){
						diners.addItem(new User(user.getName(), user.getSurname()));
						break;
					}
				}
			}
			if (!(mealselection.getOfferedTo() == 0)){ 
				for (Iterator<Guest> g = guests.getItemIds().iterator(); g.hasNext();){
					guest = g.next();
					if (mealselection.getOfferedTo() == guest.getPk()){
						diners.addItem(new User(guest.getName(), guest.getSurname()));
						break;
					}
				}
			}
		}
		return diners;
	}

}
