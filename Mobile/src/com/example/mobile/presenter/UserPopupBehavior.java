package com.example.mobile.presenter;

import com.example.mobile.MobileUI;
import com.example.mobile.data.MealSelectionPlus;
import com.example.mobile.viewer.UserPopupView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class UserPopupBehavior implements ClickListener {
	private BeanItemContainer<MealSelectionPlus> mealselections;
	
	public UserPopupBehavior(BeanItemContainer<MealSelectionPlus> mealselections)  {
		this.mealselections = mealselections;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		((MobileUI) UI.getCurrent()).getManager().navigateTo(new UserPopupView(mealselections));
		
	}

}
