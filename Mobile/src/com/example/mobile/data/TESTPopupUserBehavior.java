package com.example.mobile.data;

import java.util.Iterator;

import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class TESTPopupUserBehavior implements ClickListener {
	private BeanItemContainer<MealCount> mealcounts;
	
	public TESTPopupUserBehavior(BeanItemContainer<MealCount> mealcounts){
		this.mealcounts = mealcounts; 
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		BeanItemContainer<User> users = new BeanItemContainer<User>(User.class);					
				for (Iterator<MealCount> i = mealcounts.getItemIds().iterator(); i.hasNext();) {
					MealCount mealcount = i.next();
					users.addItem(new User(mealcount.getUsersurname(),mealcount.getUsername()));
				}
				CssLayout layout = new CssLayout();
					
				//Table userstable = new Table("", users);
				//userstable.setPageLength(userstable.size());
				//userstable.setVisibleColumns(new Object[] {"name", "surname"});
				Popover popover = new Popover();
				layout.addComponent(new Label(Integer.valueOf(mealcounts.size()).toString()));
				//layout.addComponent(userstable);
				popover.setContent(layout);   
				popover.showRelativeTo(event.getButton());
	}
}
