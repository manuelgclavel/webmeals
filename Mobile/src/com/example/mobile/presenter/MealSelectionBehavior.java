package com.example.mobile.presenter;

import com.example.mobile.viewer.MealSelectionView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class MealSelectionBehavior implements ClickListener {
	private VerticalLayout panel;

	public MealSelectionBehavior(VerticalLayout panel){
		this.panel = panel;

	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub	
		panel.removeAllComponents();
		panel.addComponent(new MealSelectionView());	
	}
}
