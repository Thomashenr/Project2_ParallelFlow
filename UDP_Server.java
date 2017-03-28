package git;
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
      super("Message Server");
      super.add(new JScrollPane(msgArea));
      super.setSize(new Dimension(450, 350));
      super.setBounds(700, 50, 450, 350);
      super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      super.setVisible(true);
      msgArea.setEditable(false);
      
	  s1.add(m1);
	  s1.setSize(new Dimension(450, 350));
	  s1.setBounds(100,500, 450, 350);
      s1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      s1.setVisible(true);
      m1.setEditable(false);
      s2.add(m2);
      
	  s2.setSize(new Dimension(450, 350));
	  s2.setBounds(550, 500, 450, 350);
      s2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      s2.setVisible(true);
      m2.setEditable(false);
      s3.add(m3);
      
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
            byte buffer[] = new byte[128];
            DatagramPacket packet =
               new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String r_p_client=new String(packet.getData());
            showMsg("\n\nHost: " + packet.getAddress()
                    + "\nPort: " + packet.getPort()
                    + "\nLength: " + packet.getLength()
                    + "\nData: "
                    + r_p_client);
            
          if(packet.getLength()!=Integer.parseInt(r_p_client.substring(0, 3)))
      	  {
      		  msg="0";
      	//	showMsg("+++++++++++");
      	  }
          else
          {
        	  msg="1";
        	  if(r_p_client.contains("T"))
        	  {
        		  int y=r_p_client.indexOf("T");
        		  String level = r_p_client.substring(y);
        		  m1.append(" "+level);  
        	  }
        	  else if(r_p_client.contains("H"))
        	  {
        		  int y=r_p_client.indexOf("H");
        		  String heartrate = r_p_client.substring(y);
        		  m2.append(" "+heartrate);
        	  }
        	  else
        	  {
        		  int y=r_p_client.indexOf("L");
        		  int z=r_p_client.indexOf("N");
        		  String latitude = r_p_client.substring(y, z);
            	  String longitude = r_p_client.substring(z);
        		  m3.append("  "+latitude + " " + longitude);
        	  }
        	  
        /*	  int y=r_p_client.indexOf("T");
        	  int z=r_p_client.indexOf("H");
        	  String level = r_p_client.substring(y, z);
        	  y=r_p_client.indexOf("H");
        	  z=r_p_client.indexOf("L");
        	  String heartrate = r_p_client.substring(y, z);
        	  y=r_p_client.indexOf("L");
        	  z=r_p_client.indexOf("N");
        	  String latitude = r_p_client.substring(y, z);
        	  String longitude = r_p_client.substring(z);
        	  
        	  m1.append(level);
        	  m2.append(heartrate);
        	  m3.append(latitude + " " + longitude);*/
        	 // showMsg("----------------"+r_p_client.substring(0, 2));
        	 //Check which sensor packet it is
        	  //if(r_p_client.substring(0, 2).equals("00"))
        	  //{
        		  //if(r_p_client.contains("$"))
        		  //{
        		  //int z=r_p_client.indexOf("$");
        		  //r_p_client =r_p_client.substring(0, z);
        		  //}
        		  
        	      //m1.append(r_p_client);
        		////  showMsg("+++++++++++------------"+r_p_client);
        	  //}
        	  //else if(r_p_client.substring(0, 2).equals("01"))
        	  //{
        		  //if(r_p_client.contains("$"))
        		  //{
        		  //int z=r_p_client.indexOf("$");
        		  //r_p_client =r_p_client.substring(0, z);
        		  //}
        		  //m2.append(r_p_client);
        		////  showMsg("+++++++++++------------"+r_p_client);
        	  //}
        	  //else
        	  //{
        		  //if(r_p_client.contains("$"))
        		  //{
        		  //int z=r_p_client.indexOf("$");
        		  //r_p_client =r_p_client.substring(0, z);
        		  //}
        		  //m3.append(r_p_client);
        		////  showMsg("+++++++++++------------"+r_p_client);
        	  //}
          }
          //  TimeUnit.SECONDS.sleep(5);
            sendPacket(packet);
         } catch (IOException ex) {
            showMsg(ex.getMessage());
         }
      }
   }

   public void sendPacket(DatagramPacket packetReceived) {
      showMsg("\nEcho to client...");
      try {
    	  
    	  
          byte buff[]=msg.getBytes();
          DatagramPacket packet =
            new DatagramPacket(buff, buff.length,
            packetReceived.getAddress(),
            packetReceived.getPort());
        
         socket.send(packet);
         showMsg("\nMessage sent :"+msg);
      } catch (IOException ex) {

      }
   }

   public void showMsg(final String msg) {
      SwingUtilities.invokeLater(new Runnable()
      {
		  public void run() {
				msgArea.append(msg);
		  }
      });
   }

}
