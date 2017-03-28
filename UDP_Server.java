import java.awt.Dimension;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class UDP_Server extends JFrame {
	
	public static void main(String[] args) throws InterruptedException {
		
	  UDP_Server server= new UDP_Server();
	  
	   //Scanner s = new Scanner(System.in);
	   //System.out.println("Enter Port# to be used : ");
	   //String portNum=s.nextLine();
	   
	  server.readyToReceivPacket();
   }
	
   private final JTextArea msgArea = new JTextArea();
   //Window for Sensor1
   public JFrame s1=new JFrame("Sensor1: Oxgyen Tak Level");
   public JTextArea m1=new JTextArea();
 //Window for Sensor2
   public JFrame s2=new JFrame("Sensor2: Heart Rate");
   public JTextArea m2=new JTextArea();
 //Window for Sensor3
   public JFrame s3=new JFrame("Sensor3: Location");
   public JTextArea m3=new JTextArea();
   
   private DatagramSocket socket;
	 public String msg="1";

   public UDP_Server(){ 
	   
	   //Creating windows for each sensor as well as the main hub
      super("Message Server");
      super.add(new JScrollPane(msgArea));
      super.setSize(new Dimension(450, 350));
      super.setBounds(700, 50, 450, 350);
      super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      super.setVisible(true);
      msgArea.setEditable(false);
      
	  //s1.add(m1);
	  s1.add(new JScrollPane(m1));
	  s1.setSize(new Dimension(450, 350));
	  s1.setBounds(100,500, 450, 350);
      s1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      s1.setVisible(true);
      m1.setEditable(false);
      s2.add(m2);
      
	  s2.add(new JScrollPane(m2));
	  s2.setSize(new Dimension(450, 350));
	  s2.setBounds(550, 500, 450, 350);
      s2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      s2.setVisible(true);
      m2.setEditable(false);
      s3.add(m3);
      
	  s3.add(new JScrollPane(m3));
	  s3.setSize(new Dimension(450, 350));
	  s3.setBounds(1000, 500, 450, 350);
      s3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      s3.setVisible(true);
      m3.setEditable(false);
      
      try {
         socket = new DatagramSocket(10168);

      } catch (SocketException ex) {
         System.exit(1);
      }
   }

   public void readyToReceivPacket() throws InterruptedException {
      while (true) {
         try {
			 //try to receive packet
            byte buffer[] = new byte[128];
            DatagramPacket packet =
               new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String r_p_client=new String(packet.getData());
            showMsg("\n\nData:" + r_p_client);
            
            //for data corruption
          if(packet.getLength()!=Integer.parseInt(r_p_client.substring(0, 3)))
      	  {
      		  msg="0";
      	  }
          else
          {
        	  msg="1";
        	 //Checks for sending the information to the proper screen
        	int i=r_p_client.indexOf("P");
        	int j = 0;
        	if(r_p_client.contains("T")) {
        		  j=r_p_client.indexOf("T");
			  }
			 else if (r_p_client.contains("H")) {
				j=r_p_client.indexOf("H");
			  }
			  else if (r_p_client.contains("L")) {					//contains all three
				j=r_p_client.indexOf("L");
			  }
			  String packetNum = r_p_client.substring(i,j);
			  showMsg("\nPacket Number: " + packetNum );
			  
        	  if(r_p_client.contains("T")) {
        		  int t=r_p_client.indexOf("T");
        		  
        		  if (r_p_client.contains("H")) {
					int h=r_p_client.indexOf("H");
					
					if (r_p_client.contains("L")) {					//contains all three
						int l=r_p_client.indexOf("L");
						int n=r_p_client.indexOf("N");
						
						String tank = r_p_client.substring(t, h);
						m1.append("\n"+tank);
						
						String heartrate = r_p_client.substring(h, l);
						m2.append("\n"+heartrate);
						
						String latitude = r_p_client.substring(l, n);
						String longitude = r_p_client.substring(n);
						m3.append("\n"+latitude + " " + longitude);
					} else { 											//contains only T and H
						String tank = r_p_client.substring(t, h);
						m1.append("\n"+tank);
						
						String heartrate = r_p_client.substring(h);
						m2.append("\n"+heartrate);
					}  
				  } else if (r_p_client.contains("L")) { 				//contains T and L/R
						int l=r_p_client.indexOf("L");
						int n=r_p_client.indexOf("N");
						
						String tank = r_p_client.substring(t, l);
						m1.append("\n"+tank);
						
						String latitude = r_p_client.substring(l, n);
						String longitude = r_p_client.substring(n);
						m3.append("\n"+latitude + " " + longitude);
				  } else {											//contains ONLY T
						String tank = r_p_client.substring(t);
						m1.append("\n"+tank);
				  }
        	  } else if (r_p_client.contains("H")) {
				int h=r_p_client.indexOf("H");
				
				if (r_p_client.contains("L")) {						//contains H and L/R
					int l=r_p_client.indexOf("L");
					int n=r_p_client.indexOf("N");
											
					String heartrate = r_p_client.substring(h, l);
					m2.append("\n"+heartrate);
					
					String latitude = r_p_client.substring(l, n);
					String longitude = r_p_client.substring(n);
					m3.append("\n"+latitude + " " + longitude);
				} else { 												//contains only H
											
					String heartrate = r_p_client.substring(h);
					m2.append("\n"+heartrate);
				}  
			  } else if (r_p_client.contains("L")) {						//contains ONLY L/R
					int l=r_p_client.indexOf("L");
					int n=r_p_client.indexOf("N");
					
					String latitude = r_p_client.substring(l, n);
					String longitude = r_p_client.substring(n);
					m3.append("\n"+latitude + " " + longitude);
				}
        	  
			
          }
            sendPacket(packet);
         } catch (IOException ex) {
            showMsg(ex.getMessage());
         }
      }
   }

   public void sendPacket(DatagramPacket packetReceived) {
      try {
    	  
          byte buff[]=msg.getBytes();
          DatagramPacket packet =
            new DatagramPacket(buff, buff.length,
            packetReceived.getAddress(),
            packetReceived.getPort());
         socket.send(packet);
      } catch (IOException ex) {

      }
   }

	//printing message to hub 
   public void showMsg(final String msg) {
	   System.out.println(msg);
      SwingUtilities.invokeLater(new Runnable()
      {
		  public void run() {
				msgArea.append(msg);
		  }
      });
   }

}
