package com.example.mobile.viewer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
	
	int weekselected = 0;
	Date dayselected;
	String timezone;

	public SelectionWeekView(){
		setCaption("Meal selection (by week)");
		// Set up the initial views

		 timezone = ((MobileUI) UI.getCurrent()).getResidence().getZone();
		 Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		 c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		 this.dayselected = c.getTime();
		
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
	      
		final Button prev = new Button("prev");
		final Button next = new Button("next");
		final Button now = new Button("today's week");
		
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
		
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		c.setTime(dayselected);
		c.add(Calendar.DATE, currentpos * 7);
		
		
		for (int i=1; i<=7; i++){ 
			String thisdate = new SimpleDateFormat("EEE, MMM d, ''yy").format(c.getTime());
			layout.addComponent(new Label("<b>" + thisdate + "</b>",ContentMode.HTML));
			layout.addComponent(new SelectionDateView(c.getTime(),
					((MobileUI) UI.getCurrent()).getUser().getPk(),
					1));
			c.add(Calendar.DATE, +1);
		}
	
		addComponent(layout);

		
		next.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				navigateTo(getNextComponent());
			}
			
		});
		
		prev.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				navigateTo(getPreviousComponent());
			}
			
		});
		
		now.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				weekselected = 0;
				//timezone = ((MobileUI) UI.getCurrent()).getResidence().getZone();
				Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timezone));
				c.setTime(dayselected);
				
				setCurrentComponent(createWeekView(weekselected));
				setNextComponent(createWeekView(weekselected+1));
			    setPreviousComponent(createWeekView(weekselected-1));
			}
			
		});
	
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
		         }
		             break;
		         case BACK: {  
		        	 weekselected = weekselected-1;
		        	 setPreviousComponent(createWeekView(weekselected-1));
		       	 
		         }}}
}
	
