package com.example.mobile.viewer;

	import java.util.Calendar;
	import java.util.Date;

	import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
	import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
	import com.vaadin.addon.touchkit.ui.NavigationView;
	import com.vaadin.addon.touchkit.ui.SwipeView;
	import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
	import com.vaadin.data.Property.ValueChangeEvent;
	import com.vaadin.data.Property.ValueChangeListener;
	import com.vaadin.shared.ui.MarginInfo;
	import com.vaadin.ui.Button;
	import com.vaadin.ui.CssLayout;
	import com.vaadin.ui.Button.ClickEvent;
	import com.vaadin.ui.Button.ClickListener;
	import com.vaadin.ui.DateField;
	import com.vaadin.ui.HorizontalLayout;
	import com.vaadin.ui.Panel;
	import com.vaadin.ui.VerticalLayout;

	@SuppressWarnings("serial")
	public class MealSelectionDateView extends SwipeView {

		final private Button prev;
		final private Button next;
		final private Button now;
		final private DateField date;
		private Date selected;
		
		private Panel panel = new Panel();

		public MealSelectionDateView(Date selected){
			
			this.setSelected(selected);
			
		//final CssLayout content = new CssLayout();
			
			final VerticalComponentGroup content = new VerticalComponentGroup();
			
			content.setWidth("100%");
			
			HorizontalLayout datepanel = new HorizontalLayout();
			datepanel.setWidth("100%");
			
			prev = new Button("prev");
			now = new Button("reset");
			date =  new DateField();
			next = new Button("next");
			
			//date.setValue(new Date());
			date.setValue(selected);
			//date.setDateFormat("yyyy-MM-dd");
			date.setDateFormat("EEE, MMM d, ''yy");

			datepanel.addComponent(date);
			datepanel.addComponent(prev);
			datepanel.addComponent(next);
			datepanel.addComponent(now);
			
			datepanel.setExpandRatio(prev, 1);
			datepanel.setExpandRatio(now, 1);
			datepanel.setExpandRatio(next, 1);
			datepanel.setExpandRatio(date, 3);
			
			
			content.addComponent(datepanel);

			panel = new SelectionView(date.getValue());	
			content.addComponent(panel);

			setContent(content);
			
			date.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					content.removeComponent(panel);
					panel = new SelectionView((Date) event.getProperty().getValue());	
					content.addComponent(panel);
				}
			});
			
			prev.addClickListener(new ClickListener(){

				@Override
				public void buttonClick(ClickEvent event) {            
				    Calendar c = Calendar.getInstance();  
				    c.setTime(date.getValue());
				    c.add(Calendar.DATE, -1);  // number of days to add  
				    date.setValue(c.getTime());
				}
			});
			
			now.addClickListener(new ClickListener(){

				@Override
				public void buttonClick(ClickEvent event) {            
				    Calendar c = Calendar.getInstance();  
				    //c.setTime(date.getValue());
				    //c.add(Calendar.DATE, +0);  // number of days to add  
				    date.setValue(c.getTime());
				}
			});
			
			next.addClickListener(new ClickListener(){

				@Override
				public void buttonClick(ClickEvent event) {     
				    Calendar c = Calendar.getInstance();  
				    c.setTime(date.getValue());
				    c.add(Calendar.DATE, 1);  // number of days to add  
				    date.setValue(c.getTime());
				}
			});
			
			
		}

		public Date getSelected() {
			return selected;
		}

		public void setSelected(Date selected) {
			this.selected = selected;
		}
	}