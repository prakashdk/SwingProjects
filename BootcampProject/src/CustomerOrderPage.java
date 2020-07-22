import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

class CustomerOrderPage extends JFrame implements ActionListener,ItemListener {
	String customerUsername,shopkeeperUsername,shopName,shopkeeperName,shopkeeperContact;
	JLabel shopNameLabel,ownerNameLabel,ownerContactLabel,noProductsLabel;
	JButton placeOrderButton, backButton,addCartButton,filterButton;
	DatabaseHandler database;
	JCheckBox productBox[];
	ArrayList<ShopkeeperInfo> shopkeeperInfo;
	ArrayList productList;
	JScrollPane productsPane;
	JPanel productsPanel,filtersPanel;
	ArrayList selectedProductIndexList;
	int quantityList[];
	JComboBox categoryBox;
	ArrayList categoryList;

	CustomerOrderPage(String customerUsername,String shopkeeperUsername,int selectedIndex) {
		this.customerUsername=customerUsername;
		this.shopkeeperUsername=shopkeeperUsername;
		database = new DatabaseHandler();
		shopkeeperInfo=new ArrayList<ShopkeeperInfo>();
		productList=new ArrayList();
		selectedProductIndexList=new ArrayList();
		
		categoryList=new ArrayList();
		categoryList.add("Categories");
		categoryList.add("All");
		categoryList.addAll(database.getCategories(shopkeeperUsername));
		categoryList=removeDuplicates(categoryList);
		categoryBox=new JComboBox(categoryList.toArray());
		categoryBox.setSelectedIndex(selectedIndex);
		String categoryString=(String) categoryList.get(selectedIndex);
		
		productsPanel=new JPanel();
		filtersPanel=new JPanel();
		productsPanel.setLayout(new BoxLayout(productsPanel,BoxLayout.Y_AXIS));
		placeOrderButton = new JButton("Place order");
		addCartButton = new JButton("Add to Cart");
		shopkeeperInfo=database.getShopkeepers();
		shopName=database.getShopDetails("SHOPNAME", shopkeeperUsername);
		noProductsLabel=new JLabel();
		noProductsLabel.setForeground(Color.RED);
		for(int i=0;i<shopkeeperInfo.size();i++) {
			if(shopkeeperUsername.equals(shopkeeperInfo.get(i).username)) {
				shopkeeperName=shopkeeperInfo.get(i).shopkeeperName;
				shopkeeperContact=shopkeeperInfo.get(i).shopkeeperContact;
				break;
			}
		}
		if(selectedIndex<=1) {
			productList=database.getProducts(shopkeeperUsername);
		}
		else {
			productList=database.getCategoryProducts(shopkeeperUsername, categoryString);
		}
		productBox=new JCheckBox[productList.size()]; 
		quantityList=new int[productList.size()];
		int f=0;
		if(productList.size()==0) {
			f=1;
			//JOptionPane.showMessageDialog(this, "No");
			noProductsLabel.setText("No Products Available!");
			noProductsLabel.setBounds(100,150,200,40);
			addCartButton.setEnabled(false);
			placeOrderButton.setEnabled(false);
		}
		else {
			noProductsLabel.setText("");
			noProductsLabel.setBounds(0,0,0,0);
			addCartButton.setEnabled(true);
			placeOrderButton.setEnabled(true);
		}
		
		int y=0;
		for(int i=0;i<productList.size();i++) {
			productBox[i]=new JCheckBox((String)productList.get(i));
			productBox[i].addItemListener(this);
			productsPanel.add(productBox[i]);
			y+=30;
		}
		productsPane=new JScrollPane(productsPanel);
		if(f==1) {
			productsPane.setVisible(false);
		}
		else {
			productsPane.setVisible(true);
		}
		shopNameLabel = new JLabel(shopName.toUpperCase());
		ownerNameLabel = new JLabel("Owner name : Mr."+shopkeeperName.toUpperCase());
		ownerContactLabel = new JLabel("Owner contact : "+shopkeeperContact);
		backButton = new JButton("Back");
		
		filterButton=new JButton("Filter");

		placeOrderButton.addActionListener(this);
		backButton.addActionListener(this);
		addCartButton.addActionListener(this);
		filterButton.addActionListener(this);
		
		filtersPanel.add(categoryBox);
		filtersPanel.add(filterButton);

		shopNameLabel.setBounds(100, 0, 150, 40);
		filtersPanel.setBounds(250, 0, 200, 40);
		ownerNameLabel.setBounds(100, 50, 300, 40);
		ownerContactLabel.setBounds(100, 100, 300, 40);
		productsPane.setBounds(100,150,250,150);
		placeOrderButton.setBounds(100, 300, 150, 40);
		addCartButton.setBounds(250, 300, 150, 40);
		backButton.setBounds(100, 350, 150, 40);

		try {
			BufferedImage addIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"addIcon.png"));
			Image scaledIcon=addIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			addCartButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage backIcon=ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBorderPainted(false);
			backButton.setContentAreaFilled(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Icon loading error");
		}
		add(filtersPanel);
		add(productsPane);
		add(noProductsLabel);
		add(shopNameLabel);
		add(ownerNameLabel);
		add(ownerContactLabel);
		add(placeOrderButton);
		add(addCartButton);
		add(backButton);
		frameInitialize();
	}

	private void frameInitialize() {
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Place order");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == placeOrderButton) {
			
			verifyOrder(0);
		}
		if (e.getSource() == addCartButton) {
			verifyOrder(1);
		}
		if (e.getSource() == filterButton) {
			if(categoryBox.getSelectedIndex()==0||categoryBox.getSelectedItem().toString().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Select some Category");
			}
			else {
				setVisible(false);
				new CustomerOrderPage(customerUsername,shopkeeperUsername,categoryBox.getSelectedIndex());
			}
			
		}
		if (e.getSource() == backButton) {
			onBack();
		}
	}
	private void verifyOrder(int flag) {
		String bill="Product    ProductPrice   Quantity    Price\n";
		bill+="----------------------------------------------------\n";
		int total=0;
		int f=0;
		for(int i=0;i<productBox.length;i++) {
			
			if(productBox[i].isSelected()) {
				f=1;
				String product=productBox[i].getText();
				String defaultPrice=database.getProductDetails("PRODUCTPRICE", shopkeeperUsername, product);
				int productPrice=(int)quantityList[i]*getProbablePrice(defaultPrice);
				//int productPrice=0;
				bill+=product+"   "+defaultPrice+"   "+quantityList[i]+"   "+productPrice+"\n";
				total+=productPrice;
			}
			
			
		}
		total+=40;
		bill+="----------------------------------------------------\n";
		bill+="Delivery Charges : Rs.40\n";
		bill+="----------------------------------------------------\n";
		bill+="Total :Rs."+total+"\n";
		bill+="----------------------------------------------------\n";
		bill+="--------------Thank you visit again-----------------";
		if(f==1) {
			DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime currentTime=LocalDateTime.now();
			String time=formatter.format(currentTime);
			JOptionPane.showMessageDialog(this, bill);
			
			if(flag==0) {
				String customerAddress=JOptionPane.showInputDialog(this, "Customer city:");
				database.addOrders(customerUsername, shopkeeperUsername,database.getShopDetails("SHOPNAME", shopkeeperUsername), 
						database.getCustomerDetails("NAME", customerUsername), bill, time, customerAddress);
				database.addOrderStatus(customerUsername, shopkeeperUsername,time,null,null,bill);
			}
			else if(flag==1) {
				database.addCart(customerUsername, shopkeeperUsername,database.getShopDetails("SHOPNAME", shopkeeperUsername), 
						database.getCustomerDetails("NAME", customerUsername), bill, time);
			}
			JOptionPane.showMessageDialog(this, "Order Placed");
			setVisible(false);
			new CustomerPage(customerUsername,null,0,0);
		}
		else {
			JOptionPane.showMessageDialog(this, "select some products");
		}
		
	}

	private int getProbablePrice(String defaultPrice) {
		String str[]=defaultPrice.split("/");
		int price=0;
		try {
			price=Integer.valueOf(str[0]);
		}
		catch(Exception e) {
			String temp="";
			for(int i=0;i<defaultPrice.length();i++) {
				if(defaultPrice.charAt(i)>='0'&&defaultPrice.charAt(i)>='9') {
					temp+=defaultPrice.charAt(i)-'0';
				}
				else {
					break;
				}
			}
			if(temp.equals("")) {
				price = 0;
			}
			else {
				price=Integer.valueOf(temp);
			}
			
		}
		return price;
	}

	private void onBack() {
		setVisible(false);
		new CustomerPage(customerUsername,null,0,0);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		for(int i=0;i<productBox.length;i++) {
			if(productBox[i].isSelected()&&!selectedProductIndexList.contains((Object)i)) {
				selectedProductIndexList.add((Object)i);
				String quantity=JOptionPane.showInputDialog(this, "Enter quantity for "+productList.get(i)
					+"\n Product price is "+database.getProductDetails("PRODUCTPRICE", shopkeeperUsername, productBox[i].getText()));
				try {
					quantityList[i]=Integer.valueOf(quantity);
					productBox[i].setSelected(true);
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Quantity must be a number");
					productBox[i].setSelected(false);
					selectedProductIndexList.remove((Object)i);
				}
			}
		}
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
	
}


