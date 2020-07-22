import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

class ShopkeeperStatsPage extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView,statsPanel;
	JButton deliveryInfoButton[],backButton;
	JScrollPane scrollableView,scrollableStatsView;
	DatabaseHandler database;
	ArrayList orderStatusList,customerOrderList,customerUsernameList,orderTimeList;
	ArrayList orderedProductsList,orderedProductsPrice,deliveredOrders;
	JLabel profitLabel;
	ShopkeeperStatsPage(String username){
		database=new DatabaseHandler();
		customerOrderList=new ArrayList();
		customerUsernameList=new ArrayList();
		orderTimeList=new ArrayList();
		orderStatusList=new ArrayList();
		orderedProductsList=new ArrayList();
		orderedProductsPrice=new ArrayList();
		deliveredOrders=new ArrayList();
		profitLabel=new JLabel("Profit Earned : Rs.0");
		customerUsernameList=database.getOrderStatusDetails("CUSTOMER_USERNAME", username);
		customerOrderList=database.getOrderStatusDetails("ORDER_INFO", username);
		orderTimeList=database.getOrderStatusDetails("ORDERED_TIME", username);
		orderStatusList=database.getOrderStatusDetails("ORDER_STATUS", username);
		this.username=username;
		ordersPanel=new JPanel[customerUsernameList.size()];
		deliveryInfoButton=new JButton[customerUsernameList.size()];
		backButton=new JButton("Back");
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		
		for(int i=0;i<customerOrderList.size();i++) {
			if(orderStatusList.get(i).equals("true")) {
				deliveredOrders.add(customerOrderList.get(i));
			}
		}
		
		splitProductAndPrice(deliveredOrders);
		
		for(int i=0;i<customerUsernameList.size();i++) {
			ordersPanel[i]=new JPanel();
			ordersPanel[i].add(new JLabel(String.valueOf(database.getCustomerDetails("NAME",(String)customerUsernameList.get(i))).toUpperCase()));
			deliveryInfoButton[i]=new JButton("Info");
			JLabel statusLabel=new JLabel();
			deliveryInfoButton[i].addActionListener(this);
			if(orderStatusList.get(i).equals("true")) {
				statusLabel.setText("Delivered");
				statusLabel.setForeground(Color.decode("#006600"));
			}
			else if(orderStatusList.get(i).equals("false")) {
				statusLabel.setText("Cancelled");
				statusLabel.setForeground(Color.RED);
			}
			else if(orderStatusList.get(i).equals("falsed")) {
				statusLabel.setText("Cancelled by User");
				statusLabel.setForeground(Color.RED);
			}
			else {
				statusLabel.setText("Pending...");
				statusLabel.setForeground(Color.decode("#ff9900"));
			}
			ordersPanel[i].add(deliveryInfoButton[i]);
			ordersPanel[i].add(statusLabel);
			ordersView.add(ordersPanel[i]);
		}
		scrollableView=new JScrollPane(ordersView);
		scrollableView.setBounds(50,100,300,200);
		statsPanel=new StatsGraph(orderedProductsList,orderedProductsPrice);
		statsPanel.setBackground(Color.WHITE);
		scrollableStatsView=new JScrollPane(statsPanel);
		scrollableStatsView.setBounds(50,300,orderedProductsList.size()*100,250);
		profitLabel.setBounds(100, 550, 200, 40);
		backButton.setBounds(150,600,100,40);
		
		backButton.addActionListener(this);
		
		add(scrollableView);
		add(scrollableStatsView);
		add(profitLabel);
		add(backButton);
		frameInitialize();
	}
	private void frameInitialize() {
		setSize(500, 700);
		setLocation(400, 50);
		setTitle("Stats");
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
				}
			}
		}
	}
	void splitProductAndPrice(ArrayList orders){
		int profit=0;
		for(int i=0;i<orders.size();i++) {
			String temp[]=String.valueOf(orders.get(i)).split("\n");
			for(int j=2;j<temp.length;j++) {
				if(temp[j].startsWith("---")) {
					break;
				}
				String inter[]=temp[j].split("   ");
				String product=inter[0].trim();
				int price=Integer.valueOf(inter[3].trim());
				int f=0;
				for(int x=0;x<orderedProductsList.size();x++) {
					if(orderedProductsList.get(x).equals(product)) {
						int priceTemp=Integer.valueOf(String.valueOf(orderedProductsPrice.get(x)));
						orderedProductsPrice.remove(x);
						orderedProductsPrice.add(x,(priceTemp+price) );
						f=1;
						break;
					}
				}
				if(f==0) {
					orderedProductsList.add(product);
					orderedProductsPrice.add(price);
				}
				
			}
			String totalPrice[]=temp[temp.length-3].split("Rs.");
			profit+=Integer.valueOf(totalPrice[1].trim());
		}
		profitLabel.setText("Profits Earned : "+profit);
	}
}

class StatsGraph extends JPanel{
	ArrayList productList,priceList;
	StatsGraph(ArrayList productList,ArrayList priceList){
		this.productList=productList;
		this.priceList=priceList;
	}
	public void paint(Graphics g) {
		int x=30,y=200;
		for(int i=0;i<productList.size();i++) {
			int temp=Integer.valueOf(String.valueOf(priceList.get(i)));
			int height=temp/10;
			g.setColor(Color.decode("#009933"));
			g.fillRect(x, y-height, 30,height);
			g.setColor(Color.BLACK);
			g.drawLine(0, y, 2000, y);
			g.drawString((String)productList.get(i), x-10, y+20);
			g.drawString("Rs."+priceList.get(i), x, y-height-10);
			x+=100;
		}
		
	}
}
