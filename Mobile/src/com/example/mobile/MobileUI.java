package com.example.mobile;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
			connectionPool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver","jdbc:mysql://localhost/ravenahl","root","admin");	
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
							"RIGHT JOIN " + " " +
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
			Date dayselected){
		try {
			Connection conn = connectionPool.reserveConnection();
			PreparedStatement ps = 
					conn.prepareStatement("SELECT MealSelection.pk, mealOption, foodRegime, meal, ownedBy, selectedBy, offeredTo  FROM MealSelection" + " " +
							"INNER JOIN " + " " +
							"(SELECT * from DailyMealSelection where Date(date) = Date(?)) AS Temp" + " " +
							"ON MealSelection.ownedBy = Temp.pk");
			ps.setDate(1, new java.sql.Date(dayselected.getTime()));
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
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meal");
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
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM MealOption");
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
	
	
	
	public String displayDay(int dayofweek) {
		// TODO Auto-generated method stub
		String day = "";
		switch (dayofweek) {
		case (1):
			day = "Mon";
		break;
		case (2):
			day = "Tue";
		break;
		case (3): 
			day = "Wed";
		break;
		case (4):
			day = "Thu";
		break;
		case (5):
			day = "Fri";
		break;
		case (6):
			day = "Sat";
		break;
		case (7):
			day = "Sun";
		break;
		}
		return day;
	}

	

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

	
}