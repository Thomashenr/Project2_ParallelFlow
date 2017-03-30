import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
//import java.text.DecimalFormat;
//import java.util.Scanner;
import javax.swing.JFrame;

public class Sensor_AirPack extends JFrame{

	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DatagramSocket socket;
	   public static int c=3000;
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
		   Sensor_AirPack client=new Sensor_AirPack();
//		   @SuppressWarnings("resource")
//		Scanner s = new Scanner(System.in);
//		   System.out.println("Enter IP with numbers separated by 'Enter' : ");
//		   ip1=s.nextInt();
//		   ip2=s.nextInt();
//		   ip3=s.nextInt();
//		   ip4=s.nextInt();
//		   s.nextLine();
		   //for demo handling
		   
		   while(c!=3150){ //have it set to stop sending packets once 100 are sent
			   Thread.sleep(2000); //pause for specified time
			
				//generating random values for sensors

//				DecimalFormat two = new DecimalFormat("###.00");  
				int tankLevel = client.getTankLevel();
				
				msg = "";
				msg = "P" + c; //add the packet number
				
				//creating the message string based on sensors that have data
				msg = msg + "T" + tankLevel;
				
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
//			  r = client.readyToReceivPacket();
			  
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
//	   Sensor_AirPack rpacket = new Sensor_AirPack();
	   try{
	         socket = new DatagramSocket();
	      }
	   catch(SocketException ex){
	         System.exit(1);
	      }
	         try{
	        	
	            byte buff[] = msg.getBytes();
	            
//	            byte[] ipAddr = new byte[] { (byte)ip1, (byte)ip2, (byte)ip3, (byte)ip4};
//	            InetAddress addressT = InetAddress.getByAddress(ipAddr);
	            InetAddress addressT = InetAddress.getLocalHost();
	            
	            DatagramPacket packetSend=
				   new DatagramPacket(buff, buff.length,
				   addressT, 10162);
				socket.send(packetSend);
	       
	           } catch(IOException ex){
	            System.out.println(ex.getMessage());
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
				
				socket.setSoTimeout(200); //set timeout for if a packet is lost
				
				socket.receive(packet);
				
				String r_p_server=new String(packet.getData());
				System.out.println("Data: " + r_p_server);
						           
				return r_p_server;

			 }catch(IOException ex){
					System.out.println(ex.getMessage());

			 }
			}
			else {//if a packet has been dropped, then here we resend it
				
		   Sensor_AirPack client1=new Sensor_AirPack();
				System.out.println("Resend!");
				client1.sendPacket();
				counter = 0;
			}
      }
  }  
  //functions for getting random data for each sensor
   public int getTankLevel() {
	   Random randT = new Random();
	   int rateT = randT.nextInt(50) + 50;
	   return rateT;
   }
}

