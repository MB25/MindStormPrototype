package mainServer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Deals with getting Data from our python Script (which reads input from the Arduino)
 * 
**/
public class MainServer {
	
	private Robot robot;
	private boolean success = false;
	private int[] values;
	
	public void initiate() {
		
		
		JFrame frame = new JFrame("Connect Prototype");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel label = new JLabel("Please enter Robot IP");
		JButton connect = new JButton();
		connect.setText("Connect");
		panel.add(label);
		panel.add(connect);
		JTextField connectfield = new JTextField(10);
	    frame.add(connectfield, BorderLayout.NORTH);
		frame.add(panel);
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		connect.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				String ip = connectfield.getText();
				label.setText("connecting...");
				try {
					robot = new Robot(ip);
					success = true;
				} 
				catch (Exception e1) {
					label.setText("Please try again! "+e1.getMessage());
				}
				  
				if (success) {
					frame.dispose();
					try {
						start();
					} catch (Exception e1) {System.out.println(e1.getMessage());}
				}
			}
		});
	}
	
	
	public void start() throws Exception {
		
		System.out.println("Robot's online");
		  
		
		values = new int[5];							//For storing the finger-values
		DatagramSocket serverSocket = new DatagramSocket(9875);
		byte[] recData = new byte[20];
		System.out.print("Server running..");
		
		  
		JFrame frame = new JFrame("Debugging Prototype");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton button = new JButton();
		button.setText("Start");
		panel.add(button);
		
		JLabel thumb = new JLabel("Thumb: ");
		JLabel index = new JLabel("Index: ");
		JLabel middle = new JLabel("Middle: ");
		JLabel ring = new JLabel("Ring: ");
		JLabel pinky = new JLabel("Pinky: ");
		panel.add(thumb);
		panel.add(index);
		panel.add(middle);
		panel.add(ring);
		panel.add(pinky);
		
		frame.add(panel);
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

		
		button.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if(success)
		        {
		            success = false;
		            button.setText("Terminate");
		            
		            new Thread(
		              new Runnable() {
		                public void run() {
		                  while(!success) {
		                	DatagramPacket receivePacket = new DatagramPacket(recData, recData.length);
		                    try {
								serverSocket.receive(receivePacket);
							} catch (IOException e1) {e1.printStackTrace();}
		                    String sentence = new String( receivePacket.getData());
		                    System.out.println("RECEIVED: " + sentence);
		                      
		                    values = decodeReceivedString(sentence);
		                    
		                    thumb.setText("Thumb: "+values[0]);
		                    index.setText("Thumb: "+values[1]);
		                    middle.setText("Thumb: "+values[2]);
		                    ring.setText("Thumb: "+values[3]);
		                    pinky.setText("Thumb: "+values[4]);
		                        
		                    try {
								robot.readinput(values);
							} catch (RemoteException e) {e.printStackTrace();}
		                    
		                    
		                    
		                    /*
		                    for(int i = 0; i<5; i++){
		                    	System.out.println("testprint..");
		                    	System.out.println(i +": "+ values[i]);
		                    }
		                        
		                    String gesture ="";
		          	   		switch(getGesture(values)){
		             			case 1: gesture = "Left";
		             			break;
		             			case 2: gesture = "Forward";
		             			break;
		             			case 3: gesture = "Right";
		             			break;
		             			case 4: gesture = "Stop";
		             			break;
		             			default: gesture = "no gesture detected";
		             		}
		          	   		
		          	   		System.out.println("gesture: " + gesture);*/
		                        
		                  }
		                }
		              }
		            ).start();
		        }
		        else
		        {
		        	success = true;
		            close(serverSocket);
		            // As you're controlling the playing value in the Thread,
		            // setting it here to false would mean that your thread stops too.
		        }
			} 
		});
		
		
		/*final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("terminate");
				serverSocket.close();
				running = false;
				try {
					mainThread.join();
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/
		  
		  
	}
	
	/**
	 * For converting received string to an array
	 * @param recStr = The String received 
	 * @return integer array with extracted values
	 */
	
	//obsolete
	private static int getGesture(int[] values){
		int threshold = 100;
		int id = -1;
		
		if(values[0] <= threshold && values[1] >= threshold && values[2] >= threshold 
				&& values[3] >= threshold && values[4] >= threshold) return 1;
		
		if(values[0] >= threshold && values[1] <= threshold && values[2] >= threshold 
				&& values[3] >= threshold && values[4] >= threshold) return 2;
		
		if(values[0] >= threshold && values[1] >= threshold && values[2] >= threshold 
				&& values[3] >= threshold && values[4] <= threshold) return 3;
		
		if(values[0] >= threshold && values[1] >= threshold && values[2] >= threshold 
				&& values[3] >= threshold && values[4] >= threshold) return 4;
		
				
		/*
		if(values[0] >= threshold){
			if(values[1] >= threshold && values[2] >= threshold && 
					values[3] >= threshold && values[4] >= threshold){
				return 4;
			}
			return 1;
		}
		if(values[1] >= threshold){
			if(values[2] >= threshold){
				return 2;
			}
				
		}
		if(values[4] >= threshold) return 3;
		*/
		return id;
	}
	
	private static int[] decodeReceivedString(String recStr){
		int[] arr = new int[5];
		int idx = 0;
		String h = "";
		int counted = 0;
		//System.out.println(recStr.length());
		for(int j = 0; counted < 5; j++){	//iterates through string until it finds a ",". Then it stores all the values since the last ","
			char cur = recStr.charAt(j);		//as one number in the values array
			if(cur != ",".charAt(0))
			{
				h	+= cur;
			}else{
				arr[counted++] = Integer.parseInt(h);
				h = "";
				
			}
		}
		
		
		return arr;
	}
	
	
	public void close(DatagramSocket serverSocket) {
		System.out.println("Terminate");
		serverSocket.close();
		try {
			robot.close();
		} catch (Exception e1) {e1.printStackTrace();}
		System.exit(1);
	}
	
	
}

