import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ShopsAndGo {
	ShopsAndGo() {
		new LoginPage();
	}
	public static void main(String[] args) {
		new ShopsAndGo();
	}
}

class LoginPage extends JFrame implements ActionListener {

	JButton customerLoginButton, shopkeeperLoginButton, customerRegisterButton, shopkeeperRegisterButton;
	DatabaseHandler database;
	LoginPage() {
		database=new DatabaseHandler();
		customerLoginButton = new JButton("Customer Login");
		shopkeeperLoginButton = new JButton("Shopkeeper Login");
		customerRegisterButton = new JButton("Customer Register");
		shopkeeperRegisterButton = new JButton("Shopkeeper Register");

		customerLoginButton.addActionListener(this);
		shopkeeperLoginButton.addActionListener(this);
		customerRegisterButton.addActionListener(this);
		shopkeeperRegisterButton.addActionListener(this);
		
		//createExpectedTables();
		
		add(customerLoginButton);
		add(shopkeeperLoginButton);
		add(customerRegisterButton);
		add(shopkeeperRegisterButton);
		frameInitialize();
	}

	private void createExpectedTables() {
		database.createTable("CREATE TABLE SHOPKEEPER(NAME TEXT,USERNAME TEXT,PASSWORD TEXT,EMAIL TEXT)");
		database.createTable("CREATE TABLE CUSTOMER(NAME TEXT,USERNAME TEXT,PASSWORD TEXT,EMAIL TEXT)");
		database.createTable("CREATE TABLE SHOPS(USERNAME TEXT,SHOPNAME TEXT,SHOPTYPE TEXT,SHOPCITY TEXT)");
		database.createTable("CREATE TABLE PRODUCTS(USERNAME TEXT,PRODUCTNAME TEXT,"
				+ "PRODUCTPRICE TEXT,PRODUCTAVAILABILITY TEXT,PRODUCTDESCRIPTION TEXT)");
		database.createTable("CREATE TABLE CATEGORIES(USERNAME TEXT,CATEGORY TEXT,PRODUCTNAME TEXT)");
		database.createTable("CREATE TABLE ORDERS(CUSTOMER_USERNAME TEXT,"
				+ "SHOPKEEPER_USERNAME TEXT,SHOP_NAME TEXT,CUSTOMER_NAME TEXT,ORDER_INFO TEXT,ORDERED_TIME TEXT,CUSTOMER_ADDRESS TEXT)");
		database.createTable("CREATE TABLE CART(CUSTOMER_USERNAME TEXT,"
				+ "SHOPKEEPER_USERNAME TEXT,SHOP_NAME TEXT,CUSTOMER_NAME TEXT,ORDER_INFO TEXT,ORDERED_TIME TEXT)");
		database.createTable("CREATE TABLE ORDERSTATUS(CUSTOMER_USERNAME TEXT,SHOPKEEPER_USERNAME,ORDERED_TIME TEXT"
				+ ",ORDER_STATUS TEXT,CONTACT_NUMBER TEXT,ORDER_INFO TEXT)");
		database.createTable("CREATE TABLE ORDERSTATUS(CUSTOMER_USERNAME TEXT,SHOPKEEPER_USERNAME,ORDERED_TIME TEXT"
				+ ",ORDER_STATUS TEXT,CONTACT_NUMBER TEXT,ORDER_INFO TEXT)");
		
	}

	private void frameInitialize() {
		setResizable(false);
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("User Type");
		setLayout(new GridLayout(4, 1, 10, 10));
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == customerLoginButton) {
			// JOptionPane.showMessageDialog(this, "Done");
			onLoginButtonClick(0);
		}
		if (e.getSource() == shopkeeperLoginButton) {
			
			// JOptionPane.showMessageDialog(this, "Done");
			onLoginButtonClick(1);
		}
		if (e.getSource() == customerRegisterButton) {
			// JOptionPane.showMessageDialog(this, "Done");
			onRegisterButtonClick(0);
		}
		if (e.getSource() == shopkeeperRegisterButton) {
			// JOptionPane.showMessageDialog(this, "Done");
			onRegisterButtonClick(1);
		}
	}

	private void onRegisterButtonClick(int flag) {
		setVisible(false);
		new UserRegister(flag,false,"");
	}

	public void onLoginButtonClick(int flag) {
		setVisible(false);
		new UserLogin(flag);
	}
}

class UserLogin extends JFrame implements ActionListener {
	JTextField usernameField;
	JPasswordField passwordField;
	JButton loginButton;
	JButton backButton,forgotPasswordButton,showPasswordButton;
	JLabel helperText;
	JPanel panel;
	int flag = 0;
	DatabaseHandler database;
	int show =0;
	UserLogin(int flag) {
		database = new DatabaseHandler();
		this.flag = flag;
		panel = new JPanel();
		helperText = new JLabel("");
		forgotPasswordButton=new JButton("Forgot password?");
		helperText.setForeground(Color.RED);
		usernameField = new JTextField("Enter username");
		passwordField = new JPasswordField("Enter password");
		backButton = new JButton("Back");
		passwordField.setEchoChar((char) 0);
		loginButton = new JButton("Login");
		showPasswordButton = new JButton();

		usernameField.setBounds(100, 100, 250, 40);
		passwordField.setBounds(100, 150, 250, 40);
		showPasswordButton.setBounds(350, 150, 40, 40);
		loginButton.setBounds(100, 230, 250, 40);
		backButton.setBounds(100, 280, 250, 40);
		forgotPasswordButton.setBounds(80, 190, 150, 40);
		helperText.setBounds(100, 350, 250, 100);

		forgotPasswordButton.setBorderPainted(false);
		forgotPasswordButton.setContentAreaFilled(false);
		forgotPasswordButton.setForeground(Color.BLUE);
		
		loginButton.addActionListener(this);
		backButton.addActionListener(this);
		forgotPasswordButton.addActionListener(this);
		showPasswordButton.addActionListener(this);

		usernameField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (usernameField.getText().equals("Enter username")) {
					usernameField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (usernameField.getText().isEmpty()) {
					usernameField.setText("Enter username");
				}
			}

		});
		passwordField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (passwordField.getText().equals("Enter password")) {
					passwordField.setEchoChar('*');
					passwordField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getText().isEmpty()) {
					passwordField.setEchoChar((char) 0);
					passwordField.setText("Enter password");
				}
			}

		});
		try {
			BufferedImage backIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			Image scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage showIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"showPasswordIcon.png"));
			scaledIcon=showIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			showPasswordButton.setIcon(new ImageIcon(scaledIcon));
			showPasswordButton.setBorderPainted(false);
			showPasswordButton.setContentAreaFilled(false);
			backButton.setBorderPainted(false);
			backButton.setContentAreaFilled(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}

		add(usernameField);
		add(passwordField);
		add(loginButton);
		add(backButton);
		add(helperText);
		add(forgotPasswordButton);
		add(showPasswordButton);
		// panel.setBounds(100,100,300,300);
		// panel.setLayout(new GridLayout(3, 1, 10, 10));
		add(panel);
		frameInitialize();
	}

	private void frameInitialize() {
		setResizable(false);
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Login Page");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			if (flag == 0) {
				verifyCustomer();
			} else if (flag == 1) {
				verifyShopkeeper();
			}
		}
		if (e.getSource() == backButton) {
			onBack();
		}
		if (e.getSource() == forgotPasswordButton) {
			String username = usernameField.getText();
			ArrayList usernameList = new ArrayList();
			if(flag==0) {
				usernameList = database.getUsernameList("CUSTOMER");
			}
			else if(flag==1) {
				usernameList = database.getUsernameList("SHOPKEEPER");
			}
			if (username.isEmpty() || username.equals("Enter username")) {
				helperText.setText("Username required !");
			} 
			else if (usernameList.contains(username)) {
				setVisible(false);
				new UserRegister(flag,true,username);
			}
			else {
				helperText.setText("Username not found register first !");
			}	
		}
		if (e.getSource() == showPasswordButton) {
			if(show==0&&!passwordField.getText().equals("Enter password")) {
				show=1;
				passwordField.setEchoChar((char)0);
				try {
					
					BufferedImage showIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"hidePasswordIcon.png"));
					Image scaledIcon=showIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					showPasswordButton.setIcon(new ImageIcon(scaledIcon));
					
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex);
				}
			}
			else if(show==1&&!passwordField.getText().equals("Enter password")) {
				show=0;
				passwordField.setEchoChar('*');
				try {
					
					BufferedImage showIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"showPasswordIcon.png"));
					Image scaledIcon=showIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					showPasswordButton.setIcon(new ImageIcon(scaledIcon));
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex);
				}
			}
		}
		
	}

	private void onBack() {
		helperText.setText("");
		setVisible(false);
		new LoginPage();
	}

	private void verifyShopkeeper() {
		
		helperText.setText("");
		String username = usernameField.getText();
		String password = passwordField.getText();
		ArrayList usernameList = new ArrayList();
		usernameList = database.getUsernameList("SHOPKEEPER");
		ArrayList shopUsernameList = new ArrayList();
		shopUsernameList = database.getUsernameList("SHOPS");
		if (username.isEmpty() || username.equals("Enter username") || password.isEmpty()
				|| password.equals("Enter password")) {
			helperText.setText("All details required !");
		} else if (usernameList.contains(username)) {
			if (password.equals(database.getPassword("SHOPKEEPER", username))) {
				JOptionPane.showMessageDialog(this, "Successfully logged in");
				setVisible(false);
				helperText.setText("");

				if (shopUsernameList.contains(username)) {
					new ShopkeeperPage(username);
				} else {
					new NoShops(username);
				}
			} else {
				helperText.setText("Incorrect password !");
			}
		} else {
			helperText.setText("Username not found register first !");
		}

	}

	private void verifyCustomer() {
		helperText.setText("");
		String username = usernameField.getText();
		String password = passwordField.getText();
		ArrayList usernameList = new ArrayList();
		usernameList = database.getUsernameList("CUSTOMER");
		if (username.isEmpty() || username.equals("Enter username") || password.isEmpty()
				|| password.equals("Enter password")) {
			helperText.setText("All details required !");
		} else if (usernameList.contains(username)) {
			if (password.equals(database.getPassword("CUSTOMER", username))) {
				JOptionPane.showMessageDialog(this, "Successfully logged in");
				setVisible(false);
				new CustomerPage(username,null,0,0);
			} else {
				helperText.setText("Incorrect password !");
			}
		} else {
			helperText.setText("Username not found register first !");
		}
	}
}

class UserRegister extends JFrame implements ActionListener {
	JTextField nameField, usernameField, emailField;
	JPasswordField passwordField;
	JButton registerButton;
	JButton backButton,showPasswordButton;
	JLabel helperText;
	JPanel panel;
	int flag = 0;
	boolean editable;
	String username;
	DatabaseHandler database;
	int show=0;
	UserRegister(int flag,boolean editable,String username) {
		database = new DatabaseHandler();
		this.editable=editable;
		this.username=username;
		// database.createTable("CREATE TABLE SHOPKEEPER(NAME TEXT,USERNAME
		// TEXT,PASSWORD TEXT,EMAIL TEXT)");
		// database.createTable("CREATE TABLE CUSTOMER(NAME TEXT,USERNAME TEXT,PASSWORD
		// TEXT,EMAIL TEXT)");
		this.flag = flag;
		panel = new JPanel();
		helperText = new JLabel("");
		helperText.setForeground(Color.RED);
		nameField = new JTextField("Enter name");
		usernameField = new JTextField("Enter username");
		passwordField = new JPasswordField("Enter password");
		emailField = new JTextField("Enter email");
		backButton = new JButton("Back");
		passwordField.setEchoChar((char) 0);
		registerButton = new JButton("Register");
		showPasswordButton = new JButton();
		nameField.setBounds(100, 50, 250, 40);
		usernameField.setBounds(100, 100, 250, 40);
		passwordField.setBounds(100, 150, 250, 40);
		showPasswordButton.setBounds(350, 150, 40, 40);
		emailField.setBounds(100, 200, 250, 40);
		registerButton.setBounds(100, 250, 250, 40);
		backButton.setBounds(100, 300, 250, 40);
		helperText.setBounds(100, 350, 250, 100);

		registerButton.addActionListener(this);
		backButton.addActionListener(this);
		showPasswordButton.addActionListener(this);

		nameField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (nameField.getText().equals("Enter name")) {
					nameField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (nameField.getText().isEmpty()) {
					nameField.setText("Enter name");
				}

			}

		});

		usernameField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (usernameField.getText().equals("Enter username")) {
					usernameField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (usernameField.getText().isEmpty()) {
					usernameField.setText("Enter username");
				}
			}

		});
		passwordField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (passwordField.getText().equals("Enter password")) {
					passwordField.setEchoChar('*');
					passwordField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getText().isEmpty()) {
					passwordField.setEchoChar((char) 0);
					passwordField.setText("Enter password");
				}
			}

		});
		emailField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (emailField.getText().equals("Enter email")) {
					emailField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (emailField.getText().isEmpty()) {
					emailField.setText("Enter email");
				}
			}

		});
		try {
			BufferedImage backIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			Image scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage registerIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"registerIcon.png"));
			scaledIcon=registerIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			registerButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage showIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"showPasswordIcon.png"));
			scaledIcon=showIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			showPasswordButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBorderPainted(false);
			backButton.setContentAreaFilled(false);
			showPasswordButton.setBorderPainted(false);
			showPasswordButton.setContentAreaFilled(false);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}

		add(nameField);
		add(usernameField);
		add(passwordField);
		add(emailField);
		add(registerButton);
		add(backButton);
		add(helperText);
		add(showPasswordButton);
		// panel.setBounds(100,100,300,300);
		// panel.setLayout(new GridLayout(3, 1, 10, 10));
		add(panel);
		if(editable) {
			setTitle("Edit User");
			usernameField.setText(username);
			usernameField.setEditable(false);
			usernameField.setEnabled(false);
			registerButton.setText("Update");
			if(flag==0) {
				nameField.setText(database.getName("CUSTOMER", username));
				emailField.setText(database.getEmail("CUSTOMER", username));
				passwordField.setText(database.getPassword("CUSTOMER", username));
			}
			else if(flag==1) {
				nameField.setText(database.getName("SHOPKEEPER", username));
				emailField.setText(database.getEmail("SHOPKEEPER", username));
				passwordField.setText(database.getPassword("SHOPKEEPER", username));
			}
		}
		frameInitialize();
	}

	private void frameInitialize() {
		setResizable(false);
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Register Page");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == registerButton) {
			if (flag == 0) {
				addCustomer();
			} else if (flag == 1) {
				addShopkeeper();
			}
		}
		if (e.getSource() == backButton) {
			onBack();
		}
		if (e.getSource() == showPasswordButton) {
			if(show==0&&!passwordField.getText().equals("Enter password")) {
				show=1;
				passwordField.setEchoChar((char)0);
				try {
					
					BufferedImage showIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"hidePasswordIcon.png"));
					Image scaledIcon=showIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					showPasswordButton.setIcon(new ImageIcon(scaledIcon));
					
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex);
				}
			}
			else if(show==1&&!passwordField.getText().equals("Enter password")) {
				show=0;
				passwordField.setEchoChar('*');
				try {
					
					BufferedImage showIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"showPasswordIcon.png"));
					Image scaledIcon=showIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					showPasswordButton.setIcon(new ImageIcon(scaledIcon));
					
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex);
				}
			}
		}
	}

	private void onBack() {
		if(flag==0||editable) {
			helperText.setText("");
			setVisible(false);
			new LoginPage();
		}
		else if(flag==1) {
			helperText.setText("");
			setVisible(false);
			new ShopkeeperPage(username);
		}
	}

	private void addShopkeeper() {
		helperText.setText("");
		String name = nameField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();
		String email = emailField.getText();
		ArrayList usernameList = new ArrayList();
		usernameList = database.getUsernameList("SHOPKEEPER");
		if (name.isEmpty() || name.equals("Enter name") || username.isEmpty() || username.equals("Enter username")
				|| password.isEmpty() || password.equals("Enter password") || email.isEmpty()
				|| email.equals("Enter email")) {
			helperText.setText("All details required");
		} else if (usernameList.contains(username)&&!editable) {
			helperText.setText("Username already exists try another");
		} else {
			if(email.contains("@")&&(email.endsWith(".com")||email.endsWith(".in"))) {
				if(editable) {
					database.updateTable("SHOPKEEPER", name, username, password, email);
					JOptionPane.showMessageDialog(this, "Successfully Updated");
					setVisible(false);
					new LoginPage();
				}
				else {
					database.add("SHOPKEEPER", name, username, password, email);
					JOptionPane.showMessageDialog(this, "Successfully added");
					setVisible(false);
					new ShopDetails(username,0);
				}
			}
			else {
				helperText.setText("Enter valid email");
			}
		}
	}

	private void addCustomer() {
		helperText.setText("");
		String name = nameField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();
		String email = emailField.getText();
		ArrayList usernameList = new ArrayList();
		usernameList = database.getUsernameList("CUSTOMER");
		if (name.isEmpty() || name.equals("Enter name") || username.isEmpty() || username.equals("Enter username")
				|| password.isEmpty() || password.equals("Enter password") || email.isEmpty()
				|| email.equals("Enter email")) {
			helperText.setText("All details required");
		} else if (usernameList.contains(username)&&!editable) {
			helperText.setText("Username already exists try another");
		} else {
			if(email.contains("@")&&(email.endsWith(".com")||email.endsWith(".in"))) {
				if(editable) {
					database.updateTable("CUSTOMER", name, username, password, email);
					JOptionPane.showMessageDialog(this, "Successfully Updated");
					setVisible(false);
					new LoginPage();
				}
				else {
					database.add("CUSTOMER", name, username, password, email);
					JOptionPane.showMessageDialog(this, "Successfully added");
					setVisible(false);
					new ShopDetails(username,0);
				}
			}
			else {
				helperText.setText("Enter valid email");
			}
		}
	}
}

class ShopDetails extends JFrame implements ActionListener {

	JTextField shopNameField, shopTypeField, shopCityField;
	JButton addShopButton;
	DatabaseHandler database;
	String username;
	JLabel helperText;
	int flag;
	ShopDetails(String username,int flag) {
		this.flag=flag;
		this.username = username;
		helperText = new JLabel();
		helperText.setForeground(Color.RED);
		database = new DatabaseHandler();
		shopNameField = new JTextField("Enter shop name");
		shopTypeField = new JTextField("Enter shop type");
		shopCityField = new JTextField("Enter shop city");
		addShopButton = new JButton("Add");

		addShopButton.addActionListener(this);
		shopNameField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (shopNameField.getText().equals("Enter shop name")) {
					shopNameField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (shopNameField.getText().isEmpty()) {
					shopNameField.setText("Enter shop name");
				}

			}

		});
		shopTypeField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (shopTypeField.getText().equals("Enter shop type")) {
					shopTypeField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (shopTypeField.getText().isEmpty()) {
					shopTypeField.setText("Enter shop type");
				}

			}

		});
		shopCityField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (shopCityField.getText().equals("Enter shop city")) {
					shopCityField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (shopCityField.getText().isEmpty()) {
					shopCityField.setText("Enter shop city");
				}
			}

		});

		shopNameField.setBounds(100, 100, 250, 40);
		shopTypeField.setBounds(100, 150, 250, 40);
		shopCityField.setBounds(100, 200, 250, 40);
		addShopButton.setBounds(100, 250, 250, 40);
		helperText.setBounds(100, 300, 250, 40);
		
		try {
			BufferedImage shopIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"addShopIcon.jpg"));
			Image scaledIcon=shopIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			addShopButton.setIcon(new ImageIcon(scaledIcon));
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}

		add(shopNameField);
		add(shopTypeField);
		add(shopCityField);
		add(addShopButton);
		add(helperText);
		if(flag==1) {
			setTitle("Edit Shop details");
			addShopButton.setText("Update");
			shopNameField.setText(database.getShopDetails("SHOPNAME", username));
			shopTypeField.setText(database.getShopDetails("SHOPTYPE", username));
			shopCityField.setText(database.getShopDetails("SHOPCITY", username));
		}
		frameInitialize();
	}

	private void frameInitialize() {
		setResizable(false);
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Shopkeeper Page");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addShopButton) {
			addNewShop();
		}
	}

	private void addNewShop() {
		helperText.setText("");
		String shopName = shopNameField.getText();
		String shopType = shopTypeField.getText();
		String shopCity = shopCityField.getText();
		if (shopName.isEmpty() || shopName.equals("Enter shop name") || shopType.isEmpty()
				|| shopType.equals("Enter shop type") || shopCity.isEmpty() || shopCity.equals("Enter shop city")) {
			helperText.setText("All details required");
		} else {
			if(flag==0) {
				database.addShop(username, shopName, shopType, shopCity);
				JOptionPane.showMessageDialog(this, "Successfully added");
				setVisible(false);
				helperText.setText("");
				new LoginPage();
			}
			else if(flag==1) {
				database.updateShop(username, shopName, shopType, shopCity);
				JOptionPane.showMessageDialog(this, "Successfully updated");
				setVisible(false);
				helperText.setText("");
				new ShopkeeperPage(username);
			}
		}
	}
}

class ShopkeeperPage extends JFrame implements ActionListener {

	String username;
	JLabel titleLabel, nameLabel;
	DatabaseHandler database;
	JComboBox productListBox,categoryListBox;
	JTextField categoryField;
	JButton infoButton, addProductsButton, editButton, ordersButton, deleteAccountButton,editUserButton,editShopButton,deleteButton;
	JButton addCategoryButton,deleteCategoryButton,showCategoryProductsButton,editCategoryButton,statsButton;

	ShopkeeperPage(String username) {

		database = new DatabaseHandler();
		ArrayList productList = new ArrayList();
		productList.add("PRODUCTS");
		productList.addAll( database.getProducts(username));
		ArrayList categoryList = new ArrayList();
		categoryList.add("CATEGORIES");
		categoryList.add("All");
		categoryList.addAll( database.getCategories(username));
		categoryList=removeDuplicates(categoryList);
		this.username = username;
		infoButton = new JButton("Info");
		statsButton = new JButton("Stats");
		showCategoryProductsButton = new JButton("Show Products");
		addCategoryButton=new JButton("Add Category");
		deleteCategoryButton=new JButton("Delete Category");
		editUserButton = new JButton("Edit User");
		editShopButton = new JButton("Edit Shop");
		editCategoryButton = new JButton("Edit Category");
		deleteAccountButton = new JButton("Delete Account");
		addProductsButton = new JButton("Add Products");
		editButton = new JButton("Edit");
		deleteButton = new JButton("Delete");
		ordersButton = new JButton("Orders");
		productListBox = new JComboBox(productList.toArray());
		categoryListBox = new JComboBox(categoryList.toArray());
		titleLabel = new JLabel(database.getShopDetails("SHOPNAME", username).toUpperCase());
		titleLabel.setBounds(100, 50, 400, 40);
		nameLabel = new JLabel("Welcome Mr." + database.getName("SHOPKEEPER", username).toUpperCase());
		nameLabel.setBounds(100, 100, 400, 40);
		productListBox.setBounds(100, 150, 200, 40);
		categoryListBox.setBounds(310, 150, 200, 40);
		infoButton.setBounds(100, 200, 200, 40);
		showCategoryProductsButton.setBounds(310, 200, 200, 40);
		editButton.setBounds(100, 250, 200, 40);
		addCategoryButton.setBounds(310, 350, 200, 40);
		deleteButton.setBounds(100, 300, 200, 40);
		deleteCategoryButton.setBounds(310, 300, 200, 40);
		addProductsButton.setBounds(100, 350, 200, 40);
		editCategoryButton.setBounds(310, 250, 200, 40);
		ordersButton.setBounds(0, 0, 120, 40);
		deleteAccountButton.setBounds(120, 0, 180, 40);
		editUserButton.setBounds(300, 0, 150, 40);
		editShopButton.setBounds(450, 0, 150, 40);
		statsButton.setBounds(600, 0, 150, 40);
		
		try {
			BufferedImage logoutIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"deleteIcon1.png"));
			Image scaledIcon=logoutIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			deleteAccountButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage editIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"editIcon1.png"));
			scaledIcon=editIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			editButton.setIcon(new ImageIcon(scaledIcon));
			editUserButton.setIcon(new ImageIcon(scaledIcon));
			editShopButton.setIcon(new ImageIcon(scaledIcon));
			editCategoryButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage addIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"addIcon.png"));
			scaledIcon=addIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			addProductsButton.setIcon(new ImageIcon(scaledIcon));
			addCategoryButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage deleteIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"deleteIcon1.png"));
			scaledIcon=deleteIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			deleteButton.setIcon(new ImageIcon(scaledIcon));
			deleteCategoryButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage infoIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"infoIcon.png"));
			scaledIcon=infoIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			infoButton.setIcon(new ImageIcon(scaledIcon));
			showCategoryProductsButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage ordersIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"ordersIcon.jpg"));
			scaledIcon=ordersIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ordersButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage statsIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"statsIcon.png"));
			scaledIcon=statsIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			statsButton.setIcon(new ImageIcon(scaledIcon));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}

		infoButton.addActionListener(this);
		addProductsButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		ordersButton.addActionListener(this);
		deleteAccountButton.addActionListener(this);
		editUserButton.addActionListener(this);
		editShopButton.addActionListener(this);
		addCategoryButton.addActionListener(this);
		deleteCategoryButton.addActionListener(this);
		showCategoryProductsButton.addActionListener(this);
		editCategoryButton.addActionListener(this);
		statsButton.addActionListener(this);

		add(titleLabel);
		add(nameLabel);
		add(productListBox);
		add(categoryListBox);
		add(infoButton);
		add(addProductsButton);
		add(showCategoryProductsButton);
		add(editButton);
		add(deleteButton);
		add(ordersButton);
		add(deleteAccountButton);
		add(editUserButton);
		add(editShopButton);
		add(addCategoryButton);
		add(deleteCategoryButton);
		add(editCategoryButton);
		add(statsButton);
		
		frameInitialize();
	}
	
	private ArrayList removeDuplicates(ArrayList categoryList) {
		for(int i=0;i<categoryList.size();i++) {
			for(int j=i+1;j<categoryList.size();j++) {
				if(String.valueOf(categoryList.get(i)).equalsIgnoreCase(String.valueOf(categoryList.get(j)))) {
					categoryList.remove(j);
				}
			}
		}
		return categoryList;
	}

	private void frameInitialize() {
		setResizable(false);
		setSize(770, 500);
		setLocation(400, 100);
		setTitle("Shopkeeper Page");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == infoButton) {
			// JOptionPane.showMessageDialog(this, "updating...");
			String data = "";
			String productName = (String) productListBox.getSelectedItem();
			if (productName.isEmpty()||productName.equalsIgnoreCase("PRODUCTS")) {
				JOptionPane.showMessageDialog(this, "Select some product");
			} else {
				data += "Product name : " + database.getProductDetails("PRODUCTNAME", username, productName);
				data += "\nProduct price : " + database.getProductDetails("PRODUCTPRICE", username, productName);
				data += "\nProduct availability : "
						+ database.getProductDetails("PRODUCTAVAILABILITY", username, productName);
				data += "\nProduct description : "
						+ database.getProductDetails("PRODUCTDESCRIPTION", username, productName);
				JOptionPane.showMessageDialog(this, data);
			}

		}
		if (e.getSource() == addProductsButton) {
			setVisible(false);
			new AddProducts(username, false, "");
		}
		if (e.getSource() == editButton) {
			String productName = (String) productListBox.getSelectedItem();
			if (productName.isEmpty()||productName.equalsIgnoreCase("PRODUCTS")) {
				JOptionPane.showMessageDialog(this, "Select some product");
			} else {
				setVisible(false);
				new AddProducts(username, true, productName);
			}

		}
		if (e.getSource() == deleteButton) {
			String productName = (String) productListBox.getSelectedItem();
			if (productName.isEmpty()||productName.equalsIgnoreCase("PRODUCTS")) {
				JOptionPane.showMessageDialog(this, "Select some product");
			} else {
				database.deleteProduct(username, productName);
				JOptionPane.showMessageDialog(this, "Deleted");
				setVisible(false);
				new ShopkeeperPage(username);
			}

		}
		if (e.getSource() == deleteAccountButton) {

			int result = JOptionPane.showConfirmDialog(this, "Are you sure want to delete your Account?");
			if (result == JOptionPane.YES_OPTION) {
				setVisible(false);
				database.logoutShopkeeper(username);
				new LoginPage();
			}
		}
		if (e.getSource() == ordersButton) {
			setVisible(false);
			new OrdersPage(username);
		}
		if (e.getSource() == statsButton) {
			//JOptionPane.showMessageDialog(this, "Waiting for deliveries...");
			new ShopkeeperStatsPage(username);
			setVisible(false);
		}
		if (e.getSource() == editUserButton) {
			setVisible(false);
			new UserRegister(1,true,username);
		}
		if(e.getSource()==editShopButton) {
			setVisible(false);
			new ShopDetails(username,1);
		}
		if(e.getSource()==addCategoryButton) {
			setVisible(false);
			new AddCategory(username,"",false);
		}
		if(e.getSource()==deleteCategoryButton) {
			String categoryName = (String) categoryListBox.getSelectedItem();
			if (categoryName.isEmpty()||categoryName.equalsIgnoreCase("CATEGORIES")) {
				JOptionPane.showMessageDialog(this, "Select some category");
			} else {
				database.deleteCategory(username, categoryName);
				JOptionPane.showMessageDialog(this, "Deleted");
				setVisible(false);
				new ShopkeeperPage(username);
			}
		}
		if(e.getSource()==showCategoryProductsButton) {
			String categoryName = (String) categoryListBox.getSelectedItem();
			if (categoryName.isEmpty()||categoryName.equalsIgnoreCase("CATEGORIES")) {
				JOptionPane.showMessageDialog(this, "Select some category");
			}
			else if(categoryName.equalsIgnoreCase("All")) {
				String data="PRODUCTS :\n"; 
				ArrayList categoryProductsList=new ArrayList();
				categoryProductsList=database.getProducts(username);
				for(Object products:categoryProductsList) {
					data+=(String)products+",\n";
				}
				JOptionPane.showMessageDialog(this,data);
				remove(productListBox);
				ArrayList productList=new ArrayList();
				productList.add("PRODUCTS");
				productList.addAll(database.getProducts(username));
				productListBox=new JComboBox(productList.toArray());
				productListBox.setBounds(100, 150, 200, 40);
				add(productListBox);
			}
			else {
				String data="PRODUCTS :\n";
				ArrayList categoryProductsList=new ArrayList();
				categoryProductsList=database.getCategoryProducts(username, categoryName);
				for(Object products:categoryProductsList) {
					data+=(String)products+",\n";
				}
				JOptionPane.showMessageDialog(this,data);
				remove(productListBox);
				ArrayList productList=new ArrayList();
				productList.add("PRODUCTS");
				productList.addAll(database.getCategoryProducts(username, categoryName));
				productListBox=new JComboBox(productList.toArray());
				productListBox.setBounds(100, 150, 200, 40);
				add(productListBox);
			}
		}
		if(e.getSource()==editCategoryButton) {
			String categoryName = (String) categoryListBox.getSelectedItem();
			if (categoryName.isEmpty()||categoryName.equalsIgnoreCase("CATEGORIES")) {
				JOptionPane.showMessageDialog(this, "Select some category");
			} else {
				setVisible(false);
				new AddCategory(username,categoryName,true);
			}
			
		}
	}
}

class NoShops extends JFrame implements ActionListener {

	JLabel titleLabel;
	JButton addShopButton, backButton;
	String username;

	NoShops(String username) {
		this.username = username;
		addShopButton = new JButton("Add shop");
		backButton = new JButton("Back");
		titleLabel = new JLabel("No shop found !");
		titleLabel.setBounds(100, 100, 200, 40);
		addShopButton.setBounds(100, 150, 150, 40);
		backButton.setBounds(100, 200, 100, 40);
		addShopButton.addActionListener(this);
		backButton.addActionListener(this);
		try {
			BufferedImage addIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"addIcon.png"));
			Image scaledIcon=addIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			addShopButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage backIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBorderPainted(false);
			backButton.setContentAreaFilled(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}

		add(titleLabel);
		add(addShopButton);
		add(backButton);
		frameInitialize();
	}

	private void frameInitialize() {
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("No shop found");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addShopButton) {
			setVisible(false);
			new ShopDetails(username,0);
		}
		if (e.getSource() == backButton) {
			setVisible(false);
			new LoginPage();
		}
	}

}

class AddProducts extends JFrame implements ActionListener {
	String username;
	JTextField productNameField, productPriceField, productAvailabilityField, productDescriptionField;
	JLabel productNameLabel, productPriceLabel, productAvailabilityLabel, productDescriptionLabel, helperText;
	JButton addButton, backButton;
	DatabaseHandler database;
	String passedProductName;
	boolean editable;

	AddProducts(String username, boolean editable, String passedProductName) {
		this.passedProductName = passedProductName;
		this.editable = editable;
		database = new DatabaseHandler();
		productNameField = new JTextField();
		productPriceField = new JTextField();
		productAvailabilityField = new JTextField();
		productDescriptionField = new JTextField();
		productNameLabel = new JLabel("Product name :");
		productPriceLabel = new JLabel("Product price :");
		productAvailabilityLabel = new JLabel("Product availability :");
		productDescriptionLabel = new JLabel("Product description :");
		addButton = new JButton("Add");
		backButton = new JButton("Back");
		helperText = new JLabel();
		helperText.setForeground(Color.RED);

		addButton.addActionListener(this);
		backButton.addActionListener(this);

		productNameField.setBounds(150, 100, 250, 40);
		productPriceField.setBounds(150, 150, 250, 40);
		productAvailabilityField.setBounds(150, 200, 250, 40);
		productDescriptionField.setBounds(150, 250, 250, 40);
		productNameLabel.setBounds(10, 100, 150, 40);
		productPriceLabel.setBounds(10, 150, 150, 40);
		productAvailabilityLabel.setBounds(10, 200, 150, 40);
		productDescriptionLabel.setBounds(10, 250, 150, 40);
		addButton.setBounds(100, 300, 150, 40);
		backButton.setBounds(100, 350, 150, 40);
		helperText.setBounds(50, 400, 250, 40);

		try {
			BufferedImage addIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"addIcon.png"));
			Image scaledIcon=addIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			addButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage backIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBorderPainted(false);
			backButton.setContentAreaFilled(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
		
		add(productNameField);
		add(productPriceField);
		add(productAvailabilityField);
		add(productDescriptionField);
		add(productNameLabel);
		add(productPriceLabel);
		add(productAvailabilityLabel);
		add(productDescriptionLabel);
		add(helperText);
		add(addButton);
		add(backButton);
		this.username = username;
		if (editable) {
			addButton.setText("Update");
			productNameField.setText(passedProductName);
			productNameField.setEditable(false);
			productNameField.setEnabled(false);
			productPriceField.setText(database.getProductDetails("PRODUCTPRICE", username, passedProductName));
			productAvailabilityField.setText(database.getProductDetails("PRODUCTAVAILABILITY", username, passedProductName));
			productDescriptionField.setText(database.getProductDetails("PRODUCTDESCRIPTION", username, passedProductName));
		}
		frameInitialize();
	}

	private void frameInitialize() {
		setResizable(false);
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Product details");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			onAddProduct();
		}
		if (e.getSource() == backButton) {
			onBack();
		}
	}

	private void onAddProduct() {
		helperText.setText("");

		String productName = productNameField.getText();
		String productPrice = productPriceField.getText();
		String productAvail = productAvailabilityField.getText();
		String productDescription = productDescriptionField.getText();

		ArrayList productList = database.getProducts(username);
		if (productName.isEmpty() || productPrice.isEmpty() || productAvail.isEmpty() || productDescription.isEmpty()) {
			helperText.setText("All details required !");
		} else if (productList.contains(productName) && !editable) {
			helperText.setText("Product name already exists");
		} else {
			if (editable) {
				if(isValidPrice(productPrice)&&isValidQuantity(productAvail)) {
					database.updateProduct(username, productName, productPrice, productAvail, productDescription);
					setVisible(false);
					helperText.setText("");
					JOptionPane.showMessageDialog(this, "Successfully updated");
					new ShopkeeperPage(username);
				}
				else {
					JOptionPane.showMessageDialog(this, "Invalid price or availability\nPrice must be in form of rate/unit"
							+ "\nEg.100/kg,2000/engine,100/l and\n"
							+ "Availability must be in form of quantity with unit \n"
							+ "Eg.100kg,2000engines,100l");
				}
				
			} else {
				if(isValidPrice(productPrice)&&isValidQuantity(productAvail)) {
					database.addProduct(username, productName, productPrice, productAvail, productDescription);
					setVisible(false);
					helperText.setText("");
					JOptionPane.showMessageDialog(this, "Successfully added");
					new ShopkeeperPage(username);
				}
				else {
					JOptionPane.showMessageDialog(this, "Invalid price or availability\nPrice must be in form of rate/unit"
							+ "\nEg.100/kg,2000/engine,100/l and\n"
							+ "Availability must be in form of quantity with unit \n"
							+ "Eg.100kg,2000engines,100l");
				}
				
			}
		}

	}
	private boolean isValidPrice(String price) {
		Pattern p=Pattern.compile("[0-9]+/[a-z]+");
		return p.matcher(price).matches();
	}
	private boolean isValidQuantity(String quantity) {
		Pattern p=Pattern.compile("[0-9]+[a-z]+");
		return p.matcher(quantity).matches();
	}

	private void onBack() {
		helperText.setText("");
		setVisible(false);
		new ShopkeeperPage(username);
	}
}

class DatabaseHandler {

	Connection connection;
	Statement statement;
	

	public int createTable(String query) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			statement.executeUpdate(query);
			connection.close();
			return 0;
		} catch (SQLException e) {
			return 1;
		}
	}

	public void add(String tableName, String name, String username, String password, String email) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO " + tableName + " VALUES('" + name + "','" + username + "','" + password + "','"
					+ email + "')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addShop(String username, String shopName, String shopType, String shopCity) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO SHOPS VALUES('" + username + "','" + shopName + "','" + shopType + "','"
					+ shopCity + "')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCategory(String username, String category,String productName) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO CATEGORIES VALUES('" + username + "','" + category + "','" + productName + "')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addProduct(String username, String productName, String productPrice, String productAvail,
			String productDesc) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO PRODUCTS VALUES('" + username + "','" + productName + "','" + productPrice
					+ "','" + productAvail + "','" + productDesc + "')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTable(String tableName, String name, String username, String password, String email) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "UPDATE "+tableName+" SET NAME='" + name + "'," + "PASSWORD='"
					+ password + "',EMAIL='" + email + "'" + "WHERE USERNAME='" + username+ "'";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateShop(String username, String shopName, String shopType, String shopCity) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "UPDATE SHOPS SET SHOPNAME='" + shopName + "'," + "SHOPTYPE='"
					+ shopType + "',SHOPCITY='" + shopCity + "'" + "WHERE USERNAME='" + username+ "'";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateProduct(String username, String productName, String productPrice, String productAvail,
			String productDesc) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "UPDATE PRODUCTS SET PRODUCTPRICE='" + productPrice + "'," + "PRODUCTAVAILABILITY='"
					+ productAvail + "',PRODUCTDESCRIPTION='" + productDesc + "'" + "WHERE USERNAME='" + username
					+ "' AND PRODUCTNAME='" + productName + "'";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList getUsernameList(String tableName) {
		ArrayList list = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM " + tableName;
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				list.add(rs.getObject("USERNAME"));
			}
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return list;
	}

	public String getPassword(String tableName, String username) {
		String password = "";
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM " + tableName;
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					password = rs.getObject("PASSWORD").toString();
					break;
				}
			}
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return password;
	}

	public String getName(String tableName, String username) {
		String name = "";
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM " + tableName;
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					name = rs.getObject("NAME").toString();
					break;
				}
			}
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return name;
	}

	public String getEmail(String tableName, String username) {
		String email = "";
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM " + tableName;
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					email = rs.getObject("EMAIL").toString();
					break;
				}
			}
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return email;
	}

	public String getShopDetails(String columnName, String username) {
		String data = "";
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					data = rs.getObject(columnName).toString();
					break;
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public String getCustomerDetails(String columnName, String username) {
		String data = "";
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM CUSTOMER";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					data = rs.getObject(columnName).toString();
					break;
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	public ArrayList<ShopInfo> getShops() {
		ArrayList<ShopInfo> data = new ArrayList<ShopInfo>();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				ShopInfo shopInfo=new ShopInfo(rs.getString("USERNAME"),rs.getString("SHOPNAME"),rs.getString("SHOPTYPE"),rs.getString("SHOPCITY"));
				data.add(shopInfo);
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	public ArrayList<ShopInfo> getShopsWithType(String shopType) {
		ArrayList<ShopInfo> data = new ArrayList<ShopInfo>();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if(rs.getString("SHOPTYPE").equalsIgnoreCase(shopType)) {
					ShopInfo shopInfo=new ShopInfo(rs.getString("USERNAME"),rs.getString("SHOPNAME"),rs.getString("SHOPTYPE"),rs.getString("SHOPCITY"));
					data.add(shopInfo);
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	public ArrayList<ShopInfo> getShopsWithCity(String shopCity) {
		ArrayList<ShopInfo> data = new ArrayList<ShopInfo>();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if(rs.getString("SHOPCITY").equalsIgnoreCase(shopCity)) {
					ShopInfo shopInfo=new ShopInfo(rs.getString("USERNAME"),rs.getString("SHOPNAME"),rs.getString("SHOPTYPE"),rs.getString("SHOPCITY"));
					data.add(shopInfo);
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	public ArrayList<ShopInfo> getShopsWithTypeAndCity(String shopType,String shopCity) {
		ArrayList<ShopInfo> data = new ArrayList<ShopInfo>();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if(rs.getString("SHOPTYPE").equalsIgnoreCase(shopType)&&rs.getString("SHOPCITY").equalsIgnoreCase(shopCity)) {
					ShopInfo shopInfo=new ShopInfo(rs.getString("USERNAME"),rs.getString("SHOPNAME"),rs.getString("SHOPTYPE"),rs.getString("SHOPCITY"));
					data.add(shopInfo);
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList<ShopkeeperInfo> getShopkeepers() {
		ArrayList<ShopkeeperInfo> data = new ArrayList<ShopkeeperInfo>();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPKEEPER";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				ShopkeeperInfo shopkeeperInfo=new ShopkeeperInfo(rs.getString("USERNAME"),rs.getString("NAME"),rs.getString("EMAIL"));
				data.add(shopkeeperInfo);
				
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getShopTypes() {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if(!data.contains(rs.getString("SHOPTYPE"))) {
					data.add(rs.getString("SHOPTYPE"));
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getShopCities() {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM SHOPS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if(!data.contains(rs.getString("SHOPCITY"))) {
					data.add(rs.getString("SHOPCITY"));
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	
	public String getProductDetails(String columnName, String username, String productName) {
		String data = "";
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM PRODUCTS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username) && rs.getObject("PRODUCTNAME").equals(productName)) {
					data = rs.getObject(columnName).toString();
					break;
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	public ArrayList getProducts(String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM PRODUCTS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					data.add(rs.getObject("PRODUCTNAME").toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getCategories(String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM CATEGORIES";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)) {
					data.add(rs.getObject("CATEGORY").toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getCategoryProducts(String username,String categoryName) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM CATEGORIES";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("USERNAME").equals(username)&&rs.getObject("CATEGORY").equals(categoryName)) {
					data.add(rs.getObject("PRODUCTNAME").toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	public void logoutShopkeeper(String username) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "DELETE FROM SHOPKEEPER WHERE USERNAME='" + username + "'";
			statement.executeUpdate(query);
			query = "DELETE FROM SHOPS WHERE USERNAME='" + username + "'";
			statement.executeUpdate(query);
			query = "DELETE FROM PRODUCTS WHERE USERNAME='" + username + "'";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public void deleteProduct(String username,String productName) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "DELETE FROM PRODUCTS WHERE USERNAME='" + username + "' AND PRODUCTNAME='"+productName+"'";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public void deleteCategory(String username,String categoryName) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "DELETE FROM CATEGORIES WHERE USERNAME='" + username + "' AND CATEGORY='"+categoryName+"'";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void addOrderStatus(String customerUsername,String shopkeeperUsername,String orderTime,String orderStatus,String contactNumber,String orderInfo) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO ORDERSTATUS VALUES('"+ customerUsername+"','"+ shopkeeperUsername 
					+ "','" + orderTime +"','"+ orderStatus+"','"+ contactNumber+"','"+ orderInfo+"')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addOrders(String customerUsername,String shopkeeperUsername,String shopName
			,String customerName,String orderInfo,String orderTime,String customerAddress) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO ORDERS VALUES('"+ customerUsername+"','"+ shopkeeperUsername 
					+ "','" + shopName + "','" + customerName + "','"+ orderInfo +"','"+ orderTime +"','"+ customerAddress+"')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addCart(String customerUsername,String shopkeeperUsername,String shopName
			,String customerName,String orderInfo,String orderTime) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "INSERT INTO CART VALUES('"+ customerUsername+"','"+ shopkeeperUsername 
					+ "','" + shopName + "','" + customerName + "','"+ orderInfo +"','"+ orderTime +"')";
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ArrayList getCustomerOrderDetails(String columnName, String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM ORDERS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("CUSTOMER_USERNAME").equals(username)) {
					data.add(rs.getObject(columnName).toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getShopkeeperOrderDetails(String columnName, String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM ORDERS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("SHOPKEEPER_USERNAME").equals(username)) {
					data.add(rs.getObject(columnName).toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getCustomerCartDetails(String columnName, String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM CART";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("CUSTOMER_USERNAME").equals(username)) {
					data.add(rs.getObject(columnName).toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getOrderStatusDetails(String columnName, String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM ORDERSTATUS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("SHOPKEEPER_USERNAME").equals(username)) {
					data.add(rs.getObject(columnName).toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getOrderStatusDetailsForCustomer(String columnName, String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM ORDERSTATUS";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("CUSTOMER_USERNAME").equals(username)) {
					data.add(rs.getObject(columnName).toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public ArrayList getCartDetails(String columnName, String username) {
		ArrayList data = new ArrayList();
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "SELECT * FROM CART";
			ResultSet rs = statement.executeQuery(query);
			connection.setAutoCommit(true);
			while (rs.next()) {
				if (rs.getObject("CUSTOMER_USERNAME").equals(username)) {
					data.add(rs.getObject(columnName).toString());
				}
			}
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	public void deleteOrder(String customerUsername,String shopkeeperUsername,String orderTime) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "DELETE FROM ORDERS WHERE (( CUSTOMER_USERNAME='" + customerUsername 
					+ "' AND SHOPKEEPER_USERNAME='"+shopkeeperUsername+ "') AND ORDERED_TIME='"+orderTime+"')";
			//System.out.println(query);
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public void deleteOrderInCart(String customerUsername,String shopkeeperUsername,String orderTime) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "DELETE FROM CART WHERE (( CUSTOMER_USERNAME='" + customerUsername 
					+ "' AND SHOPKEEPER_USERNAME='"+shopkeeperUsername+ "') AND ORDERED_TIME='"+orderTime+"')";
			//System.out.println(query);
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public void updateOrderStatus(String customerUsername,String shopkeeperUsername,String orderTime,String status,String contactNumber) {
		try {
			connection = DriverManager.getConnection(new ConstantPath().DATABASE_URL);
			statement = connection.createStatement();
			String query = "UPDATE ORDERSTATUS SET ORDER_STATUS='"+ status+"', CONTACT_NUMBER='"+contactNumber+"' WHERE (( CUSTOMER_USERNAME='" + customerUsername 
					+ "' AND SHOPKEEPER_USERNAME='"+shopkeeperUsername+ "') AND ORDERED_TIME='"+orderTime+"')";
			//System.out.println(query);
			statement.executeUpdate(query);
			connection.setAutoCommit(true);
			// System.out.println("Successfull");
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}