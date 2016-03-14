package com.example.mobile.viewer;

import java.util.Iterator;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.Guest;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.Residence;
import com.example.mobile.data.User;
import com.example.mobile.data.Residency;
import com.example.mobile.presenter.ExitBehavior;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DatabaseCheckView extends NavigationView {
	final private MobileUI ui = (MobileUI) UI.getCurrent();
	final private JDBCConnectionPool connectionPool = ui.getConnectionPool();
	final private BeanItemContainer<DailyMealSelection> dailymeals ;
	final private BeanItemContainer<User> users;
	final private BeanItemContainer<Guest> guests;
	final private BeanItemContainer<MealSelection> mealselections;
	final private BeanItemContainer<Meal> meals;
	final private BeanItemContainer<MealOption> mealoptions;
	final private BeanItemContainer<Residence> residences;
	final private BeanItemContainer<Contract> contracts;
	final private BeanItemContainer<Residency> periods;
	final private BeanItemContainer<MealOptionDeadline> deadlines;
	final private BeanItemContainer<DeadlineDay> deadlinedays;

	public DatabaseCheckView(){
		VerticalComponentGroup layout = new VerticalComponentGroup();
		Button logout = new Button();
		logout.setCaption("Exit");
		setRightComponent(logout);
		logout.addClickListener(new ExitBehavior());

		Button back = new Button();
		back.setCaption("System menu");
		setLeftComponent(back);
		back.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				((MobileUI) UI.getCurrent()).getManager().navigateTo(new SystemMenuView());
			} });


		dailymeals = new BeanItemContainer<DailyMealSelection>(DailyMealSelection.class);
		ui.populateDailyMeals(connectionPool, dailymeals);
		guests = new BeanItemContainer<Guest>(Guest.class);
		ui.populateGuests(connectionPool, guests);
		users = new BeanItemContainer<User>(User.class);
		ui.populateUsers(connectionPool, users);
		mealselections = new BeanItemContainer<MealSelection>(MealSelection.class);
		ui.populateMealSelections(connectionPool, mealselections);
		meals = new BeanItemContainer<Meal>(Meal.class);
		ui.populateMeals(connectionPool, meals);
		mealoptions = new BeanItemContainer<MealOption>(MealOption.class);
		ui.populateMealOptions(connectionPool, mealoptions);
		residences = new BeanItemContainer<Residence>(Residence.class);
		ui.populateResidences(connectionPool, residences);
		contracts = new BeanItemContainer<Contract>(Contract.class);
		ui.populateContracts(connectionPool, contracts);
		periods = new BeanItemContainer<Residency>(Residency.class);
		ui.populateResidency(connectionPool, periods);
		deadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
		ui.populateMealOptionDeadlines(connectionPool, deadlines);
		deadlinedays = new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
		ui.populateDeadlineDays(connectionPool, deadlinedays);


		/** DailyMeal */
		Button check0 = new Button("DailyMeals-Either-User-Or-Guest");
		layout.addComponent(check0);
		Button check1 = new Button("DailyMeals-Users");
		layout.addComponent(check1);
		Button check2 = new Button("DailyMeals-Guests");
		layout.addComponent(check2);


		/** MealSelection */
		Button check3 = new Button("Only-One-MealSelection-Meal-Per-DailyMeals");
		layout.addComponent(check3);
		Button check4 = new Button("MealSelection-DailyMeals");
		layout.addComponent(check4);
		Button check5 = new Button("MealSelection-Meals");
		layout.addComponent(check5);
		Button check6 = new Button("MealSelection-MealOptions");
		layout.addComponent(check6);
		Button check7 = new Button("MealSelection-MealOption-In-Meal");
		layout.addComponent(check7);

		/** Meal */
		Button check8 = new Button("Meal-Unique-Position");
		layout.addComponent(check8);
		Button check9 = new Button("Meal-Unique-Literal");
		layout.addComponent(check9);
		// MISSING: Meal-Residence

		/** MealOption */
		Button check10 = new Button("MealOption-Unique-Position");
		layout.addComponent(check10);
		Button check11 = new Button("MealOption-Unique-Initial-Per-Meal");
		layout.addComponent(check11);
		Button check12 = new Button("MealOption-Unique-Literal-Per-Meal");
		layout.addComponent(check12);
		Button check13 = new Button("MealOption-Meals");
		layout.addComponent(check13);


		/** User */
		Button check20 = new Button("User-Roles");
		layout.addComponent(check20);
		Button check21 = new Button("User-Residences");
		layout.addComponent(check21);

		/** Contract */
		Button check30 = new Button("Contract-Unique-Name");
		layout.addComponent(check30);
		Button check31 = new Button("Contract-Residences");
		layout.addComponent(check31);

		/** Deadlines */
		Button check40 = new Button("Deadline-AtMost-One-Per-Day");
		layout.addComponent(check40);


		setContent(layout);

		check0.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int count = 0;
				DailyMealSelection dailymeal;
				//Boolean found = false;
				for (Iterator<DailyMealSelection> i = dailymeals.getItemIds().iterator(); i.hasNext();){
					dailymeal = (DailyMealSelection) i.next();
					if (dailymeal.getSelectedBy() == 0 && dailymeal.getOfferedTo() == 0) { 
						count++ ; 
					}
				}

				//Notification.show(Integer.valueOf(dailymeals.size()).toString());
				Notification.show(Integer.valueOf(count).toString());
			}});

		check1.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int count = 0;
				DailyMealSelection dailymeal;
				Boolean found;
				for (Iterator<DailyMealSelection> i = dailymeals.getItemIds().iterator(); i.hasNext();){
					dailymeal = (DailyMealSelection) i.next();
					if (!(dailymeal.getSelectedBy() == 0)){
						found = false;
						User user;
						for (Iterator<User> j = users.getItemIds().iterator(); j.hasNext();){
							user = (User) j.next();
							if (dailymeal.getSelectedBy() == user.getPk()){
								found = true;
								break;
							}
						}
						//	};
						if (found == false) { count++ ; }
					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check2.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				DailyMealSelection dailymeal;
				Boolean found;
				for (Iterator<DailyMealSelection> i = dailymeals.getItemIds().iterator(); i.hasNext();){
					dailymeal = (DailyMealSelection) i.next();
					if (!(dailymeal.getOfferedTo() == 0)){
						found = false;
						Guest guest;
						for (Iterator<Guest> h = guests.getItemIds().iterator(); h.hasNext();){
							guest = (Guest) h.next();
							if (dailymeal.getOfferedTo() == guest.getPk()){
								found = true;
								break;
							}
						}
						//	};
						if (found == false) { 
							count++ ; }
					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check3.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealSelection mealselection1;
				Boolean found;
				for (Iterator<MealSelection> i = mealselections.getItemIds().iterator(); i.hasNext();){
					found = true;
					mealselection1 = (MealSelection) i.next();
					MealSelection mealselection2;
					for (Iterator<MealSelection> h = mealselections.getItemIds().iterator(); h.hasNext();){
						mealselection2 = (MealSelection) h.next();	
						if (!(mealselection1.getPk() == mealselection2.getPk())
								&& (mealselection1.getOwnedBy() == mealselection2.getOwnedBy())
								&& (mealselection1.getMeal() == mealselection2.getMeal())){
							found = false;
							break;
						}
					}
					if (found == false) { 
						count++ ; }
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check4.addClickListener(new ClickListener(){
			/** MealSelection-DailyMeals */

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealSelection mealselection;
				DailyMealSelection dailymeal;
				Boolean found;
				for (Iterator<MealSelection> i = mealselections.getItemIds().iterator(); i.hasNext();){
					found = false;
					mealselection = (MealSelection) i.next();
					for (Iterator<DailyMealSelection> h = dailymeals.getItemIds().iterator(); h.hasNext();){
						dailymeal = (DailyMealSelection) h.next();
						if(mealselection.getOwnedBy() == dailymeal.getPk()) { 
							found = true ; 
							break ;
						}
					}
					if (found == false) {count++ ;} 
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check5.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealSelection mealselection;
				Meal meal;
				Boolean found;
				for (Iterator<MealSelection> i = mealselections.getItemIds().iterator(); i.hasNext();){
					found = false;
					mealselection = (MealSelection) i.next();
					for (Iterator<Meal> h = meals.getItemIds().iterator(); h.hasNext();){
						meal = (Meal) h.next();
						if(mealselection.getMeal() == meal.getPk()) { 
							found = true ; 
							break;
						}
					}
					if (found == false) {count++ ;} 
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check6.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealSelection mealselection;
				MealOption mealoption;
				Boolean found;
				for (Iterator<MealSelection> i = mealselections.getItemIds().iterator(); i.hasNext();){
					found = false;
					mealselection = (MealSelection) i.next();
					for (Iterator<MealOption> h = mealoptions.getItemIds().iterator(); h.hasNext();){
						mealoption = (MealOption) h.next();
						if(mealselection.getMealOption() == mealoption.getPk()) { 
							found = true ; 
							break ;}
					}
					if (found == false) {count++ ;} 
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check7.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealSelection mealselection;
				for (Iterator<MealSelection> i = mealselections.getItemIds().iterator(); i.hasNext();){
					mealselection = (MealSelection) i.next();
					if (! ui.isValidMealOption(mealselection.getMealOption(), 
							mealselection.getMeal(), mealoptions)) { 
						count++ ; 
					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check8.addClickListener(new ClickListener(){
			/** Meal-Unique-Position */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				Meal meal1;
				Meal meal2;
				for (Iterator<Meal> i = meals.getItemIds().iterator(); i.hasNext();){
					meal1 = (Meal) i.next();
					for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();){
						meal2 = (Meal) j.next();
						if (!(meal1.getPk() == meal2.getPk()) 
								&& (meal1.getPosition() == meal2.getPosition())) { 
							count++ ; 
							break;
						}

					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check9.addClickListener(new ClickListener(){
			/** Meal-Unique-Literal */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				Meal meal1;
				Meal meal2;
				for (Iterator<Meal> i = meals.getItemIds().iterator(); i.hasNext();){
					meal1 = (Meal) i.next();
					for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();){
						meal2 = (Meal) j.next();
						if (!(meal1.getPk() == meal2.getPk()) 
								&& (meal1.getLiteral() == meal2.getLiteral())) { 
							count++ ; 
							break;
						}

					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check10.addClickListener(new ClickListener(){
			/** MealOption-Unique-Position-Per-Meal */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealOption mealoption1;
				MealOption mealoption2;
				for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
					mealoption1 = (MealOption) i.next();
					for (Iterator<MealOption> j = mealoptions.getItemIds().iterator(); j.hasNext();){
						mealoption2 = (MealOption) j.next();
						if (!(mealoption1.getPk() == mealoption2.getPk()) 
								&& (mealoption1.getOwnedBy() == mealoption2.getOwnedBy())
								&& (mealoption1.getPosition() == mealoption2.getPosition())) { 
							count++ ; 
							break;
						}

					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check11.addClickListener(new ClickListener(){
			/** MealOption-Unique-Initial-Per-Meal */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealOption mealoption1;
				MealOption mealoption2;
				for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
					mealoption1 = (MealOption) i.next();
					for (Iterator<MealOption> j = mealoptions.getItemIds().iterator(); j.hasNext();){
						mealoption2 = (MealOption) j.next();
						if (!(mealoption1.getPk() == mealoption2.getPk()) 
								&& (mealoption1.getOwnedBy() == mealoption2.getOwnedBy())
								&& (mealoption1.getInitial() == mealoption2.getInitial())) { 
							count++ ; 
							break;
						}

					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check12.addClickListener(new ClickListener(){
			/** MealOption-Unique-Literal-Per-Meal */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealOption mealoption1;
				MealOption mealoption2;
				for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
					mealoption1 = (MealOption) i.next();
					for (Iterator<MealOption> j = mealoptions.getItemIds().iterator(); j.hasNext();){
						mealoption2 = (MealOption) j.next();
						if (!(mealoption1.getPk() == mealoption2.getPk()) 
								&& (mealoption1.getOwnedBy() == mealoption2.getOwnedBy())
								&& (mealoption1.getLiteral() == mealoption2.getLiteral())) { 
							count++ ; 
							break;
						}

					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		check13.addClickListener(new ClickListener(){
			/** MealOption-Meals */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				MealOption mealoption;
				Meal meal;
				boolean found;
				for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
					found = false;
					mealoption = (MealOption) i.next();
					for (Iterator<Meal> j = meals.getItemIds().iterator(); j.hasNext();){
						meal = (Meal) j.next();
						if (mealoption.getOwnedBy() == meal.getPk()) { 
							found = true;
							break; 
						}
					}
					if (found == false ) { count++ ; }
				}
				Notification.show(Integer.valueOf(count).toString());
			}});


		/** USERS **/

		check20.addClickListener(new ClickListener(){
			/** User-Role */
			/** Remember that role cannot be NULL **/

			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int count = 0;
				User user;
				for (Iterator<User> i = users.getItemIds().iterator(); i.hasNext();){
					user = (User) i.next();
					if ((user.getRole() < 0) || (user.getRole() > 3)){
						count++;
					}
				}

				Notification.show(Integer.valueOf(count).toString());
			}});


		check21.addClickListener(new ClickListener(){
			/** User-Residence */
			/** Remember that if residence is NULL, then getResidence() returns 0 */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int count = 0;
				User user;
				Residence residence;
				Boolean found;
				for (Iterator<User> i = users.getItemIds().iterator(); i.hasNext();){
					user = (User) i.next();
					found = false ;
					for (Iterator<Residence> j = residences.getItemIds().iterator(); j.hasNext();){
						residence = (Residence) j.next();
						if (user.getResidence() == residence.getPk()){
							found = true;
							break;
						}
					}
					if (found == false) { count++ ; }
				}
				Notification.show(Integer.valueOf(count).toString());
			}});

		/** Contracts **/

		check30.addClickListener(new ClickListener(){
			/** Contract-Unique-Name */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Notification.show(Integer.valueOf(guests.size()).toString());
				int count = 0;
				Contract contract1;
				Contract contract2;
				for (Iterator<Contract> i = contracts.getItemIds().iterator(); i.hasNext();){
					contract1 = (Contract) i.next();
					for (Iterator<Contract> j = contracts.getItemIds().iterator(); j.hasNext();){
						contract2 = (Contract) j.next();
						if (!(contract1.getPk() == contract2.getPk()) 
								&& (contract1.getName() == contract2.getName())) { 
							count++ ; 
							break;
						}

					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}});


		check31.addClickListener(new ClickListener(){
			/** Contract-Residence */
			/** Remember that if residence is NULL, then getResidence() returns 0 */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int count = 0;
				Contract contract;
				Residence residence;
				Boolean found;
				for (Iterator<Contract> i = contracts.getItemIds().iterator(); i.hasNext();){
					contract = (Contract) i.next();
					found = false ;
					for (Iterator<Residence> j = residences.getItemIds().iterator(); j.hasNext();){
						residence = (Residence) j.next();
						if (contract.getResidence() == residence.getPk()){
							found = true;
							break;
						}
					}
					if (found == false) { count++ ; }
				}
				Notification.show(Integer.valueOf(count).toString());
			}});


		check40.addClickListener(new ClickListener(){
			/** Deqdline-AtMost-One-Per-Day */
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int count = 0;
				Boolean found;
				MealOption mealoption;
				BeanItemContainer<Residency> mealoptionperiods;
				BeanItemContainer<MealOptionDeadline> periodmealoptiondeadlines;
				BeanItemContainer<DeadlineDay> deadlineperiodmealoptiondays;
				for (Iterator<MealOption> m = mealoptions.getItemIds().iterator(); m.hasNext();){
					mealoption = (MealOption) m.next();
					mealoptionperiods = MobileUI.selectPeriodsByMealOption(periods,mealoption); 
					Residency period;
					for (Iterator<Residency> r = mealoptionperiods.getItemIds().iterator(); r.hasNext();){
						period = r.next();
						periodmealoptiondeadlines = MobileUI.selectDeadlinesByPeriod(deadlines,period);
						MealOptionDeadline deadline;
						for (int i = 0; i <= 6; i++){
							found = false;
							for (Iterator<MealOptionDeadline> d = periodmealoptiondeadlines.getItemIds().iterator(); d.hasNext();){
							deadline = d.next();
							deadlineperiodmealoptiondays = MobileUI.selectDayByDeadline(deadlinedays,deadline);
							if (MobileUI.findDay(deadlineperiodmealoptiondays, i)){
								if (found == true){
										count = count + 1;
									} else {
										found = true;
										//count = count + 1;
									}
								}
							}
						}
					}
				}
				Notification.show(Integer.valueOf(count).toString());
			}
		});

	}
}