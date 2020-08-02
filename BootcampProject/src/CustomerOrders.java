import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;

class CustomerOrders extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView,titlePanel;
	JButton deliveryInfoButton[],contactButton[],cancelButton[],backButton;
	JScrollPane scrollableView;
	DatabaseHandler database;
	ArrayList orderStatusList,customerOrderList,shopkeeperUsernameList,orderTimeList,orderContactList;
	JLabel backgroundLabel,titleLabel,noOrderLabel;
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
		backButton=new JButton();
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		backgroundLabel=new JLabel();
		titleLabel=new JLabel("My Orders",SwingConstants.CENTER);
		titlePanel=new JPanel();
		noOrderLabel=new JLabel("No Orders Placed Yet!",SwingConstants.CENTER);
		
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
			ordersPanel[i].setBackground(Color.WHITE);
			ordersPanel[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			ordersView.add(ordersPanel[i]);
		}
		
		try {
			BufferedImage backIcon = ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			Image scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBackground(Color.WHITE);
			backButton.setBorderPainted(false);
			backButton.setFocusPainted(false);
			BufferedImage background=ImageIO.read(new File(new ConstantPath().ICONS_URL+"appLogo10.jpg"));
			scaledIcon=background.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
			backgroundLabel.setIcon(new ImageIcon(scaledIcon));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Icon error");
		}
		
		ordersView.setBackground(Color.WHITE);
		scrollableView=new JScrollPane(ordersView);
		
		if(shopkeeperUsernameList.size()==0) {
			noOrderLabel.setVisible(true);
			scrollableView.setVisible(false);
		}
		else {
			noOrderLabel.setVisible(false);
			scrollableView.setVisible(true);
		}
		
		scrollableView.setBounds(25,100,440,300);
		backButton.setBounds(10,10,40,40);
		titleLabel.setBounds(80,10,300,40);
		titlePanel.setBounds(0,0,500,60);
		noOrderLabel.setBounds(50,200,300,40);
		
		backButton.addActionListener(this);
		scrollableView.setBackground(Color.WHITE);
		titlePanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
		titleLabel.setBackground(Color.WHITE);
		titlePanel.setBackground(Color.WHITE);
		titleLabel.setFont(new Font("Timesroman",Font.BOLD,24));
		noOrderLabel.setFont(new Font("Vederna",Font.BOLD,16));
		noOrderLabel.setForeground(Color.RED);
		
		setContentPane(backgroundLabel);
		add(noOrderLabel);
		add(titleLabel);
		add(titlePanel);
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
