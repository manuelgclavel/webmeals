package com.example.mobile;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import com.example.mobile.MobileUI;
import com.example.mobile.data.Contract;
import com.example.mobile.data.ContractOption;
import com.example.mobile.data.ContractOptionDay;
import com.example.mobile.data.ContractResidency;
import com.example.mobile.data.DailyMealSelection;
import com.example.mobile.data.DeadlineDay;
import com.example.mobile.data.FoodRegime;
import com.example.mobile.data.Guest;
import com.example.mobile.data.Meal;
import com.example.mobile.data.MealOption;
import com.example.mobile.data.MealOptionDeadline;
import com.example.mobile.data.MealOptionResidency;
import com.example.mobile.data.MealSelection;
import com.example.mobile.data.MealSelectionPlus;
import com.example.mobile.data.Residence;
import com.example.mobile.data.Residency;
import com.example.mobile.data.Role;
import com.example.mobile.data.User;
import com.example.mobile.viewer.LoginView;
import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@Theme("touchkit")
//@Widgetset("com.example.myapp.MyAppWidgetSet")
@Title("My Mobile App")

public class MobileUI extends UI {
	
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MobileUI.class, widgetset = "com.example.mobile.widgetset.MobileWidgetset")
	public static class MobileServlet extends TouchKitServlet {
	}
	
	private JDBCConnectionPool connectionPool;
	private User user;
	private Role role;
	private Residence residence; 
	private NavigationManager manager;
	
	public static final String LOGIN_COOKIE = "loginmeals";
	public static final String PASSWORD_COOKIE = "passwordmeals";
	public static final Integer COOKIE_MAX_AGE = 31536000; /** 1 year */
	

	
    @Override
    protected void init(VaadinRequest request)  {
    	
    	try {
			//connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/schweidt","root","4Meaningful");	
			//connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/allenmoos","root","admin");
			//connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/lugano","root","4Meaningful");	
			connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/ravenahl","root","admin");	
			//connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/mendaur","root","4Meaningful");
			manager = new NavigationManager(new LoginView());
			setContent(manager);
			//setContent(new LoginView());
					
			
		} catch (SQLException e) {
    	 throw new RuntimeException(e);
		}	
        
    	
    }


	public JDBCConnectionPool getConnectionPool() {
		return connectionPool;
	}

	public User getUser() {
		return user;
	}
	
	public NavigationManager getManager () {
		return manager;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Residence getResidence() {
		return residence;
	}

	public void setResidence(Residence residence) {
		this.residence = residence;
	}
	


	
	public Cookie getCookieByName(String name) { 
		// Fetch all cookies from the request 
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

		// Iterate to find cookie by its name 
		for (Cookie cookie : cookies) { 
			if (name.equals(cookie.getName())) { 
				return cookie; 
			} 
		}

		return null; 
	} 
	
	public void populateDailyMeals(JDBCConnectionPool connectionPool, 
			BeanItemContainer<DailyMealSelection> dailymeals){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM DailyMealSelection");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				dailymeals.addItem(new DailyMealSelection(result.getInt(1), result.getDate(2), result.getInt(3),
						result.getInt(4)));
			}
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateDailyMeals(JDBCConnectionPool connectionPool, 
			BeanItemContainer<DailyMealSelection> dailymeals, Date dayselected){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM DailyMealSelection where date = Date(?)");
			ps.setDate(1, new java.sql.Date(dayselected.getTime()));
			ResultSet result = ps.executeQuery();
			while (result.next()){
				dailymeals.addItem(new DailyMealSelection(result.getInt(1), result.getDate(2), result.getInt(3),
						result.getInt(4)));
			}
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateRoles(BeanItemContainer<Role> roles){
		roles.addItem(new Role(0)); 
		roles.addItem(new Role(1));
		roles.addItem(new Role(2));
		roles.addItem(new Role(3));
	}
	
	public void populateUsers(JDBCConnectionPool connectionPool, 
			BeanItemContainer<User> users){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				users.addItem(new User(result.getInt(1), result.getString(2), result.getInt(3),
						result.getString(4), result.getString(5), result.getString(6),
						result.getInt(7)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BeanItemContainer<User> selectResidents(BeanItemContainer<User> users){
		BeanItemContainer<User> residents = new BeanItemContainer<User>(User.class);
		User user;
		for (Iterator<User> i = users.getItemIds().iterator(); i.hasNext();){
			user = (User) i.next();
			if (user.getRole() == 0){
				residents.addItem(user);
			}
		}		
		return residents;
		
	}
	
	
	
	
	public void populateGuests(JDBCConnectionPool connectionPool, 
			BeanItemContainer<Guest> guests){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Guest");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				guests.addItem(new Guest(result.getInt(1), result.getString(2), 
						result.getString(3), result.getInt(4)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateMealSelections(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealSelection> mealselections){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM MealSelection");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealselections.addItem(new MealSelection(result.getInt(1), result.getInt(2), 
						result.getInt(3), result.getInt(4), result.getInt(5)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateMealSelections(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealSelection> mealselections,
			Date dayselected){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT MealSelection.pk, mealOption, foodRegime, meal, ownedBy FROM MealSelection" + " " +
							//"RIGHT JOIN " + " " +
							"INNER JOIN " + " " +
							"(SELECT * from DailyMealSelection where Date(date) = Date(?)) AS Temp" + " " +
							"ON MealSelection.ownedBy = Temp.pk");
			ps.setDate(1, new java.sql.Date(dayselected.getTime()));
			ResultSet result = ps.executeQuery();
			//List<Integer> pks = new ArrayList<Integer>();
			//for (Iterator<DailyMealSelection> i = dailymeals.getItemIds().iterator(); i.hasNext();) {
			//	DailyMealSelection dailymeal = i.next();
			//	pks.add(dailymeal.getPk());
			//}
			while (result.next()){
				//if (pks.contains(result.getInt(1))){
					mealselections.addItem(new MealSelection(result.getInt(1), result.getInt(2), 
						result.getInt(3), result.getInt(4), result.getInt(5)));
				//}
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateMealSelectionsPlus(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealSelectionPlus> mealselections,
			GregorianCalendar c){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT MealSelection.pk, mealOption, foodRegime, meal, ownedBy, selectedBy, offeredTo  FROM MealSelection" + " " +
							"INNER JOIN " + " " +
							"(SELECT * from DailyMealSelection where Date(date) = Date(?)) AS Temp" + " " +
							"ON MealSelection.ownedBy = Temp.pk");
			ps.setDate(1, new java.sql.Date(c.getTime().getTime()));
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealselections.addItem(new MealSelectionPlus(result.getInt(1), result.getInt(2), 
						result.getInt(3), result.getInt(4), result.getInt(5), result.getInt(6), result.getInt(7)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateMealSelectionsPlusPlus(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealSelectionPlus> mealselections,
			Date startselected, Date endselected){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT MealSelection.pk, mealOption, foodRegime, meal, ownedBy, selectedBy, offeredTo  FROM MealSelection" + " " +
							"INNER JOIN " + " " +
							"(SELECT * from DailyMealSelection where Date(date) >= Date(?) and Date(date) <= Date(?)) AS Temp" + " " +
							"ON MealSelection.ownedBy = Temp.pk");
			ps.setDate(1, new java.sql.Date(startselected.getTime()));
			ps.setDate(2, new java.sql.Date(endselected.getTime()));
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealselections.addItem(new MealSelectionPlus(result.getInt(1), result.getInt(2), 
						result.getInt(3), result.getInt(4), result.getInt(5), result.getInt(6), result.getInt(7)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BeanItemContainer<MealSelectionPlus> selectMealOption(BeanItemContainer<MealSelectionPlus> mealselections, MealOption mealoption){
		BeanItemContainer<MealSelectionPlus> selectedBy = new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
		MealSelectionPlus mealselection;
		for (Iterator<MealSelectionPlus> i = mealselections.getItemIds().iterator(); i.hasNext();){
			mealselection = (MealSelectionPlus) i.next();
			if (mealselection.getMealOption() == mealoption.getPk()){
				selectedBy.addItem(mealselection);
			}
		}		
		return selectedBy;
	
	}
	
	public BeanItemContainer<MealSelectionPlus> selectSelectedBy(BeanItemContainer<MealSelectionPlus> mealselections, User user){
		BeanItemContainer<MealSelectionPlus> selectedBy = new BeanItemContainer<MealSelectionPlus>(MealSelectionPlus.class);
		MealSelectionPlus mealselection;
		for (Iterator<MealSelectionPlus> i = mealselections.getItemIds().iterator(); i.hasNext();){
			mealselection = (MealSelectionPlus) i.next();
			if (mealselection.getSelectedBy() == user.getPk()){
				selectedBy.addItem(mealselection);
			}
		}		
		return selectedBy;
	
	}
	
	
	public void populateMeals(JDBCConnectionPool connectionPool, 
			BeanItemContainer<Meal> meals){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal ORDER BY position");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				meals.addItem(new Meal(result.getInt(1), result.getInt(2), 
						result.getString(3), result.getInt(4)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void populateMealOptions(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealOption> mealoptions){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM MealOption ORDER BY position");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealoptions.addItem(new MealOption(result.getInt(1), result.getInt(2), 
						result.getString(3), result.getString(4), result.getInt(5)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(guests.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateFoodRegimes(JDBCConnectionPool connectionPool, 
			BeanItemContainer<FoodRegime> foodregimes){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM FoodRegime");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				foodregimes.addItem(new FoodRegime(result.getInt(1), result.getString(2), 
						result.getString(3), result.getInt(4)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	public void populateResidences(JDBCConnectionPool connectionPool, BeanItemContainer<Residence> residences) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Residence");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				residences.addItem(new Residence(result.getInt(1), result.getString(2), 
						result.getString(3), result.getString(4)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateResidency(JDBCConnectionPool connectionPool, BeanItemContainer<Residency> periods) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Residency");
			ResultSet result = ps.executeQuery();
			
			while (result.next()){
				periods.addItem(new Residency(result.getInt(1), result.getDate(2), result.getDate(3),
						result.getInt(4), result.getInt(5), result.getInt(6), result.getInt(7),
						result.getInt(8), result.getInt(9)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateMealOptionResidency(JDBCConnectionPool connectionPool, BeanItemContainer<MealOptionResidency> mealoptionperiods) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT MealOptionDeadline_ownedBy as ownedBy, Residency_deadlines as deadlines FROM MealOptionDeadline_ownedBy__Residency_deadlines");
			ResultSet result = ps.executeQuery();
			
			while (result.next()){
				mealoptionperiods.addItem(new MealOptionResidency(result.getInt(1), result.getInt(2))); 
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateMealOptionDeadlines(JDBCConnectionPool connectionPool, BeanItemContainer<MealOptionDeadline> mealoptiondeadlines) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps =
					conn.prepareStatement("SELECT pk, cday, chour, cminute, literal, ownedBy FROM MealOptionDeadline");
			ResultSet result = ps.executeQuery();
			
			while (result.next()){
				mealoptiondeadlines.addItem(new MealOptionDeadline(result.getInt(1), result.getInt(2), result.getInt(3),
						result.getInt(4), result.getString(5), result.getInt(6))); 
			}
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateDeadlineDays(JDBCConnectionPool connectionPool, BeanItemContainer<DeadlineDay> deadlinedays) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM MealOptionDeadline_days");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				deadlinedays.addItem(new DeadlineDay(result.getInt(1), result.getInt(2))); 
			}
			result.close();
			ps.close();
			
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateContracts(JDBCConnectionPool connectionPool, BeanItemContainer<Contract> contracts) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM Contract");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				contracts.addItem(new Contract(result.getInt(1), result.getString(2), result.getInt(3))); 
			}
			result.close();
			ps.close();
			
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateContractOptions(JDBCConnectionPool connectionPool, BeanItemContainer<ContractOption> contractoptions) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM ContractOption");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				contractoptions.addItem(new ContractOption(result.getInt(1), result.getInt(2), result.getInt(3))); 
			}
			result.close();
			ps.close();
			
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateContractResidency(JDBCConnectionPool connectionPool, BeanItemContainer<ContractResidency> contractperiods) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM Contract_ownedBy__Residency_contract");
			ResultSet result = ps.executeQuery();
			
			while (result.next()){
				contractperiods.addItem(new ContractResidency(result.getInt(1), result.getInt(2))); 
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateContractOptionDays(JDBCConnectionPool connectionPool, BeanItemContainer<ContractOptionDay> contractoptiondays) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM ContractOption_days");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				contractoptiondays.addItem(new ContractOptionDay(result.getInt(1), result.getInt(2))); 
			}
			result.close();
			ps.close();
			
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void populateRegimens(JDBCConnectionPool connectionPool, BeanItemContainer<FoodRegime> regimens) {
		// TODO Auto-generated method stub
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT * FROM FoodRegime");
			ResultSet result = ps.executeQuery();
			while (result.next()){
				regimens.addItem(new FoodRegime(result.getInt(1), result.getString(2), 
						result.getString(3), result.getInt(4))); 
			}
			
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	

	public void updateUserPassword(JDBCConnectionPool connectionPool, User user, String value) {
		// TODO Auto-generated method stub
			try {
				Connection conn= connectionPool.reserveConnection();
				PreparedStatement ps = conn.prepareStatement("UPDATE User SET password=? where pk =?");
				ps.setString(1, value);
				ps.setInt(2, user.getPk());
				ps.executeUpdate();
				conn.commit();
				ps.close();
				conn.close();
				connectionPool.releaseConnection(conn);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
	
	
	


	public void createUser(String name, String surname, String login, String password, int role, int residence) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("INSERT User (name, surname, login, password, role, residence)" + " " +
							"VALUES (?,?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2,  surname);
			ps.setString(3, login);
			ps.setString(4,  password);
			ps.setInt(5, role);
			ps.setInt(6, residence);
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
}


	


	public void createGuest(String name, String surname, int residence) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("INSERT Guest (name, surname, residence)" + " " +
							"VALUES (?,?,?)");
			ps.setString(1, name);
			ps.setString(2,  surname);
			ps.setInt(3, residence);
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	public void createRegimen(String name, String description, int residence) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("INSERT FoodRegime (name, description, residence)" + " " +
							"VALUES (?,?,?)");
			ps.setString(1, name);
			ps.setString(2,  description);
			ps.setInt(3, residence);
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertContractMealOptionDay(MealOption mealoption, Contract selected, int day){
		PreparedStatement ps;
		ResultSet result;
		Connection conn;
		int newcontractoption;
		try {
			conn= connectionPool.reserveConnection();
			ps = conn.prepareStatement("INSERT ContractOption (mealOption, includedIn)" + " " +
							"VALUES (?,?)");
			ps.setInt(1, mealoption.getPk());
			ps.setInt(2,  selected.getPk());
			ps.executeUpdate();
			conn.commit();
			
			ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			result = ps.executeQuery();
			result.next();
			newcontractoption = result.getInt(1);
			
			ps = conn.prepareStatement("INSERT ContractOption_days (entity, elements)" + " " +
					"VALUES (?,?)");
			ps.setInt(1, newcontractoption);
			ps.setInt(2,  day);
			ps.executeUpdate();
			conn.commit();
			
			ps = conn.prepareStatement("INSERT ContractOption_includedIn__Contract_options (ContractOption_includedIn, Contract_options)" + " " +
					"VALUES (?,?)");
			ps.setInt(1, newcontractoption);
			ps.setInt(2,  selected.getPk());
			ps.executeUpdate();
			conn.commit();
			
			result.close();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void insertContractMealOptionOnlyDay(Contract selected, ContractOption contractoption, int day){
		PreparedStatement ps;
		Connection conn;
		try {
			conn= connectionPool.reserveConnection();
			
			ps = conn.prepareStatement("INSERT ContractOption_days (entity, elements)" + " " +
					"VALUES (?,?)");
			ps.setInt(1, contractoption.getPk());
			ps.setInt(2,  day);
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void insertMealOptionDeadline(Residency selected) {
		// TODO Auto-generated method stub
		PreparedStatement ps;
		Connection conn;
		try {
			conn= connectionPool.reserveConnection();
			
			ps = conn.prepareStatement("INSERT MealOptionDeadline (cday,cminute,chour,ownedBy)" + " " +
					"VALUES (0,0,0,?)");
			ps.setInt(1, selected.getPk());
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public void insertMealOptionDeadlineDay(MealOptionDeadline deadline, int day) {
		// TODO Auto-generated method stub
		PreparedStatement ps;
		Connection conn;
		try {
			conn= connectionPool.reserveConnection();
			
			ps = conn.prepareStatement("INSERT MealOptionDeadline_days (entity, elements)" + " " +
					"VALUES (?,?)");
			ps.setInt(1, deadline.getPk());
			ps.setInt(2,  day);
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	public void deleteContractMealOptionOnlyDay(Contract selected, ContractOption contractoption, int day){
		PreparedStatement ps;
		Connection conn;
		try {
			conn= connectionPool.reserveConnection();
			
			ps = conn.prepareStatement("DELETE FROM ContractOption_days" + " " +
					"WHERE entity = ? and elements = ?");
			ps.setInt(1, contractoption.getPk());
			ps.setInt(2,  day);
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	public void deleteMealOptionDeadline(MealOptionDeadline mealoptiondeadline) {
		// TODO Auto-generated method stub
		PreparedStatement ps;
		Connection conn;
		try {
			conn= connectionPool.reserveConnection();
			
			ps = conn.prepareStatement("DELETE FROM MealOptionDeadline_days" + " " +
					"WHERE entity = ?");
			ps.setInt(1, mealoptiondeadline.getPk());
			ps.executeUpdate();
			conn.commit();
			
			ps = conn.prepareStatement("DELETE FROM MealOptionDeadline" + " " +
					"WHERE pk = ?");
			ps.setInt(1, mealoptiondeadline.getPk());
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	public void deleteMealOptionDeadlineDay(MealOptionDeadline deadline, int day) {
		// TODO Auto-generated method stub
		PreparedStatement ps;
		Connection conn;
		try {
			conn= connectionPool.reserveConnection();
			
			ps = conn.prepareStatement("DELETE FROM MealOptionDeadline_days" + " " +
					"WHERE entity = ? and elements = ?");
			ps.setInt(1, deadline.getPk());
			ps.setInt(2,  day);
			ps.executeUpdate();
			conn.commit();
			
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void deleteMealOptionDeadlineDay(BeanItemContainer<MealOptionDeadline> filterdeadlines, int day) {
		// TODO Auto-generated method stub
		MealOptionDeadline mealoptiondeadline;
		for (Iterator<MealOptionDeadline> i = filterdeadlines.getItemIds().iterator(); i.hasNext();){
			mealoptiondeadline = (MealOptionDeadline) i.next();
			deleteMealOptionDeadlineDay(mealoptiondeadline, day);
		}
	}

	
	
	
	
	public String displayDay(int i) {
		// TODO Auto-generated method stub
		String day = "";
		switch (i) {
		case (1):
			day = "Sun";
		break;
		case (2):
			day = "Mon";
		break;
		case (3):
			day = "Tue";
		break;
		case (4): 
			day = "Wed";
		break;
		case (5):
			day = "Thu";
		break;
		case (6):
			day = "Fri";
		break;
		case (7):
			day = "Sat";
		break;
		default: day = "-1";
		}
		return day;
	}

	public String displayMonth(int i) {
		// TODO Auto-generated method stub
		String day = "";
		switch (i) {
		case (0):
			day = "Jan";
		break;
		case (1):
			day = "Feb";
		break;
		case (2): 
			day = "Mar";
		break;
		case (3):
			day = "Apr";
		break;
		case (4):
			day = "May";
		break;
		case (5):
			day = "Jun";
		break;
		case (6):
			day = "Jul";
		break;
		case (7):
			day = "Aug";
		break;
		case (8):
			day = "Sep";
		break;
		case (9):
			day = "Oct";
		break;
		case (10):
			day = "Nov";
		break;
		case (11):
			day = "Dec";
		break;	
		}
		return day;
	}
	
	public int encodeDay(int i) {
		// TODO Auto-generated method stub
		Integer day = null;
		
		switch (i) {	
		case (Calendar.MONDAY):
			day = 0;
		break;
		case (Calendar.TUESDAY): 
			day = 1;
		break;
		case (Calendar.WEDNESDAY):
			day = 2;
		break;
		case (Calendar.THURSDAY):
			day = 3;
		break;
		case (Calendar.FRIDAY):
			day = 4;
		break;
		case (Calendar.SATURDAY):
			day = 5;
		break;
		case (Calendar.SUNDAY):
			day = 6;
		break;
		}
		return day;
	}

	/**
	public Integer getDayOfWeek(GregorianCalendar c) {
		// TODO Auto-generated method stub
		Integer day = null;
		
		switch (c.get(Calendar.DAY_OF_WEEK)) {	
		case (Calendar.MONDAY):
			day = 0;
		break;
		case (Calendar.TUESDAY): 
			day = 1;
		break;
		case (Calendar.WEDNESDAY):
			day = 2;
		break;
		case (Calendar.THURSDAY):
			day = 3;
		break;
		case (Calendar.FRIDAY):
			day = 4;
		break;
		case (Calendar.SATURDAY):
			day = 5;
		break;
		case (Calendar.SUNDAY):
			day = 6;
		break;
		}
		return day;
	}

	**/
	

	public boolean existsLogin(BeanItemContainer<User> users, String login){
		boolean found = false;
		User user;
		for (Iterator<User> i = users.getItemIds().iterator(); i.hasNext();){
			user = i.next();
			if (user.getLogin().equals(login)){
				found = true;
				break;
			}
		}
		return found;
	}
	
	public boolean existsGuest(BeanItemContainer<Guest> guests, String name, String surname) {
		// TODO Auto-generated method stub
		boolean found = false;
		Guest guest;
		for (Iterator<Guest> i = guests.getItemIds().iterator(); i.hasNext();){
			guest = i.next();
			if (guest.getName().equals(name) && guest.getSurname().equals(surname)){
				found = true;
				break;
			}
		}
		return found;
	}
	

	public boolean existsRegimen(BeanItemContainer<FoodRegime> regimens, String name) {
		// TODO Auto-generated method stub
				boolean found = false;
				FoodRegime regimen;
				for (Iterator<FoodRegime> i = regimens.getItemIds().iterator(); i.hasNext();){
					regimen = i.next();
					if (regimen.getName().equals(name)){
						found = true;
						break;
					}
				}
				return found;
			}
			


	
	public Meal findMealOfMealOption(BeanItemContainer<Meal> meals, MealOption mealoption) {
		// TODO Auto-generated method stub
		Meal result = null;
		Meal meal;
		for (Iterator<Meal> i = meals.getItemIds().iterator(); i.hasNext();){
			meal = i.next();
			if (mealoption.getOwnedBy() == meal.getPk()){
				result = meal;
				break;
			}
		}
		return result;
	}
	
	public boolean isValidMealOption(final int pkmealoption, final int pkmeal, 
			final BeanItemContainer<MealOption> mealoptions){
		boolean check = false;
		MealOption mealoption;
		for (Iterator<MealOption> i = mealoptions.getItemIds().iterator(); i.hasNext();){
			mealoption = (MealOption) i.next();
			if (pkmealoption == mealoption.getPk() && pkmeal == mealoption.getOwnedBy()){
				check = true;
				break;
			}
		}
		return check;
	}


	public Residency selectPeriodsByDateAndMealOption(BeanItemContainer<Residency> periods, GregorianCalendar dayselected, MealOption mealoption) {
		// TODO Auto-generated method stub
		Residency selectedBy = null;
		Residency period;
		GregorianCalendar start = this.createGCalendarNoTime();
		GregorianCalendar end = this.createGCalendarNoTime();
		for (Iterator<Residency> i = periods.getItemIds().iterator(); i.hasNext();){
			period = (Residency) i.next();
			start.setTime(period.getStart());
			end.setTime(period.getEnd());
			if (period.getOwnedByOption() == mealoption.getPk()){
				if (start.before(dayselected) && end.after(dayselected)){
				selectedBy = period;
				break;
			}
			}
		}		
		return selectedBy;
	}


	public BeanItemContainer<MealOptionDeadline> selectMealOptionDeadlineByPeriod(BeanItemContainer<MealOptionDeadline> mealoptiondeadlines,
			Residency period) {
		BeanItemContainer<MealOptionDeadline> selectedBy = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
		if (!(period == null)){
			MealOptionDeadline mealoptiondeadline;
			for (Iterator<MealOptionDeadline> i = mealoptiondeadlines.getItemIds().iterator(); i.hasNext();){
				mealoptiondeadline = (MealOptionDeadline) i.next();
				if (mealoptiondeadline.getOwnedBy() == period.getPk()){
					selectedBy.addItem(mealoptiondeadline);
				}
			}
		}
		return selectedBy;
		
		
	}


	public MealOptionDeadline selectMealOptionDeadlineByDay(BeanItemContainer<DeadlineDay> deadlinedays, GregorianCalendar dayselected,
			BeanItemContainer<MealOptionDeadline> mealoptiondeadlines) {
		MealOptionDeadline selectedBy = null;
		MealOptionDeadline mealoptiondeadline;
		DeadlineDay deadlineday;
		for (Iterator<MealOptionDeadline> i = mealoptiondeadlines.getItemIds().iterator(); i.hasNext();){
			mealoptiondeadline = (MealOptionDeadline) i.next();
			for (Iterator<DeadlineDay> j = deadlinedays.getItemIds().iterator(); j.hasNext();){
				deadlineday = (DeadlineDay) j.next();
				if (deadlineday.getDeadline() == mealoptiondeadline.getPk()
						&& deadlineday.getDay() == encodeDay(dayselected.get(Calendar.DAY_OF_WEEK))){
						//&& deadlineday.getDay() == date.get(Calendar.DAY_OF_WEEK)){
					selectedBy = mealoptiondeadline;
					break;
					}
			}
		}
		
		return selectedBy;
		
		
	}


	public void updateMealOptionDeadlineMinutes(JDBCConnectionPool connectionPool, MealOptionDeadline mealoptiondeadline, 
			Integer minutes) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE MealOptionDeadline SET cminute=? where pk =?");
			ps.setInt(1, minutes);
			ps.setInt(2, mealoptiondeadline.getPk());
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void updateMealOptionDeadlineDays(JDBCConnectionPool connectionPool, MealOptionDeadline mealoptiondeadline,
			Integer days) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE MealOptionDeadline SET cday=? where pk =?");
			ps.setInt(1, days);
			ps.setInt(2, mealoptiondeadline.getPk());
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateMealOptionDeadlineHours(JDBCConnectionPool connectionPool, MealOptionDeadline mealoptiondeadline,
			Integer hours) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE MealOptionDeadline SET chour=? where pk =?");
			ps.setInt(1, hours);
			ps.setInt(2, mealoptiondeadline.getPk());
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void updateRegimen(int pk, String name, String description) {
		// TODO Auto-generated method stub
				try {
					Connection conn= connectionPool.reserveConnection();
					PreparedStatement ps = conn.prepareStatement("UPDATE FoodRegime SET name=?, description=? where pk =?");
					ps.setString(1, name);
					ps.setString(2, description);
					ps.setInt(3, pk);
					
					ps.executeUpdate();
					conn.commit();
					ps.close();
					conn.close();
					connectionPool.releaseConnection(conn);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
	public void updateUser(int pk, String name, String surname, String login) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE User SET name=?, surname=?, login=? where pk =?");
			ps.setString(1, name);
			ps.setString(2, surname);
			ps.setString(3, login);
			ps.setInt(4, pk);
			
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public String displayDate(GregorianCalendar c) {
		// TODO Auto-generated method stub
		//Wed, Jul 4, '01
		//Wed, 4 Jul 2001 12:08:56 -0700
		String showdate = "";
		
		//SimpleDateFormat formatter=new SimpleDateFormat("EEE, MMM d, yyyy");
		//showdate = formatter.format(c.getTime());
		//formatter.setTimeZone(c.getTimeZone());
		showdate = displayDay(c.get(Calendar.DAY_OF_WEEK)) + "," + " " +
				c.get(Calendar.DAY_OF_MONTH) + " " +
				displayMonth(c.get(Calendar.MONTH)) + " " +  " " + 
				c.get(Calendar.YEAR);
		return showdate;
	}
	
	public String displayFullDate(GregorianCalendar c) {
		// TODO Auto-generated method stub
		//Wed, Jul 4, '01
		//Wed, 4 Jul 2001 12:08:56 -0700
		String showdate = "";
		//SimpleDateFormat formatter=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		//formatter.setTimeZone(c.getTimeZone());
		//showdate = formatter.format(c.getTime());
	
		showdate = displayDay(c.get(Calendar.DAY_OF_WEEK)) + "," + " " +
				c.get(Calendar.DAY_OF_MONTH) + " " +
				displayMonth(c.get(Calendar.MONTH)) + " " +  
				Integer.valueOf(c.get(Calendar.YEAR)).toString() + ", " + " " +
				displayTwoDecimalDigit(c.get(Calendar.HOUR_OF_DAY)) + ":" +
				displayTwoDecimalDigit(c.get(Calendar.MINUTE)) + ":" +
				displayTwoDecimalDigit(c.get(Calendar.SECOND));
		return showdate;
	}
	
	public String displayTwoDecimalDigit(int i){
		String twodigit;
		if (i < 10){ 
			twodigit = "0" + Integer.valueOf(i).toString();
		} else {
			twodigit = Integer.valueOf(i).toString();
		}
		
		return twodigit;
	}
	
	
	
	public GregorianCalendar createGCalendarNoTime(){
		GregorianCalendar calendar;
		calendar = new GregorianCalendar(TimeZone.getTimeZone(this.getResidence().getZone()), 
										new Locale(this.getResidence().getLang()));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		//calendar = new GregorianCalendar(TimeZone.getTimeZone(this.getResidence().getZone()));
		//calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//calendar.setMinimalDaysInFirstWeek(4);
		//calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
		return calendar;
	}
	
	public GregorianCalendar createGCalendarWithTime(){
		GregorianCalendar calendar;
		calendar = new GregorianCalendar(TimeZone.getTimeZone(this.getResidence().getZone()), 
										new Locale(this.getResidence().getLang()));
		return calendar;
	}


	public BeanItemContainer<MealOption> filteredByDate(BeanItemContainer<MealOption> mealoptions, 
			BeanItemContainer<Residency> periods, GregorianCalendar calendar) {
		// TODO Auto-generated method stub
		BeanItemContainer<MealOption> filteredBy = new BeanItemContainer<MealOption>(MealOption.class);
		MealOption mealoption = null;
		Residency period;
		GregorianCalendar start = this.createGCalendarNoTime();
		GregorianCalendar end = this.createGCalendarNoTime();
		for (Iterator<MealOption> m = mealoptions.getItemIds().iterator(); m.hasNext();){
			mealoption = (MealOption) m.next();
			for (Iterator<Residency> i = periods.getItemIds().iterator(); i.hasNext();){
				period = (Residency) i.next();
				if (period.getOwnedByOption() == mealoption.getPk()){
					start.setTime(period.getStart());
					end.setTime(period.getEnd());
					if (start.before(calendar) && end.after(calendar)){
						filteredBy.addItem(mealoption);
						break;
					}
				}
			}
		}
		return filteredBy;
	}


	public BeanItemContainer<User> filterOnlyCurrentUsers(BeanItemContainer<User> users, BeanItemContainer<Residency> periods) {
		// TODO Auto-generated method stub
		BeanItemContainer<User> filteredBy = new BeanItemContainer<User>(User.class);
		User user;
		Residency period;
		GregorianCalendar today = this.createGCalendarNoTime();
		GregorianCalendar start = this.createGCalendarNoTime();
		GregorianCalendar end = this.createGCalendarNoTime();
		for (Iterator<User> u = users.getItemIds().iterator(); u.hasNext();){
			user = (User) u.next();
			if (user.getRole() == 0) {
				for (Iterator<Residency> m = periods.getItemIds().iterator(); m.hasNext();){
					period = (Residency) m.next();
					if (period.getOwnedBy() == user.getPk()){
						start.setTime(period.getStart());
						end.setTime(period.getEnd());
						if (start.before(today) && end.after(today)){
							filteredBy.addItem(user);
							break;
						}}}}}

		return filteredBy;
	}


	public BeanItemContainer<Residency> filteredByUserWithContract(BeanItemContainer<Residency> periods, User selected) {
		// TODO Auto-generated method stub
		BeanItemContainer<Residency> filteredBy = new BeanItemContainer<Residency>(Residency.class);
		Residency period;
		for (Iterator<Residency> m = periods.getItemIds().iterator(); m.hasNext();){
			period = (Residency) m.next();
			if (period.getOwnedBy() == selected.getPk()){ 
				filteredBy.addItem(period);
				//break;
			}}

		return filteredBy;
	}


	public BeanItemContainer<Residency> filteredByUserWithRegime(BeanItemContainer<Residency> periods, User selected) {
		// TODO Auto-generated method stub
		BeanItemContainer<Residency> filteredBy = new BeanItemContainer<Residency>(Residency.class);
		Residency period;
		for (Iterator<Residency> m = periods.getItemIds().iterator(); m.hasNext();){
			period = (Residency) m.next();
			if (period.getOwnedByRegime() == selected.getPk()){ 
				filteredBy.addItem(period);
				//break;
			}}

		return filteredBy;
	}


	public static BeanItemContainer<Residency> selectPeriodsByMealOption(BeanItemContainer<Residency> periods, MealOption mealoption) {
		// TODO Auto-generated method stub
		BeanItemContainer<Residency> mealoptionperiods = new BeanItemContainer<Residency>(Residency.class);
		Residency period;
		for (Iterator<Residency> r  = periods.getItemIds().iterator(); r.hasNext();){
			period = r.next();
			if (period.getOwnedByOption() == mealoption.getPk()){
				mealoptionperiods.addBean(period);
			}
		}
		
		return mealoptionperiods;
	}
	
	public static BeanItemContainer<MealOptionDeadline> selectDeadlinesByPeriod(BeanItemContainer<MealOptionDeadline> deadlines, Residency period) {
		// TODO Auto-generated method stub
		BeanItemContainer<MealOptionDeadline> perioddeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
		MealOptionDeadline deadline;
		for (Iterator<MealOptionDeadline> d  = deadlines.getItemIds().iterator(); d.hasNext();){
			deadline = d.next();
			if (deadline.getOwnedBy() == period.getPk()){
				perioddeadlines.addBean(deadline);
			}
		}
		
		return perioddeadlines;
	}


	public static BeanItemContainer<DeadlineDay> selectDayByDeadline(BeanItemContainer<DeadlineDay> days,
			MealOptionDeadline deadline) {
		// TODO Auto-generated method stub
		BeanItemContainer<DeadlineDay> deadlinedays = new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
		DeadlineDay day;
		for (Iterator<DeadlineDay> d  = days.getItemIds().iterator(); d.hasNext();){
			day = d.next();
			if (day.getDeadline() == deadline.getPk()){
				deadlinedays.addBean(day);
			}
		}
		return deadlinedays;
	}
	
	public static Boolean findDay(BeanItemContainer<DeadlineDay> days, int i){
		Boolean found = false;
		DeadlineDay day;
		for (Iterator<DeadlineDay> d  = days.getItemIds().iterator(); d.hasNext();){
			day = d.next();
			if (day.getDay() == i){
				found = true;
				break;
			}
		}
		
		return found;
	}


	public void insertRegimenPeriod(Date start, Date end, FoodRegime regime, User selected) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("INSERT Residency (start, end, ownedByRegime, regime)" + " " +
							"VALUES (?,?,?,?)");
			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2,  new java.sql.Date(end.getTime()));
			ps.setInt(3,  selected.getPk());
			ps.setInt(4, regime.getPk());
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	public boolean periodOverlaps(Date start, Date end, BeanItemContainer<Residency> periods) {
		// TODO Auto-generated method stub
		Boolean overlaps = false;
		Residency period;
		for (Iterator<Residency> p  = periods.getItemIds().iterator(); p.hasNext();){
			period = p.next();
			if (!(start.after(period.getEnd())) && !(end.before(period.getStart()))){
				overlaps = true;
				break;
			}
		}
		return overlaps;
	}


	public void insertContractPeriod(Date start, Date end, Contract contract, User selected) {
		// TODO Auto-generated method stub
				try {
					Connection conn= connectionPool.reserveConnection();
					PreparedStatement ps = 
							conn.prepareStatement("INSERT Residency (start, end, ownedBy, contract)" + " " +
									"VALUES (?,?,?,?)");
					ps.setDate(1, new java.sql.Date(start.getTime()));
					ps.setDate(2,  new java.sql.Date(end.getTime()));
					ps.setInt(3, selected.getPk());
					ps.setInt(4, contract.getPk());
					ps.executeUpdate();
					conn.commit();
					ps.close();
					conn.close();
					connectionPool.releaseConnection(conn);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
	}


	public BeanItemContainer<Residency> filteredByUserWithRegimeMinus(BeanItemContainer<Residency> periods,
			User selected, Residency residency) {
		// TODO Auto-generated method stub
				BeanItemContainer<Residency> filteredBy = new BeanItemContainer<Residency>(Residency.class);
				Residency period;
				for (Iterator<Residency> m = periods.getItemIds().iterator(); m.hasNext();){
					period = (Residency) m.next();
					if (period.getOwnedByRegime() == selected.getPk() && !(period.getPk() == residency.getPk())){ 
						filteredBy.addItem(period);
					}}

				return filteredBy;
	}


	public void updateRegimenPeriod(Date start, Date end, FoodRegime regime, Residency selectedperiod) {
		// TODO Auto-generated method stub
		try {
			Connection conn= connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("UPDATE Residency SET start = ?, end = ?, regime = ?" + " " +
							"WHERE pk = ?");
			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2,  new java.sql.Date(end.getTime()));
			ps.setInt(3, regime.getPk());
			ps.setInt(4, selectedperiod.getPk());
			ps.executeUpdate();
			conn.commit();
			ps.close();
			conn.close();
			connectionPool.releaseConnection(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateUserPeriodMealSelections(JDBCConnectionPool connectionPool, 
			BeanItemContainer<MealSelectionPlus> mealselections,
			Date start, Date end, User user){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT MealSelection.pk, mealOption, foodRegime, meal, ownedBy, Temp.selectedBy, Temp.offeredTo, Temp.date FROM MealSelection" + " " +
							"INNER JOIN " + " " +
							"(SELECT * from DailyMealSelection where Date(date) >= Date(?) and Date(date) <= Date(?) and selectedBy = ?) AS Temp" + " " +
							"ON MealSelection.ownedBy = Temp.pk");
			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2, new java.sql.Date(end.getTime()));
			ps.setInt(3, user.getPk());
			ResultSet result = ps.executeQuery();
			while (result.next()){
				mealselections.addItem(new MealSelectionPlus(result.getInt(1), result.getInt(2), 
						result.getInt(3), result.getInt(4), result.getInt(5), result.getInt(6), result.getInt(7), result.getDate(8)));
			}
			result.close();
			ps.close();	
			conn.close();
			connectionPool.releaseConnection(conn);
			//Notification.show(Integer.valueOf(mealselections.size()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void updateRegimeMealSelection(BeanItemContainer<MealSelectionPlus> mealselections, FoodRegime regime) {
		// TODO Auto-generated method stub
		Connection conn;
		PreparedStatement ps;
		ResultSet result;

		MealOption mealoption = null;
		MealSelectionPlus mealselection;
		MealOptionDeadline optionDeadline;

		// initializing containers
		BeanItemContainer<Residency> periods = new BeanItemContainer<Residency>(Residency.class);
		populateResidency(connectionPool, periods);
		BeanItemContainer<MealOptionDeadline> mealoptiondeadlines = new BeanItemContainer<MealOptionDeadline>(MealOptionDeadline.class);
		populateMealOptionDeadlines(connectionPool, mealoptiondeadlines);	
		BeanItemContainer<DeadlineDay> deadlinedays = new BeanItemContainer<DeadlineDay>(DeadlineDay.class);
		populateDeadlineDays(connectionPool, deadlinedays);


		GregorianCalendar gcalendar = createGCalendarNoTime();

		GregorianCalendar current = createGCalendarWithTime();
		GregorianCalendar curDeadline = null;


		try {
			conn = connectionPool.reserveConnection();

			for (Iterator<MealSelectionPlus> m = mealselections.getItemIds().iterator(); m.hasNext();){
				mealselection = (MealSelectionPlus) m.next();

				// get the current meal option
				ps = conn.prepareStatement("SELECT pk, position, initial, literal, ownedBy FROM MealOption" + " " +
						"WHERE pk = ?");
				ps.setInt(1, mealselection.getMealOption());
				result = ps.executeQuery();
				result.next();
				mealoption = new MealOption(result.getInt(1), 
						result.getInt(2), result.getString(3), result.getString(4), mealselection.getMeal());

				result.close();
				ps.close();
				
				// Get the deadline for the current meal option and day of the week

				optionDeadline = selectMealOptionDeadlineByDay(deadlinedays, gcalendar,
						selectMealOptionDeadlineByPeriod(mealoptiondeadlines, 
								selectPeriodsByDateAndMealOption(periods, gcalendar, mealoption)));

				// Get the deadline for the given meal option and day of the week
				// NO NEED: IT IS THE SAME

				// Get the actual deadline for the meal selection
				curDeadline = createGCalendarNoTime();
				curDeadline.setTime(mealselection.getDate());
				curDeadline.add(Calendar.DAY_OF_MONTH, - optionDeadline.getCday());
				curDeadline.set(Calendar.HOUR_OF_DAY, optionDeadline.getChour());
				curDeadline.set(Calendar.MINUTE, optionDeadline.getCminute());

				if (current.before(curDeadline)){
					if (regime == null){
						ps = conn.prepareStatement("UPDATE MealSelection SET foodRegime= 0 where pk = ?");
						ps.setInt(1, mealselection.getPk());
						ps.executeUpdate();
						conn.commit();
						ps.close();
					} else {
						ps.close();
						ps = conn.prepareStatement("UPDATE MealSelection SET foodRegime= ? where pk = ?");
						ps.setInt(1, regime.getPk());
						ps.setInt(2, mealselection.getPk());
						ps.executeUpdate();
						conn.commit();
						ps.close();
					}
				}
			}
			conn.close();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}