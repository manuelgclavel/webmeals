package com.example.mobile.viewer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.mobile.MobileUI;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
public class MealSelectionSwipeView extends NavigationManager
//public class MealSelectionSwipeView extends NavigationView
	implements NavigationListener {
	
  int pos = 0;
  Date dayselected;
  int diner;
  int type = 1;


  public MealSelectionSwipeView(Date selected, int diner, int type) {
	  // TODO Auto-generated constructor stub
	  setCaption("Meal selection (by day)");
	  //this.manager = getNavigationManager();
	  this.dayselected= selected;
	  this.diner = diner;
	  this.type = type;
	  navigateTo(createView(+pos));
	  setNextComponent(createView(pos+1));
	  setPreviousComponent(createView(pos-1));
	  addNavigationListener((NavigationListener) this);
  }



private Component createView(int currentpos) {
	// TODO Auto-generated method stub
	 SwipeView view = new SwipeView();

     // Use an inner layout to center the image
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
			 switch (type) {
	            case 1:  ((MobileUI) UI.getCurrent()).getManager().navigateTo(new UserMenuView());
	                     break;
	            case 2:  ((MobileUI) UI.getCurrent()).getManager().navigateTo(new AdminMenuView());
	            		break;
	            default : break;
	            
				}
			
		}});
     
     logout.addClickListener(new ExitBehavior());
    
     layout.addComponent(top);
     
     
		final Button prev = new Button("prev");
		final Button now = new Button("today");
		final DateField dateshown =  new DateField();
		final Button next = new Button("next");
		//final Button week = new Button("week");
		

		Calendar c = new GregorianCalendar();
		c.setTime(dayselected);
		c.add(Calendar.DATE, currentpos);
		
		dateshown.setValue(c.getTime());
		dateshown.setDateFormat("EEE, MMM d, ''yy");

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
		
		layout.addComponent(new SelectionDateView(c.getTime(), diner, type));
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
				navigateTo(getNextComponent());
			}
			
		});
		
		prev.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				navigateTo(getPreviousComponent());
			}
			
		});
		
		now.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
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

