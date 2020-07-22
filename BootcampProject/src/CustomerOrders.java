import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

class CustomerOrders extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView;
	JButton deliveryInfoButton[],contactButton[],cancelButton[],backButton;
	JScrollPane scrollableView;
	DatabaseHandler database;
	ArrayList orderStatusList,customerOrderList,shopkeeperUsernameList,orderTimeList,orderContactList;
	CustomerOrders(String username){
		database=new DatabaseHandler();
		customerOrderList=new ArrayList();
		shopkeeperUsernameList=new ArrayList();
		orderTimeList=new ArrayList();
		orderStatusList=new ArrayList();
		orderContactList=new ArrayList();
		shopkeeperUsernameList=database.getOrderStatusDetailsForCustomer("SHOPKEEPER_USERNAME", username);
		customerOrderList=database.getOrderStatusDetailsForCustomer("ORDER_INFO", username);
		orderTimeList=database.getOrderStatusDetailsForCustomer("ORDERED_TIME", username);
		orderStatusList=database.getOrderStatusDetailsForCustomer("ORDER_STATUS", username);
		orderContactList=database.getOrderStatusDetailsForCustomer("CONTACT_NUMBER", username);
		this.username=username;
		ordersPanel=new JPanel[shopkeeperUsernameList.size()];
		deliveryInfoButton=new JButton[shopkeeperUsernameList.size()];
		contactButton=new JButton[shopkeeperUsernameList.size()];
		cancelButton=new JButton[shopkeeperUsernameList.size()];
		backButton=new JButton("Back");
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		
		for(int i=0;i<shopkeeperUsernameList.size();i++) {
			ordersPanel[i]=new JPanel();
			ordersPanel[i].add(new JLabel(String.valueOf(database.getShopDetails("SHOPNAME",(String)shopkeeperUsernameList.get(i))).toUpperCase()));
			deliveryInfoButton[i]=new JButton("Info");
			deliveryInfoButton[i].addActionListener(this);
			contactButton[i]=new JButton("Contact");
			contactButton[i].addActionListener(this);
			cancelButton[i]=new JButton("Cancel");
			cancelButton[i].addActionListener(this);
			JLabel statusLabel=new JLabel();
			ordersPanel[i].add(deliveryInfoButton[i]);			
			if(orderStatusList.get(i).equals("true")) {
				ordersPanel[i].add(contactButton[i]);
				statusLabel.setText("Accepted");
				statusLabel.setForeground(Color.decode("#006600"));
			}
			else if(orderStatusList.get(i).equals("false")) {
				statusLabel.setText("Cancelled by ShopOwner");
				statusLabel.setForeground(Color.RED);
			}
			else if(orderStatusList.get(i).equals("falsed")) {
				statusLabel.setText("Cancelled");
				statusLabel.setForeground(Color.RED);
			}
			else {
				ordersPanel[i].add(cancelButton[i]);
				statusLabel.setText("Waiting...");
				statusLabel.setForeground(Color.decode("#ff9900"));
			}
			
			ordersPanel[i].add(statusLabel);
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
		setTitle("My Orders");
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
				if(e.getSource()==contactButton[i]) {
					JOptionPane.showMessageDialog(this,"Contact Number : "+orderContactList.get(i));
				}
				if(e.getSource()==cancelButton[i]) {
					database.deleteOrder(username,(String) shopkeeperUsernameList.get(i),(String) orderTimeList.get(i));
					database.updateOrderStatus(username,(String)shopkeeperUsernameList.get(i), (String)orderTimeList.get(i), "falsed", null);
					JOptionPane.showMessageDialog(this,"Order Cancelled");
					setVisible(false);
					new CustomerOrders(username);
				}
			}
		}
	}
}
