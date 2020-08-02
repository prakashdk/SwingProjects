import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;

class CustomerCart extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView,titlePanel;
	JButton deliveryInfoButton[],placeOrderButton[],removeButton[],backButton;
	JScrollPane scrollableView;
	DatabaseHandler database;
	ArrayList customerOrderList,shopkeeperUsernameList,orderTimeList;
	JLabel backgroundLabel,titleLabel,noOrdersLabel;
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
		backButton=new JButton();
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		backgroundLabel=new JLabel();
		titleLabel=new JLabel("My Cart",SwingConstants.CENTER);
		titlePanel=new JPanel();
		noOrdersLabel=new JLabel("Cart is Empty!",SwingConstants.CENTER);
		
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
			ordersPanel[i].setBackground(Color.WHITE);
			ordersPanel[i].setBorder(new MatteBorder(0,0,1,0,Color.BLACK));
			ordersView.add(ordersPanel[i]);
		}
		ordersView.setBackground(Color.WHITE);
		scrollableView=new JScrollPane(ordersView);
		
		if(shopkeeperUsernameList.size()==0) {
			noOrdersLabel.setVisible(true);
			scrollableView.setVisible(false);
		}
		else {
			noOrdersLabel.setVisible(false);
			scrollableView.setVisible(true);
		}
		
		scrollableView.setBounds(40,150,400,200);
		backButton.setBounds(10,10,40,40);
		noOrdersLabel.setBounds(50,200,300,40);
		titlePanel.setBounds(0, 0, 500, 60);
		titleLabel.setBounds(80,10,300,40);
		
		backButton.addActionListener(this);
		noOrdersLabel.setFont(new Font("Vederna",Font.BOLD,16));
		noOrdersLabel.setForeground(Color.RED);
		scrollableView.setBackground(Color.WHITE);
		titlePanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
		titleLabel.setBackground(Color.WHITE);
		titlePanel.setBackground(Color.WHITE);
		titleLabel.setFont(new Font("Timesroman",Font.BOLD,24));
		
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
		
		setContentPane(backgroundLabel);
		add(titleLabel);
		add(titlePanel);
		add(noOrdersLabel);
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
