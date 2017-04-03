import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
//import java.text.DecimalFormat;
//import java.util.Scanner;
import javax.swing.JFrame;

public class Sensor_ToxicGas extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private static DatagramSocket socket;
	public static int c = 4000;
	public static int ack = 1;
	public static String r = "1";
	public static String msg = "";
	// public static String old_msg="0";

	public static int ip1 = 0;
	public static int ip2 = 0;
	public static int ip3 = 0;
	public static int ip4 = 0;
	public static int g = 0;

	public static void main(String[] args) throws InterruptedException, IOException {
		Sensor_ToxicGas client = new Sensor_ToxicGas();
		// Scanner s = new Scanner(System.in);
		// System.out.println("Enter IP with numbers separated by 'Enter' : ");
		// ip1=s.nextInt();
		// ip2=s.nextInt();
		// ip3=s.nextInt();
		// ip4=s.nextInt();
		// s.nextLine();

		while (c != 5200) { // have it set to stop sending packets after 5 min
			Thread.sleep(250); // pause for specified time
			int toxicLevel = 0;
			toxicLevel = client.getToxicLevel();
			msg = "";
			msg = msg + "X" + toxicLevel;

			System.out.println("Sending message packet: " + msg);
			// sending packet
			client.sendPacket();
			c++;
		}
	}

	// function to send the packet
	public void sendPacket() throws InterruptedException {
		try {
			socket = new DatagramSocket();
		} catch (SocketException ex) {
			System.exit(1);
		}
		try {
			byte buff[] = msg.getBytes();
			// byte[] ipAddr = new byte[] { (byte)ip1, (byte)ip2, (byte)ip3,
			// (byte)ip4};
			// InetAddress addressT = InetAddress.getByAddress(ipAddr);
			InetAddress addressT = InetAddress.getLocalHost();
			DatagramPacket packetSend = new DatagramPacket(buff, buff.length, addressT, 10163);
			socket.send(packetSend);

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// functions for getting random data for each sensor
	public int getToxicLevel() {
		Random randT = new Random();
		int rateT = randT.nextInt(40) + 10;
		return rateT;
	}
}
