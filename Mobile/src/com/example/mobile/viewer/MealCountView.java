package com.example.mobile.viewer;


import java.util.Calendar;
import java.util.Date;


import com.example.mobile.viewer.CountView;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MealCountView extends NavigationView {
	private VerticalComponentGroup panel = new VerticalComponentGroup();

	public MealCountView(){
		

		CssLayout content = new CssLayout();
		content.setWidth("100%");
			
		final VerticalComponentGroup componentGroup = new VerticalComponentGroup();
		//componentGroup.setCaption("Menu");

		DateField date = new DateField();
		date.setValue(new Date());
		componentGroup.addComponent(date);
		panel.setImmediate(true);
		//java.util.Date javaDate = Calendar.getInstance().getTime();
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime()); 
		panel = new CountView(sqlDate);	
		componentGroup.addComponent(panel);
		content.addComponent(componentGroup);

		setContent(content);
			
			date.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					java.util.Date javaDate = Calendar.getInstance().getTime();
					javaDate = (Date) event.getProperty().getValue();
					java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime()); 
					//mealCountTable = new CountView(sqlDate);
					componentGroup.removeComponent(panel);
					panel = new CountView(sqlDate);	
					componentGroup.addComponent(panel);	
				}
			});
	}
}
	
					
		