package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.mobile.MobileUI;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class SelectionWeekView extends NavigationManager implements NavigationListener {
	//final private MobileUI mobile;
	//final private JDBCConnectionPool connectionPool;

	int pos = 0;
	int weekselected;
	Date dayselected;

	public SelectionWeekView(java.util.Date selected){
		Calendar c = new GregorianCalendar();
		c.setTime(selected);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		this.dayselected = c.getTime();
		this.weekselected= c.get(Calendar.WEEK_OF_YEAR);
		// Set up the initial views
		navigateTo(createWeekView(+pos));
		setNextComponent(createWeekView(pos+1));
		setPreviousComponent(createWeekView(pos-1));
		addNavigationListener((NavigationListener) this);
	}
	
	SwipeView createWeekView(int currentpos) {
		SwipeView view = new SwipeView();
		setSizeFull();
		VerticalComponentGroup layout = new VerticalComponentGroup();

		/**  */
		final Button prev = new Button("prev");
		final Button now = new Button("today's");
		final Button next = new Button("next");
		final Label weekshown =  new Label("Week: " + Integer.valueOf(weekselected+currentpos).toString());

		layout.addComponent(weekshown);
		
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
		
		buttons.addComponent(prev);
		buttons.addComponent(next);
		buttons.addComponent(now);
		
		layout.addComponent(buttons);
		
		
		/** GREAT FOR DEBUGGING */
		//layout.addComponent(new Label(c.getTime().toString()));
		//layout.addComponent(new Label(Integer.valueOf(currentpos).toString()));
		/** END */
		
		Calendar c = new GregorianCalendar();
		c.set(Calendar.WEEK_OF_YEAR, weekselected+pos);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
	
		for (int i=1; i<= 7; i++){ 
			
			String thisdate = new SimpleDateFormat("EEE, MMM d, ''yy").format(c.getTime());
			layout.addComponent(new Label("<b>" + thisdate + "</b>",ContentMode.HTML));
			layout.addComponent(new SelectionDateView(c.getTime()));
			c.add(Calendar.DATE,1);
		}
		//addComponent(layout);

		
		next.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Calendar c = new GregorianCalendar();
				//c.setTime(dayselected);
				//c.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(weekselected));
				//c.add(Calendar.WEEK_OF_YEAR, +1);
				navigateTo(getNextComponent());
			}
			
		});
		
		prev.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Calendar c = new GregorianCalendar();
				//c.add(Calendar.WEEK_OF_YEAR, -1);
				//c.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(weekselected));
				navigateTo(getPreviousComponent());
			}
			
		});
		
		now.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				pos = 0;
				Calendar c = new GregorianCalendar();
				c.setTime(dayselected);
				c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
				dayselected = c.getTime();
				weekselected= c.get(Calendar.WEEK_OF_YEAR);
				
				setCurrentComponent(createWeekView(+pos));
				setNextComponent(createWeekView(pos+1));
			    setPreviousComponent(createWeekView(pos-1));
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
           	  pos = pos+1;
              setNextComponent(createWeekView(pos+1));
         }
             break;
         case BACK: {
       	  pos = pos-1;
       	  setPreviousComponent(createWeekView(pos-1));
         }}}
}
	
