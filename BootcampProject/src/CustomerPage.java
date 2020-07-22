import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

class CustomerPage extends JFrame implements ActionListener,ListSelectionListener {

	String username;
	JLabel nameLabel;
	JList shopListView;
	DefaultListModel listModel;
	DatabaseHandler database;
	JButton myCartButton,goButton,filterButton,myOrdersButton;
	ArrayList<ShopInfo> shopList;
	JComboBox shopTypeBox,shopCityBox; 
	JPanel filtersPanel;
	JScrollPane scrollableListView;
	JLabel noShopLabel;
	CustomerPage(String username,ArrayList passedShopList,int typeIndex,int cityIndex){
		this.username=username;
		database=new DatabaseHandler();
		listModel=new DefaultListModel();
		myCartButton=new JButton("My Cart");
		myOrdersButton=new JButton("My Orders");
		noShopLabel=new JLabel("No Results found !");
		noShopLabel.setForeground(Color.RED);
		noShopLabel.setFont(new Font("sanseriff",Font.BOLD,24));
		noShopLabel.setBounds(150,220,300,50);
		filtersPanel=new JPanel();
		filterButton=new JButton("Filter");
		ArrayList typeList=new ArrayList();
		typeList.add("TYPES");
		typeList.add("All");
		typeList.addAll(database.getShopTypes());
		ArrayList cityList=new ArrayList();
		cityList.add("CITIES");
		cityList.add("All");
		cityList.addAll(database.getShopCities());
		typeList=removeDuplicates(typeList);
		cityList=removeDuplicates(cityList);
		shopTypeBox=new JComboBox(typeList.toArray());
		shopCityBox=new JComboBox(cityList.toArray());
		
		goButton=new JButton("Go");
		nameLabel=new JLabel("Welcome Mr."+database.getName("CUSTOMER", username).toUpperCase());
		shopList=new ArrayList<ShopInfo>();
		scrollableListView=new JScrollPane();
		if(passedShopList==null) {
			noShopLabel.setVisible(false);
			scrollableListView.setVisible(true);
			shopList=database.getShops();
		}
		else if(passedShopList.isEmpty()) {
			
			shopTypeBox.setSelectedIndex(typeIndex);
			shopCityBox.setSelectedIndex(cityIndex);
			noShopLabel.setVisible(true);
			scrollableListView.setVisible(false);
		}
		else {
			
			shopList=passedShopList;
			shopTypeBox.setSelectedIndex(typeIndex);
			shopCityBox.setSelectedIndex(cityIndex);
			noShopLabel.setVisible(false);
			scrollableListView.setVisible(true);
		}
		for(int i=0;i<shopList.size();i++) {
			ShopInfo shopInfo=shopList.get(i);
			listModel.addElement(shopInfo.shopName);
		}
		shopListView=new JList(listModel);
		shopListView.setFixedCellHeight(50);
		shopListView.setFixedCellWidth(50);
		
		shopListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollableListView=new JScrollPane(shopListView);
		nameLabel.setBounds(100,50,300,40);
		myCartButton.setBounds(0,0,120,40);
		myOrdersButton.setBounds(460,0,120,40);
		filtersPanel.setBounds(120,0,340,40);
		goButton.setBounds(200,350,100,40);
		scrollableListView.setBounds(100,150,300,200);
		
		shopListView.addListSelectionListener(this);
		myCartButton.addActionListener(this);
		myOrdersButton.addActionListener(this);
		goButton.addActionListener(this);
		filterButton.addActionListener(this);
		//shopTypeBox.addItemListener(this);
		//shopCityBox.addItemListener(this);
		
		filtersPanel.add(shopTypeBox);
		filtersPanel.add(shopCityBox);
		filtersPanel.add(filterButton);
		add(myOrdersButton);
		add(noShopLabel);
		add(filtersPanel);
		add(nameLabel);
		add(myCartButton);
		add(goButton);
		setResizable(false);
		add(scrollableListView);
		frameInitialize();
	}
	private void frameInitialize() {
		setSize(600, 500);
		setLocation(400, 100);
		setTitle("Customer Page");
		setLayout(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==myCartButton) {
			//JOptionPane.showMessageDialog(this,database.getCustomerCartDetails("ORDER_INFO", username).toString()+
				//	database.getCustomerCartDetails("ORDERED_TIME", username).toString());
			setVisible(false);
			new CustomerCart(username);
			
		}
		if(e.getSource()==myOrdersButton) {
			//JOptionPane.showMessageDialog(this,"Updating");
			//JOptionPane.showMessageDialog(this,database.getCustomerOrderDetails("ORDER_INFO", username).toString()+
				//	database.getCustomerOrderDetails("ORDERED_TIME", username).toString());
			setVisible(false);
			new CustomerOrders(username);
		}
		if(e.getSource()==filterButton) {
			String shopType=(String) shopTypeBox.getSelectedItem();
			String shopCity=(String) shopCityBox.getSelectedItem();
			if(shopType.equalsIgnoreCase("TYPES")&&shopCity.equalsIgnoreCase("CITIES")) {
				JOptionPane.showMessageDialog(this, "Select any filters");
			}
			else if((shopType.equalsIgnoreCase("All")&&shopCity.equalsIgnoreCase("CITIES"))
					||(shopType.equalsIgnoreCase("TYPES")&&shopCity.equalsIgnoreCase("All"))
					||(shopType.equalsIgnoreCase("All")&&shopCity.equalsIgnoreCase("All"))) {
				shopList=new ArrayList();
				shopList=database.getShops();
				setVisible(false);
				new CustomerPage(username,shopList,shopTypeBox.getSelectedIndex(),shopCityBox.getSelectedIndex());
			}
			else if(shopType.equalsIgnoreCase("All")||shopType.equalsIgnoreCase("TYPES")) {
				shopList=new ArrayList();
				shopList=database.getShopsWithCity(shopCity);
				setVisible(false);
				new CustomerPage(username,shopList,shopTypeBox.getSelectedIndex(),shopCityBox.getSelectedIndex());
			}
			else if(shopCity.equalsIgnoreCase("All")||shopCity.equalsIgnoreCase("CITIES")) {
				shopList=new ArrayList();
				shopList=database.getShopsWithType(shopType);
				setVisible(false);
				new CustomerPage(username,shopList,shopTypeBox.getSelectedIndex(),shopCityBox.getSelectedIndex());
			}
			else {
				shopList=new ArrayList();
				shopList=database.getShopsWithTypeAndCity(shopType,shopCity);
				setVisible(false);
				new CustomerPage(username,shopList,shopTypeBox.getSelectedIndex(),shopCityBox.getSelectedIndex());
				//JOptionPane.showMessageDialog(this, shopType+"\n"+shopCity);
			}
		}
		if(e.getSource()==goButton) {
			if(shopListView.isSelectionEmpty()) {
				JOptionPane.showMessageDialog(this,"Select some shop");
			}
			else {
				JOptionPane.showMessageDialog(this,shopListView.getSelectedValue());
			}	
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		String data="";
		int index=shopListView.getSelectedIndex();
		data+="Shop name : "+shopList.get(index).shopName+"\nType : "+shopList.get(index).shopType+"\nCity : "+shopList.get(index).shopCity;
		JOptionPane.showMessageDialog(this, data);
		setVisible(false);
		new CustomerOrderPage(username,shopList.get(index).username,0);
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

class ShopInfo{
	
	public String username;
	public String shopName;
	public String shopType;
	public String shopCity;
	
	public ShopInfo(String username,String shopName,String shopType,String shopCity) {
		this.username=username;
		this.shopName=shopName;
		this.shopType=shopType;
		this.shopCity=shopCity;
	}
	
}

class ShopkeeperInfo{
	
	public String username;
	public String shopkeeperName;
	public String shopkeeperContact;
	
	public ShopkeeperInfo(String username,String shopkeeperName,String shopkeeperContact) {
		
		this.username=username;
		this.shopkeeperName=shopkeeperName;
		this.shopkeeperContact=shopkeeperContact;
	}
}
