import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
//import java.util.Scanner;
import javax.swing.JFrame;

public class Sensor_Location extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private static DatagramSocket socket;
	public static int c = 2000;
	public static int ack = 1;
	public static String r = "1";
	public static String msg = "";
	public static String old_msg = "0";

	public static int ip1 = 0;
	public static int ip2 = 0;
	public static int ip3 = 0;
	public static int ip4 = 0;
	public static int g = 0;

	public static int latBegin = 50;
	public static int lonBegin = 50;

	public static void main(String[] args) throws InterruptedException, IOException {
		Sensor_Location client = new Sensor_Location();
		// Scanner s = new Scanner(System.in);
		// System.out.println("Enter IP with numbers separated by 'Enter' : ");
		// ip1=s.nextInt();
		// ip2=s.nextInt();
		// ip3=s.nextInt();
		// ip4=s.nextInt();
		// s.nextLine();

		while (c != 2600) { // have it set to stop sending packets after 5
							// minutes
			Thread.sleep(500); // pause for specified time
			// generating random values for sensors
			int latitude = client.getLoc1();
			int longitude = client.getLoc2();
			msg = "";
			msg = msg + "L0" + latitude + "N0" + longitude;
			// saving old message in case we need to retransmit
			// old_msg=msg;
			System.out.println("\nSending message packet: " + msg);
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

			DatagramPacket packetSend = new DatagramPacket(buff, buff.length, addressT, 10161);
			socket.send(packetSend);

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// functions for getting random data for each sensor
	public int getLoc1() {
		Random randL = new Random();
		int rateL = randL.nextInt(5);
		latBegin += (rateL - 2) % 99;
		if (latBegin < 10) {
			latBegin = 10;
		}

		return latBegin;
	}

	public int getLoc2() {
		Random randL = new Random();
		int rateL = randL.nextInt(5);
		lonBegin += (rateL - 2) % 99;
		if (lonBegin < 10) {
			lonBegin = 10;
		}

		return lonBegin;
	}

}
