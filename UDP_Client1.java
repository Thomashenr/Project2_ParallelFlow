package git;
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
	   
	 public static void main(String[] args) throws InterruptedException, IOException {
		   UDP_Client1 client=new UDP_Client1();
		   Scanner s = new Scanner(System.in);
		   System.out.println("Enter IP with numbers separated by 'Enter' : ");
		   ip1=s.nextInt();
		   ip2=s.nextInt();
		   ip3=s.nextInt();
		   ip4=s.nextInt();
		   s.nextLine();
		   System.out.println("Enter Gremlin Function Value : ");
		   String g=s.nextLine();
		   //for demo handling
		   System.out.println("Select Option to test : ");
		   System.out.println("Option 1:All Sensors together");
		   System.out.println("Option 2:Air Pack and Heart rate");
		   int o = s.nextInt();
		   //for demo handling
		   
		   while(c!=10){
			
         Random rand = new Random(); 
         DecimalFormat two = new DecimalFormat("###.00");  
			int heartRate = rand.nextInt(250);
			double latitude = rand.nextDouble() * 180;
			double longitude = rand.nextDouble() * 180;
			int tankLevel = rand.nextInt(100);
		
			//for demo handling
			Random r1 =new Random();
	        	         
			if(o==1)
			{
				String s1="HTL";
				int N = s1.length();
		        char c1=s1.charAt(r1.nextInt(N));
		        if(c1=='H')
		        {
			        msg= "H" + heartRate ;

		        }
		        else if(c1=='T')
		        {
		        	msg = "T" + tankLevel;
		        }
		        else
		        {
		        	msg = "L" + two.format(latitude )+ "N" + two.format(longitude);
		        }
		       // msg= "T" + tankLevel + "H" + heartRate + "L" + two.format(latitude )+ "N" + two.format(longitude);
	            int messageLength = msg.length();
	            String ml=Integer.toString(messageLength);
	           messageLength +=  ml.length();
	           ml=Integer.toString(messageLength);
	            if(messageLength<100)
	            {
	            	if(messageLength<10)
		            {
	            		if(messageLength==9)
	            		{
	            			messageLength = messageLength+1;
	            			ml="0"+(messageLength+1);
	            		}
	            		else
	            		{
	            			ml="00"+(messageLength+2);
	            		}
		            	
		            }
	            	else
	            	{
	            		if(messageLength==99)
	            		{
	            			messageLength = messageLength+1;
	            			ml=""+messageLength;
	            		}
	            		else
	            		{
	            			ml="0"+(messageLength+1);
	            		}
	            	}
	            }
	           // messageLength +=  ml.length();

	            msg = ml + msg;
	          //	int z=msg.getBytes().length;
			}
			
			else if(o==2)
			{
				char c1;
				if(c==0)
				{
					String s1="L";
					int N = s1.length();
			         c1=s1.charAt(r1.nextInt(N));
				}
				else
				{
				String s1="HT";
				int N = s1.length();
		         c1=s1.charAt(r1.nextInt(N));
				}
		        if(c1=='H')
		        {
			        msg= "H" + heartRate ;

		        }
		        else if(c1=='T')
		        {
		        	msg = "T" + tankLevel;
		        }
		        else
		        {
		        	msg = "L" + two.format(latitude )+ "N" + two.format(longitude);
		        }
		       // msg= "T" + tankLevel + "H" + heartRate + "L" + two.format(latitude )+ "N" + two.format(longitude);
		        int messageLength = msg.length();
	            String ml=Integer.toString(messageLength);
	           messageLength +=  ml.length();
	           ml=Integer.toString(messageLength);
	            if(messageLength<100)
	            {
	            	if(messageLength<10)
		            {
	            		if(messageLength==9)
	            		{
	            			messageLength = messageLength+1;
	            			ml="0"+(messageLength+1);
	            		}
	            		else
	            		{
	            			ml="00"+(messageLength+2);
	            		}
		            	
		            }
	            	else
	            	{
	            		if(messageLength==99)
	            		{
	            			messageLength = messageLength+1;
	            			ml=""+messageLength;
	            		}
	            		else
	            		{
	            			ml="0"+(messageLength+1);
	            		}
	            	}
	            }
	           // messageLength +=  ml.length();

	            msg = ml + msg;
			}
			//for demo handling

		/*	 
          	msg= "T" + tankLevel + "H" + heartRate + "L" + two.format(latitude )+ "N" + two.format(longitude);
            int messageLength = msg.length();
            messageLength +=  Integer.toString(messageLength).length();
            msg = messageLength + "T" + tankLevel + "H" + heartRate + "L" + two.format(latitude )+ "N" + two.format(longitude);
          	int z=msg.getBytes().length;
          	*/
          	
          	old_msg=msg;
          	
          System.out.println("\nSending message packet: "+msg);
        
          //for Gremlin
		 //if(!g.equals("0"))
		 //{
	          //StringBuilder sb=new StringBuilder(msg);
	          //int N = msg.length();
	          //Random r =new Random();
	          //int ig=Integer.parseInt(g);
	          //for(int i=0;i<ig;i++)
	          //{
	        	  //N = msg.length();
	        	  //sb.deleteCharAt(r.nextInt(N));
	          //}
	          //msg=sb.toString();

		 //}
          
          client.sendPacket();
		
		  
		  r=client.readyToReceivPacket();
		  System.out.println("received message from server :"+r);
		  
		  while(r.trim().equals("0"))
		  {
			  System.out.println("Sending again");
			  byte buff[]=old_msg.getBytes();
	            System.out.println("client side bytes :"+buff+"    "+buff.length);
	            
	            byte[] ipAddr = new byte[] { (byte)131, (byte)204, (byte)14, (byte)209};
	            InetAddress address = InetAddress.getByAddress(ipAddr);
	            System.out.println("Here " + address);
	            
	            DatagramPacket packetSend=
	               new DatagramPacket(buff, buff.length,
	               address, 10168);
	               
	            socket.send(packetSend);
	            System.out.println("\nPacket sent");
	            r=client.readyToReceivPacket();
	  		  System.out.println("received message from server :"+r);

		  }
		  //  TimeUnit.SECONDS.sleep(5);
		  //ack=Integer.parseInt(a);
	   }
	 }
	

   public void sendPacket() throws InterruptedException{
	   UDP_Client1 rpacket=new UDP_Client1();
	   try{
	         socket=new DatagramSocket();
	      }catch(SocketException ex){
	         System.exit(1);
	      }
	         try{
	        	
	            byte buff[]=msg.getBytes();
	            System.out.println("client side bytes :"+buff+"    "+buff.length);
	            
	            
	            byte[] ipAddr = new byte[] { (byte)ip1, (byte)ip2, (byte)ip3, (byte)ip4};
	            InetAddress addressT = InetAddress.getByAddress(ipAddr);
	            //System.out.println("Here " + address);
	            
	            DatagramPacket packetSend=
	               new DatagramPacket(buff, buff.length,
	               addressT, 10168);
	            socket.send(packetSend);
	            System.out.println("\nPacket sent");
	            c++;
	       
	           }catch(IOException ex){
	            System.out.println(ex.getMessage());
	         }
	      
   }

   public String readyToReceivPacket() throws InterruptedException{
      while(true){
         try{
        	 
        	 byte buff1[]=new byte[128];
            DatagramPacket packet=
               new DatagramPacket(buff1,buff1.length);
            socket.receive(packet);
            String r_p_server=new String(packet.getData());
           // Integer rp=new Integer(packet.getData());
            System.out.println("\nHost: " + packet.getAddress()
                    + "\nPort: " + packet.getPort()
                    + "\nLength: " + packet.getLength()
                    + "\nData: "
                    + r_p_server);           
            return r_p_server;

         }catch(IOException ex){
	            System.out.println(ex.getMessage());

         }
      }
   }

 
  

}

