import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;

class CustomerCart extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView;
	JButton deliveryInfoButton[],placeOrderButton[],removeButton[],backButton;
	JScrollPane scrollableView;
	DatabaseHandler database;
	ArrayList customerOrderList,shopkeeperUsernameList,orderTimeList;
	CustomerCart(String username){
		database=new DatabaseHandler();
		customerOrderList=new ArrayList();
		shopkeeperUsernameList=new ArrayList();
		orderTimeList=new ArrayList();
		shopkeeperUsernameList=database.getCartDetails("SHOPKEEPER_USERNAME", username);
		customerOrderList=database.getCartDetails("ORDER_INFO", username);
		orderTimeList=database.getCartDetails("ORDERED_TIME", username);
		this.username=username;
		ordersPanel=new JPanel[shopkeeperUsernameList.size()];
		deliveryInfoButton=new JButton[shopkeeperUsernameList.size()];
		placeOrderButton=new JButton[shopkeeperUsernameList.size()];
		removeButton=new JButton[shopkeeperUsernameList.size()];
		backButton=new JButton("Back");
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		
		for(int i=0;i<shopkeeperUsernameList.size();i++) {
			ordersPanel[i]=new JPanel();
			ordersPanel[i].add(new JLabel(String.valueOf(database.getShopDetails("SHOPNAME",(String)shopkeeperUsernameList.get(i))).toUpperCase()));
			deliveryInfoButton[i]=new JButton("Info");
			deliveryInfoButton[i].addActionListener(this);
			placeOrderButton[i]=new JButton("Place Order");
			placeOrderButton[i].addActionListener(this);
			removeButton[i]=new JButton("Cancel");
			removeButton[i].addActionListener(this);
			ordersPanel[i].add(deliveryInfoButton[i]);		
			ordersPanel[i].add(placeOrderButton[i]);
			ordersPanel[i].add(removeButton[i]);
			ordersView.add(ordersPanel[i]);
		}
		scrollableView=new JScrollPane(ordersView);
		scrollableView.setBounds(50,10,300,200);
		backButton.setBounds(150,350,100,40);
		
		backButton.addActionListener(this);
		
		add(scrollableView);
		add(backButton);
		frameInitialize();
	}
	private void frameInitialize() {
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("My Cart");
		setLayout(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backButton) {
			setVisible(false);
			new CustomerPage(username,null,0,0);
		}
		else {
			for(int i=0;i<deliveryInfoButton.length;i++) {
				if(e.getSource()==deliveryInfoButton[i]) {
					JOptionPane.showMessageDialog(this,customerOrderList.get(i)+"\nOrdered time : "+orderTimeList.get(i));
				}
				if(e.getSource()==placeOrderButton[i]) {
					DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					LocalDateTime currentTime=LocalDateTime.now();
					String time=formatter.format(currentTime);
					String customerAddress=JOptionPane.showInputDialog(this, "Customer city:");
					database.addOrders(username,(String) shopkeeperUsernameList.get(i),database.getShopDetails("SHOPNAME", (String)shopkeeperUsernameList.get(i)), 
							database.getCustomerDetails("NAME", username), (String)customerOrderList.get(i), time, customerAddress);
					database.addOrderStatus(username, (String) shopkeeperUsernameList.get(i),time,null,null,(String)customerOrderList.get(i));
					database.deleteOrderInCart(username, (String) shopkeeperUsernameList.get(i),(String) orderTimeList.get(i));
					JOptionPane.showMessageDialog(this,"Order Placed");
					setVisible(false);
					new CustomerCart(username);
				}
				if(e.getSource()==removeButton[i]) {
					database.deleteOrderInCart(username, (String) shopkeeperUsernameList.get(i),(String) orderTimeList.get(i));
					JOptionPane.showMessageDialog(this,"Removed");
					setVisible(false);
					new CustomerCart(username);
				}
			}
		}
	}
}
