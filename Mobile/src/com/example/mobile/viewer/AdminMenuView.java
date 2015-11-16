package com.example.mobile.viewer;

import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;

@SuppressWarnings("serial")
public class AdminMenuView extends NavigationView {
	
	public AdminMenuView(){
	
	CssLayout content = new CssLayout();
	content.setWidth("100%");
		
	VerticalComponentGroup componentGroup = new VerticalComponentGroup();
	componentGroup.setCaption("Menu");

	NavigationButton mealcount = new NavigationButton("Meal count");
	mealcount.addClickListener(new NavigationButtonClickListener(){

		@Override
		public void buttonClick(NavigationButtonClickEvent event) {
			// TODO Auto-generated method stub
			//getNavigationManager().navigateTo(new MealCountView());
			setContent(new MealCountView());
		}});
	
	componentGroup.addComponent(mealcount);
	
	/** */
	Button logout = new Button("Logout");
	logout.addClickListener(new ExitBehavior());
	componentGroup.addComponent(logout);
	
	/** */
	
	content.addComponent(componentGroup);
	setContent(content);

	//componentGroup.addComponent(new TextField("Username"));

	//NumberField age = new NumberField("Age");
	//age.setWidth("100%");
	//componentGroup.addComponent(age);

	//Switch switch1 = new Switch("Use my location");
	//switch1.setValue(true);
	//componentGroup.addComponent(switch1);

	//switch1 = new Switch("Alerts");
	//componentGroup.addComponent(switch1);

}
}
