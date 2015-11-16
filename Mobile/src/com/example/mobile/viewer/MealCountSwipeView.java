package com.example.mobile.viewer;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class MealCountSwipeView extends NavigationManager implements NavigationListener {
	
  int pos = 0;
  Date dayselected;

  public MealCountSwipeView(Date selected) {
	  this.dayselected= selected;
      // Set up the initial views
      navigateTo(createView(+pos));
      setNextComponent(createView(pos+1));
      setPreviousComponent(createView(pos-1));
      addNavigationListener((NavigationListener) this);
  }

  SwipeView createView(int currentpos) {
      SwipeView view = new SwipeView();
      NavigationView navview = new NavigationView();
    		  
      view.setSizeFull();
      navview.setSizeFull();
      //navview.setPreviousComponent(new AdminMenuView());
     
      
      
      Button logout = new Button();
      logout.setCaption("Exit");
      logout.addClickListener(new ExitBehavior());
      navview.setLeftComponent(logout);

      // Use an inner layout to center the image
      //NavigationBar nav = new NavigationBar();
      //nav.setPreviousView(getPreviousComponent());
     
      VerticalComponentGroup layout = new VerticalComponentGroup();
      layout.setSizeFull();
 
     //HorizontalButtonGroup top = new HorizontalButtonGroup();
     //Button exit = new Button("<");
     //exit.addStyleName("rav");
     
    
     //exit.addClickListener(new ClickListener(){

	//	@Override
	//	public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
	//		navigateTo(new AdminMenuView());
	//	}});
     
     
     //top.addComponent(exit);
     //layout.addComponent(top);
   
      //layout.addComponent(exit);
     
      
      HorizontalLayout datepanel = new HorizontalLayout();
		datepanel.setWidth("100%");
		
		final Button prev = new Button("prev");
		final Button now = new Button("today");
		final DateField dateshown =  new DateField();
		final Button next = new Button("next");
		

		Calendar c = new GregorianCalendar();
		c.setTime(dayselected);
		c.add(Calendar.DATE, currentpos);
		dateshown.setValue(c.getTime());
		dateshown.setDateFormat("EEE, MMM d, ''yy");

		datepanel.addComponent(dateshown);
		datepanel.addComponent(prev);
		datepanel.addComponent(next);
		datepanel.addComponent(now);
		
		datepanel.setExpandRatio(prev, 1);
		datepanel.setExpandRatio(now, 1);
		datepanel.setExpandRatio(next, 1);
		datepanel.setExpandRatio(dateshown, 3);
	
		layout.addComponent(datepanel);
		
		
		/** GREAT FOR DEBUGGING */
		//layout.addComponent(new Label(c.getTime().toString()));
		//layout.addComponent(new Label(Integer.valueOf(currentpos).toString()));
		/** END */
		
		//layout.addComponent(new SelectionDateView(c.getTime()));
		layout.addComponent(new CountView(c.getTime()));
		//navview.setContent(layout);
		view.setContent(layout);
		
		
		dateshown.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Calendar c = new GregorianCalendar();
				c.setTime(((Date) event.getProperty().getValue()));
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 
						c.get(Calendar.DATE), 0, 0, 0);
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
				Calendar c = new GregorianCalendar();
				c.setTime(dayselected);
				c.add(Calendar.DATE, +1);
				navigateTo(getNextComponent());
			}
			
		});
		
		prev.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Calendar c = new GregorianCalendar();
				c.setTime(dayselected);
				c.add(Calendar.DATE, -1);
				
				navigateTo(getPreviousComponent());
			}
			
		});
		
		now.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				pos = 0;
				dayselected = Calendar.getInstance().getTime();
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
