import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

class OrdersPage extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView;
	JButton deliveryInfoButton[],takeDeliveryButton[],cancelButton[],backButton;
	JScrollPane scrollableView;
	DatabaseHandler database;
	ArrayList customerNameList,customerOrderList,customerUsernameList,orderTimeList;
	OrdersPage(String username){
		database=new DatabaseHandler();
		customerNameList=new ArrayList();
		customerOrderList=new ArrayList();
		customerNameList=database.getShopkeeperOrderDetails("CUSTOMER_NAME", username);
		customerOrderList=database.getShopkeeperOrderDetails("ORDER_INFO", username);
		customerUsernameList=database.getShopkeeperOrderDetails("CUSTOMER_USERNAME", username);
		orderTimeList=database.getShopkeeperOrderDetails("ORDERED_TIME", username);
		this.username=username;
		ordersPanel=new JPanel[customerNameList.size()];
		deliveryInfoButton=new JButton[customerNameList.size()];
		takeDeliveryButton=new JButton[customerNameList.size()];
		backButton=new JButton("Back");
		cancelButton=new JButton[customerNameList.size()];
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		for(int i=0;i<customerNameList.size();i++) {
			ordersPanel[i]=new JPanel();
			ordersPanel[i].add(new JLabel(String.valueOf(customerNameList.get(i)).toUpperCase()));
			deliveryInfoButton[i]=new JButton("Info");
			takeDeliveryButton[i]=new JButton("Take Delivery");
			cancelButton[i]=new JButton("Cancel");
			deliveryInfoButton[i].addActionListener(this);
			takeDeliveryButton[i].addActionListener(this);
			cancelButton[i].addActionListener(this);
			ordersPanel[i].add(deliveryInfoButton[i]);
			ordersPanel[i].add(takeDeliveryButton[i]);
			ordersPanel[i].add(cancelButton[i]);
			ordersView.add(ordersPanel[i]);
		}
		scrollableView=new JScrollPane(ordersView);
		scrollableView.setBounds(100,100,300,200);
		backButton.setBounds(150,350,100,40);
		
		backButton.addActionListener(this);
		
		add(scrollableView);
		add(backButton);
		frameInitialize();
	}
	private void frameInitialize() {
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Orders");
		setLayout(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backButton) {
			setVisible(false);
			new ShopkeeperPage(username);
		}
		else {
			for(int i=0;i<deliveryInfoButton.length;i++) {
				if(e.getSource()==deliveryInfoButton[i]) {
					JOptionPane.showMessageDialog(this,customerOrderList.get(i)+"\nOrdered Time : "+orderTimeList.get(i));
					break;
				}
				if(e.getSource()==takeDeliveryButton[i]) {
					String contactNumber=JOptionPane.showInputDialog(this, "Enter Contact Number");
					
					database.deleteOrder((String)customerUsernameList.get(i),username, (String)orderTimeList.get(i));
					database.updateOrderStatus((String)customerUsernameList.get(i), username, (String)orderTimeList.get(i), "true",contactNumber);
					JOptionPane.showMessageDialog(this,"Delivery Accepted");
					setVisible(false);
					new OrdersPage(username);
					break;
				}
				if(e.getSource()==cancelButton[i]) {
					database.deleteOrder((String)customerUsernameList.get(i),username, (String)orderTimeList.get(i));
					database.updateOrderStatus((String)customerUsernameList.get(i), username, (String)orderTimeList.get(i), "false",null);
					JOptionPane.showMessageDialog(this,"Delivery Cancelled");
					setVisible(false);
					new OrdersPage(username);
					break;
				}
			}
		}
	}
}
