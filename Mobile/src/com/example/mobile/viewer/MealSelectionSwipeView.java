package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
public class MealSelectionSwipeView extends NavigationManager
	implements NavigationListener {
	
  int pos = 0;
  Date dayselected;

  public MealSelectionSwipeView(Date selected) {
	  this.dayselected= selected;
      // Set up the initial views
      navigateTo(createView(+pos));
      setNextComponent(createView(pos+1));
      setPreviousComponent(createView(pos-1));
      addNavigationListener((NavigationListener) this);
  }

  SwipeView createView(int currentpos) {
	  SwipeView view = new SwipeView();
      view.setSizeFull();
      
      //NavigationButton test = new NavigationButton();
	  NavigationView navview = new NavigationView();
	  navview.setPreviousComponent(new UserMenuView());
	  //navview.setPreviousComponent(new CloseSessionView());
      navview.setSizeFull();
      
      /**
      Button menu = new Button();
      menu.setCaption("");
      menu.addClickListener(new ClickListener(){

    	  @Override
    	  public void buttonClick(ClickEvent event) {
    		  // TODO Auto-generated method stub
    		  ((MobileUI) UI.getCurrent()).getManager().navigateTo(new UserMenuView());
    	  }});
      navview.setLeftComponent(menu);
	
      Button logout = new Button();
      logout.setCaption("Exit");
      logout.addClickListener(new ExitBehavior());
      navview.setRightComponent(logout);
       */
  	
      //((NavigationButton) navview.getNavigationBar().getLeftComponent())
      //	.addClickListener(new ExitBehavior());
     

      // Use an inner layout to center the image
      VerticalComponentGroup layout = new VerticalComponentGroup();
      layout.setSizeFull();
 
     //HorizontalButtonGroup top = new HorizontalButtonGroup();
     //Button exit = new Button("Exit");
     //exit.addClickListener(new ExitBehavior());
     //top.addComponent(exit);
     //layout.addComponent(top);
     
      
      HorizontalLayout datepanel = new HorizontalLayout();
		datepanel.setWidth("100%");
		
		final Button prev = new Button("-1");
		final Button now = new Button("^");
		final DateField dateshown =  new DateField();
		final Button next = new Button("+1");
		

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
		
		layout.addComponent(new SelectionDateView(c.getTime()));
		navview.setContent(layout);
		view.setContent(navview);
		
		
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

