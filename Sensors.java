import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UDP_Client1 extends JFrame{

	   private static DatagramSocket socket;
	   public static int c=0;
	   public static int ack=1;
	   public static String r="1";
	   public static String msg="";
	   public static String old_msg="0";
	   
	   public static int ip1 = 0;
	   public static int ip2 = 0;
	   public static int ip3 = 0;
	   public static int ip4 = 0;
	   public static int g = 0;
	   
	 public static void main(String[] args) throws InterruptedException, IOException {
		   UDP_Client1 client=new UDP_Client1();
		   Scanner s = new Scanner(System.in);
		   System.out.println("Enter IP with numbers separated by 'Enter' : ");
		   ip1=s.nextInt();
		   ip2=s.nextInt();
		   ip3=s.nextInt();
		   ip4=s.nextInt();
		   s.nextLine();
		   System.out.println("Enter Gremlin Function Value (0-100) : ");
		   g=s.nextInt();
		   //for demo handling
		   
		   while(c!=100){ //have it set to stop sending packets once 100 are sent
			   Thread.sleep(500); //pause for readability
			
				//generating random values for sensors
				Random rand = new Random(); 
				DecimalFormat two = new DecimalFormat("###.00");  
				int tankLevel = client.getTankLevel();
				int heartRate = client.getHeartRate();
				double latitude = client.getLoc();
				
				msg = "";
				msg = "P" + c; //add the packet number
				
				//creating the message string based on sensors that have data
				if (tankLevel != 0) {
					msg = msg + "T" + tankLevel;
				}
				if (heartRate != 0) {
					msg = msg + "H" + heartRate;
				}
				if (latitude != 00.00) {
					Random randN = new Random();
					double longitude = randN.nextDouble() * 180 + 1;
					msg = msg + "L" + two.format(latitude) + "N" + two.format(longitude);
				}
				//adding message length header
				int length = msg.length() + 3;
				String lengthString = "";
				if (length < 10) {
					lengthString = "00" + length;
				} else if (length < 100) {
					lengthString = "0" + length;
				}
				else {
					lengthString = Integer.toString(length);
				}
				//adding length to message string
				msg = lengthString + msg;
		
			  //saving old message in case we need to retransmit
			  old_msg=msg;
			  System.out.println("\nSending message packet: "+msg);
			  //sending packet
			  client.sendPacket();
			  //preparing to recieve ACK
			  r=client.readyToReceivPacket();
			  
			  c++;
			  
			  //while(r.trim().equals("0"))
			  //{
				  //System.out.println("Sending again");
				  //byte buff[]=old_msg.getBytes();
					
					//byte[] ipAddr = new byte[] { (byte)131, (byte)204, (byte)14, (byte)209};
					//InetAddress address = InetAddress.getByAddress(ipAddr);
					
					//DatagramPacket packetSend= new DatagramPacket(buff, buff.length, address, 10168);
					//socket.send(packetSend);
					//r=client.readyToReceivPacket();
			  //}
		   }
		 }
	
//function to send the packet
   public void sendPacket() throws InterruptedException{
	   UDP_Client1 rpacket=new UDP_Client1();
	   try{
	         socket=new DatagramSocket();
	      }catch(SocketException ex){
	         System.exit(1);
	      }
	         try{
	        	
	            byte buff[]=msg.getBytes();
	            
	            byte[] ipAddr = new byte[] { (byte)ip1, (byte)ip2, (byte)ip3, (byte)ip4};
	            InetAddress addressT = InetAddress.getByAddress(ipAddr);
	            
	            //gremlin function to determin if the packet is dropped
				
				boolean gremlin = gremlinFunctionEthernet();
				if (gremlin == false) {
					System.out.println("Packet Number " + c + " Dropped!");
				}
				//if gremlin doesnt drop, then send it to the server
				else {
					DatagramPacket packetSend=
					   new DatagramPacket(buff, buff.length,
					   addressT, 10168);
					socket.send(packetSend);
				}
	       
	           }catch(IOException ex){
	            System.out.println(ex.getMessage());
	         }
   }
   
   public boolean gremlinFunctionEthernet() {
	   Random randSend = new Random();
		int gremlin = randSend.nextInt(100) + 1;
		if (gremlin < g) {
			return false;
		}
		//if gremlin doesnt drop, then send it to the server
		else {
			return true;
		}
	   
   }
   public String readyToReceivPacket() throws InterruptedException{
	   
	int counter = 0;
      while(true){
		  if(counter == 0) {
				  
			 try{
				byte buff1[]=new byte[128];
				DatagramPacket packet=
				new DatagramPacket(buff1,buff1.length);
				counter = 1;
				
				socket.setSoTimeout(1000); //set timeout for if a packet is lost
				
				socket.receive(packet);
				
				String r_p_server=new String(packet.getData());
				System.out.println("Data: " + r_p_server);
						           
				return r_p_server;

			 }catch(IOException ex){
					System.out.println(ex.getMessage());

			 }
			}
			else {//if a packet has been dropped, then here we resend it
				
		   UDP_Client1 client1=new UDP_Client1();
				System.out.println("Resend!");
				client1.sendPacket();
				counter = 0;
			}
      }
  }  
  //functions for getting random data for each sensor
   public int getTankLevel() {
	   int rateIn = 100;
	   Random randT = new Random();
	   int rateDeterminant = randT.nextInt(100) + 1;
	   if (rateIn >= rateDeterminant) {
		   int rateT = randT.nextInt(100) + 1;
		   return rateT;
	   }
	   else {
		   return 0;
	   }
   }
  //functions for getting random data for each sensor
	  public int getHeartRate() {
	   int rateIn = 30;
	   Random randH = new Random();
	   int rateDeterminant = randH.nextInt(100) + 1;
	   if (rateIn >= rateDeterminant) {
		   int rateH = randH.nextInt(50) + 50;
		   return rateH;
	   }
	   else {
		   return 0;
	   }
   }
  //functions for getting random data for each sensor
   public double getLoc() {
	   int rateIn = 60;
	   Random randL = new Random();
	   int rateDeterminant = randL.nextInt(100) + 1;
	   if (rateIn >= rateDeterminant) {
		   double rateL = randL.nextDouble() * 180;
		   return rateL;
	   }
	   else {
		   return 00.00;
	   }
   }

 
  

}

