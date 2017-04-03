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
	public static int c = 3000;
	public static String msg = "";
	   
	public static int ip1 = 0;
	public static int ip2 = 0;
	public static int ip3 = 0;
	public static int ip4 = 0;
	   
	public static void main(String[] args) throws InterruptedException, IOException {
		Sensor_AirPack client=new Sensor_AirPack();
//		   Scanner s = new Scanner(System.in);
//		   System.out.println("Enter IP with numbers separated by 'Enter' : ");
//		   ip1=s.nextInt();
//		   ip2=s.nextInt();
//		   ip3=s.nextInt();
//		   ip4=s.nextInt();
//		   s.nextLine();
		   
		while(c != 3150){ //have it set to stop sending packets after ~5 minutes
			Thread.sleep(2000); //pause for specified time
			int tankLevel = client.getTankLevel();
			
			msg = "";
			//creating the message string
			msg = msg + "T" + tankLevel;

			System.out.println("\nSending message packet: " + msg);
			client.sendPacket();
			c++;
		}
	}
	
	//function to send the packet
	public void sendPacket() throws InterruptedException{
		try{
			socket = new DatagramSocket();
		}
		catch(SocketException ex){
			System.exit(1);
		}
		try{
			byte buff[] = msg.getBytes();
			//byte[] ipAddr = new byte[] { (byte)ip1, (byte)ip2, (byte)ip3, (byte)ip4};
			//InetAddress addressT = InetAddress.getByAddress(ipAddr);
			InetAddress addressT = InetAddress.getLocalHost();
			DatagramPacket packetSend = new DatagramPacket(buff, buff.length, addressT, 10162);
			socket.send(packetSend);
		}
		catch(IOException ex){
			System.out.println(ex.getMessage());
		}
	}
	//functions for getting random data for each sensor
	public int getTankLevel() {
		Random randT = new Random();
		int rateT = randT.nextInt(50) + 50;
		return rateT;
	}
}