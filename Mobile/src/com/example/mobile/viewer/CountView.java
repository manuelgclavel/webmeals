package com.example.mobile.viewer;

import java.util.Date;
import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealSelectionPlus;
import com.example.mobile.presenter.UserPopupBehavior;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class CountView extends VerticalComponentGroup {
	private MobileUI ui = (MobileUI) UI.getCurrent();
	private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	private BeanItemContainer<MealSelectionPlus> mealselections = new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
	private BeanItemContainer<Meal> meals = new BeanItemContainer<Meal>(Meal.class);
	private BeanItemContainer<MealOption> mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
	private BeanItemContainer<FoodRegime> regimenes = new BeanItemContainer<FoodRegime>(FoodRegime.class);
	
	
	public CountView (Date selected, boolean showusers){
		ui.populateMeals(connectionPool, meals);
		ui.populateMealOptions(connectionPool, mealoptions);
		ui.populateFoodRegimes(connectionPool, regimenes);
		ui.populateMealSelectionsPlus(connectionPool, mealselections, selected);

			for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();) {
				Meal meal = j.next();
				Label mealName = new Label("<b>" + meal.getLiteral() + "</b>", ContentMode.HTML);
				addComponent(mealName);
				BeanItemContainer<MealOption> selectedmealoptions = selectByMeal(meal);
				
				for (Iterator<MealOption> i = selectedmealoptions.getItemIds().iterator(); i.hasNext();) {
					MealOption mealoption = i.next();
					BeanItemContainer<MealSelectionPlus> selectedmealselections = selectByMealOption(mealoption);
					int optionCount = selectedmealselections.size();
					
					if (!(optionCount == 0)){			
						HorizontalLayout rowMealCount = new HorizontalLayout();
						rowMealCount.setSpacing(true);
						Button popup = new Button (mealoption.getInitial() + " : " + Integer.valueOf(optionCount).toString());	
						if (showusers){
							popup.addClickListener(new UserPopupBehavior(selectedmealselections));
						}
						rowMealCount.addComponent(popup);
						
						HorizontalLayout regimenesPanel = new HorizontalLayout();
						regimenesPanel.setSpacing(true);
						FoodRegime regime;
						for (Iterator<FoodRegime> k = regimenes.getItemIds().iterator(); k.hasNext();) {
							regime = k.next();
							selectedmealselections = selectByMealOptionAndRegime(mealoption,regime);
							int regimeCount = selectedmealselections.size();
							if (!(regimeCount == 0)){

								HorizontalLayout rowRegimeCount = new HorizontalLayout();
								rowRegimeCount.setSpacing(true);
								Button regimepopup = new Button ("#" + regime.getName() + " (" + Integer.valueOf(regimeCount).toString() + ")");
								if (showusers){
									regimepopup.addClickListener(new UserPopupBehavior(selectedmealselections));	
								}
								regimenesPanel.addComponent(regimepopup);	
							}
						}

						rowMealCount.addComponent(regimenesPanel);
						addComponent(rowMealCount);
					}
				}
			}
	}
		private BeanItemContainer<MealSelectionPlus> selectByMealOption(MealOption mealoption) {
		// TODO Auto-generated method stub
		BeanItemContainer<MealSelectionPlus> selectedmealselections =
				new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
		MealSelectionPlus mealselection;
		for (Iterator<MealSelectionPlus> i = mealselections.getItemIds().iterator(); i.hasNext();){
			mealselection = (MealSelectionPlus) i.next();
			if (mealselection.getMealOption()== mealoption.getPk()) { 
				selectedmealselections.addItem(mealselection);
			}
		}
		return selectedmealselections;
	}
	
	private BeanItemContainer<MealSelectionPlus> selectByMealOptionAndRegime(MealOption mealoption, FoodRegime regime) {
		// TODO Auto-generated method stub
		BeanItemContainer<MealSelectionPlus> selectedmealselections =
				new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
		MealSelectionPlus mealselection;
		for (Iterator<MealSelectionPlus> i = mealselections.getItemIds().iterator(); i.hasNext();){
			mealselection = (MealSelectionPlus) i.next();
			if (mealselection.getMealOption() == mealoption.getPk() 
					&& mealselection.getFoodRegime() == regime.getPk()) {
				selectedmealselections.addItem(mealselection);
			}
		}
		return selectedmealselections;
	}
	
	
	private BeanItemContainer<MealOption> selectByMeal(Meal meal){
		BeanItemContainer<MealOption> selectedmealoptions = 
				new BeanItemContainer<MealOption>(MealOption.class);
		MealOption mealoption;
		for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
			mealoption = (MealOption) i.next();
			if (mealoption.getOwnedBy() == meal.getPk()) { 
				selectedmealoptions.addItem(mealoption);
				}
			}
		return selectedmealoptions;
	}
	
	
	
	
}