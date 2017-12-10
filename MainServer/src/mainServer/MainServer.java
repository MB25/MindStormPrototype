package mainServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Deals with getting Data from our python Script (which reads input from the Arduino)
 * 
**/
public class MainServer {
	public static void main(String[] args) throws Exception	{
	      System.out.print("running..");
		  int [] values = new int[5];							//For storing the finger-values
		  DatagramSocket serverSocket = new DatagramSocket(9876);
		  byte[] recData = new byte[20];
		  while(true){
			  DatagramPacket receivePacket = new DatagramPacket(recData, recData.length);
              serverSocket.receive(receivePacket);
              String sentence = new String( receivePacket.getData());
              System.out.println("RECEIVED: " + sentence);
              
              values = decodeReceivedString(sentence);
              /*
              for(int i = 0; i<5; i++){
            	  System.out.println("testprint..");
            	  System.out.println(i +": "+ values[i]);
              }
              */
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
	   			System.out.println("gesture: " + gesture);
		  }
		  
		  
		 }
	
	/**
	 * For converting received string to an array
	 * @param recStr = The String received 
	 * @return integer array with extracted values
	 */
	
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
	
	
	
	
	
	}

