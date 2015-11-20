package com.example.mobile.viewer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SelectionWeekView extends NavigationManager implements NavigationListener {
	
	
	int pos = 0;
	Calendar c = new GregorianCalendar();
	int weekselected= c.get(Calendar.WEEK_OF_YEAR);
	Date dayselected;

	public SelectionWeekView(Date selected){
		setCaption("Meal selection (by week)");
		// Set up the initial views
		this.dayselected = selected;
		//navigateTo(createWeekView(+pos));
		//
		navigateTo(createWeekView(weekselected));
		setNextComponent(createWeekView(weekselected+1));
		setPreviousComponent(createWeekView(weekselected-1));
		addNavigationListener((NavigationListener) this);
		
	}
	
	SwipeView createWeekView(int currentpos) {
		SwipeView view = new SwipeView();
		VerticalComponentGroup layout = new VerticalComponentGroup();
		
		 NavigationBar top = new NavigationBar();

	      Button menu = new Button("Menu");
	      top.setLeftComponent(menu);
	      Button logout = new Button("Exit");
	      top.setRightComponent(logout);

	      menu.addClickListener(new ClickListener(){

	 		@Override
	 		public void buttonClick(ClickEvent event) {
	 			// TODO Auto-generated method stub
	 			((MobileUI) UI.getCurrent()).getManager().navigateTo(new UserMenuView());
	 			
	 		}});
	      
	      logout.addClickListener(new ExitBehavior());
	     
	      layout.addComponent(top);
	      

		/**  */
		final Button prev = new Button("prev");
		final Button next = new Button("next");
		final Button now = new Button("today's week");
		final Button today = new Button("today");
		
		Label weekshown =  new Label("Week: " + Integer.valueOf(currentpos).toString());
		
		layout.addComponent(weekshown);
		
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
		
		buttons.addComponent(prev);
		buttons.addComponent(next);
		buttons.addComponent(now);
		//buttons.addComponent(today);		
		layout.addComponent(buttons);
		
		
		/** GREAT FOR DEBUGGING */
		//layout.addComponent(new Label(c.getTime().toString()));
		//layout.addComponent(new Label(Integer.valueOf(currentpos).toString()));
		/** END */
		
		Calendar c = new GregorianCalendar();
		c.set(Calendar.WEEK_OF_YEAR, currentpos);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		
		for (int i=1; i<=7; i++){ 
			String thisdate = new SimpleDateFormat("EEE, MMM d, ''yy").format(c.getTime());
			layout.addComponent(new Label("<b>" + thisdate + "</b>",ContentMode.HTML));
			layout.addComponent(new SelectionDateView(c.getTime()));
			c.add(Calendar.DATE, +1);
		}
	
		addComponent(layout);

		
		next.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				navigateTo(getNextComponent());
				//weekselected = weekselected+1;
				//navigateTo(createWeekView(weekselected));
				//setNextComponent(createWeekView(weekselected+1));
			    //setPreviousComponent(createWeekView(weekselected-1));
			}
			
		});
		
		prev.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				navigateTo(getPreviousComponent());
				//weekselected = weekselected-1;
				//navigateTo(createWeekView(weekselected));
				//setNextComponent(createWeekView(weekselected+1));
			    //setPreviousComponent(createWeekView(weekselected-1));
			}
			
		});
		
		now.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				pos = 0;
				Calendar c = new GregorianCalendar();
				c.setTime(dayselected);
				//c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
				//dayselected = c.getTime();
				weekselected= c.get(Calendar.WEEK_OF_YEAR);
				
				setCurrentComponent(createWeekView(+weekselected));
				setNextComponent(createWeekView(weekselected+1));
			    setPreviousComponent(createWeekView(weekselected-1));
			}
			
		});
		
		//today.addClickListener(new ClickListener(){

		//	@Override
		//	public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show("HERE");
				//navigateBack();
				//navigateTo(new UserMenuView());
				//setNextComponent(new MealSelectionSwipeView(Calendar.getInstance().getTime()));
				//navigateTo(new MealSelectionSwipeView(Calendar.getInstance().getTime()));
		//	}
			
		//});
		view.setContent(layout);
		return view;
	}


		/** */

	@Override
		public void navigate(NavigationEvent event) {
			// TODO Auto-generated method stub
				 switch (event.getDirection()) {
		         case FORWARD: {
		        	 weekselected = weekselected+1;
	                 setNextComponent(createWeekView(weekselected+1));
		        	 //navigateTo(createWeekView(weekselected));
		        	 //setNextComponent(createWeekView(weekselected+1));
		        	 //setPreviousComponent(createWeekView(weekselected-1));
		     
		        	 		        	 
		             
		         }
		             break;
		         case BACK: {  
		        	 weekselected = weekselected-1;
		        	 setPreviousComponent(createWeekView(weekselected-1));
		        	 //navigateTo(createWeekView(weekselected));
		        	 //setNextComponent(createWeekView(weekselected+1));
		        	 //setPreviousComponent(createWeekView(weekselected-1));
		     
		        	
		       	 
		         }}}
}
	
