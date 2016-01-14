package com.example.mobile.viewer;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MealCountSwipeView extends NavigationManager implements NavigationListener {
	
	private MobileUI ui = (MobileUI) UI.getCurrent();
	private int pos = 0;
	private boolean show;
	private Date dayselected;

  public MealCountSwipeView(boolean showusers) {
	 
	 GregorianCalendar c = ui.createGCalendarNoTime();
	 dayselected= c.getTime();
	 
	 show = showusers;
	 
      // Set up the initial views
	  setCurrentComponent(createView(+pos));
      setNextComponent(createView(pos+1));
      setPreviousComponent(createView(pos-1));
      addNavigationListener((NavigationListener) this);
  }

  SwipeView createView(int currentpos) {
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
			if (show){
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new AdminMenuView());
			} else {
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new CookMenuView());
			}
			
		}});
     
     logout.addClickListener(new ExitBehavior());
    
     layout.addComponent(top);
    
    
		
		final Button prev = new Button("prev");
		final Button now = new Button("today");
		final DateField dateshown =  new DateField();
		final Button next = new Button("next");
		

		GregorianCalendar c = ui.createGCalendarNoTime();
		c.setTime(dayselected);
		c.add(Calendar.DATE, currentpos);
		dateshown.setValue(c.getTime());
		//dateshown.setTimeZone(c.getTimeZone());
		//dateshown.setLocale(new Locale(ui.getResidence().getLang()));
		dateshown.setDateFormat("EEE, MMM d, yyyy");
		
		layout.addComponent(dateshown);
		
		HorizontalButtonGroup buttons = new HorizontalButtonGroup();
		
		buttons.addComponent(prev);
		buttons.addComponent(next);
		buttons.addComponent(now);
		
		layout.addComponent(buttons);
		
		/** GREAT FOR DEBUGGING */
		//layout.addComponent(new Label(c.getTime().toString()));
		//layout.addComponent(new Label(Integer.valueOf(currentpos).toString()));
		/** END */

		layout.addComponent(new CountView(c, show));
		view.setContent(layout);
		
		
		dateshown.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				GregorianCalendar c = ui.createGCalendarNoTime();
				c.setTime(((Date) event.getProperty().getValue()));
				pos = 0;
				dayselected = c.getTime();
				setCurrentComponent(createView(+pos));
				setNextComponent(createView(pos+1));
				setPreviousComponent(createView(pos-1));
			}

		});


		
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
				pos = 0;
				GregorianCalendar c = ui.createGCalendarNoTime();
				dayselected= c.getTime();
				
				setCurrentComponent(createView(+pos));
				setNextComponent(createView(pos+1));
			    setPreviousComponent(createView(pos-1));
			}
			
		});
		
		return view;
		
  }
  
  
  @Override
  public void navigate(NavigationEvent event) {
      switch (event.getDirection()) {
          case FORWARD: {
            	  pos = pos+1;
                  setNextComponent(createView(pos+1));
          }
              break;
          case BACK: {
        	  pos = pos-1;
        	  setPreviousComponent(createView(pos-1));
          }
      }
  }
}
