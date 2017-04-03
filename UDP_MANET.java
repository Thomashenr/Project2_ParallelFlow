import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class Node extends JFrame {
	private String name;
	private String configFile;
	private String nodes;
	private int attachedNodes[];
	private Node currentNode;
	private int numberNodes, value;
	private Node previousNode;
	private String machine;
	public String portNumber;
	private String location;
	public Node allNodes[];
	private File file;


	public Node(String nodeIn) {
		name = nodeIn;
		attachedNodes = new int[10];
		for (int i = 0; i < 10; i++) {
			attachNewNode(0, i);
		}
	}

	public void setNodeName(String nodeName) {
		name = nodeName;   
	}

	public String getNodeName() {
		return name;
	}

	public void attachNewNode(int newNode, int index) {
		attachedNodes[index] = newNode;
	}

	public int getAttachedNode(int index) {
		return attachedNodes[index];
	}

	public long checkFileModification() {
		return file.lastModified();
	}

	public void linkNodes(String configFile) { 
		allNodes = new Node[10];
		String link;
		Node current;
		file = new File(configFile);
		String line;
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(file));
			int added = 0;
			int i = 0;
			while ((line = buffer.readLine()) != null) {
				Node currentNode = new Node("");
				int index = line.indexOf(" ");
				currentNode.setNodeName(line.substring(0, index));
				line = line.replace(currentNode.getNodeName() + " ", "");
				index = line.indexOf(" ");
				currentNode.machine = line.substring(0, index);
				line = line.replace(currentNode.machine + " ", "");
				index = line.indexOf(" ");
				currentNode.portNumber = line.substring(0, index);
				line = line.replace(currentNode.portNumber + " ", "");
				index = line.indexOf("links ");
				currentNode.location = line.substring(0, index);
				line = line.replace(currentNode.location + "links ", "");  
				for(int x = 0; x <= 3; x++) {
					if((index = line.indexOf(" ")) > 0) {
						String linkNode =  line.substring(0, index);
						line = line.replace(linkNode + " ", "");
						currentNode.attachNewNode(Integer.parseInt(linkNode), x);  
					}
					else if(line.compareTo("") == 0) {
						break;
					}
					else {
						currentNode.attachNewNode(Integer.parseInt(line), x);   
						line = line.replace(line, "");
					}
				}
				allNodes[i] = currentNode; 
				i++;
			}
			// for (int y = 0; y < 4; y++) {
			// System.out.print(allNodes[y].getNodeName() + " ");
			// System.out.print(allNodes[y].machine + " " + allNodes[y].portNumber + " " + allNodes[y].location + " links: ");
			// for(int z = 0; z < 4; z++) {
			// System.out.print(allNodes[y].attachedNodes[z] + " ");
			// }
			// System.out.println("");
			// }
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}
}

public class UDP_MANET extends Node {
	public UDP_MANET() {
		super("");
	}

	private static DatagramSocket socket;
	public static long originalTimeStamp;
	public static int ip1 = 0;
	public static int ip2 = 0;
	public static int ip3 = 0;
	public static int ip4 = 0;
	public static int g = 0;
	public static Node node = new Node("");
	public static String fileName;
	public static String[][] cache_table=new String[10][10];


	public static void main(String[] args) throws InterruptedException, IOException {
		if (args.length < 0) {
			System.out.println("Missing argument: Configuration File needed.");
		}
		fileName = args[0];
		node.linkNodes(fileName);
		originalTimeStamp = node.checkFileModification();
		//UDP_MANET client=new UDP_MANET();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter Node : ");
		int noden=s.nextInt();
		
		cache_table[0][0]="source_addr";
		cache_table[0][1]="-1";
		
		System.out.println("Current Port : "+node.allNodes[noden-1].portNumber);
		int port = Integer.parseInt(node.allNodes[noden-1].portNumber);

		socket = new DatagramSocket(port);

		while(true){

			try{   
				
				if(node.checkFileModification() > originalTimeStamp) {
					System.out.println("Config file has been modified. Must relink Nodes.");
					originalTimeStamp = node.checkFileModification();
					node.linkNodes(fileName);
				}

				byte buff1[]=new byte[128];
				DatagramPacket packet=
						new DatagramPacket(buff1,buff1.length);

				socket.receive(packet);
				String r_p_server=new String(packet.getData());
				System.out.println("Data: " + r_p_server);

				int i1=r_p_server.indexOf("P");
				int j1 = 0;
				if(r_p_server.contains("T")) 
				{
					j1=r_p_server.indexOf("T");
				}
				else if (r_p_server.contains("H")) 
				{
					j1=r_p_server.indexOf("H");
				}
				else if (r_p_server.contains("L")) 
				{					
					j1=r_p_server.indexOf("L");
				}
				String packetNum = r_p_server.substring(i1+1,j1);
				System.out.println("Packet Number: " + packetNum );

				String previous_node="";

				if(Integer.parseInt(packetNum)>Integer.parseInt(cache_table[0][1]))
				{
					cache_table[0][1]=packetNum;
					System.out.println("-----");
					if(!r_p_server.contains("PN"))
					{
						System.out.println("!PN");
						
						r_p_server=r_p_server+"PN"+(noden-1);
						previous_node = r_p_server.substring(r_p_server.indexOf("PN")+2);

					}
					else
					{
						System.out.println("PN");

						previous_node = r_p_server.substring(r_p_server.indexOf("PN")+2);
						System.out.println("Previous Node: " + previous_node);
						r_p_server = r_p_server.substring(0, r_p_server.indexOf("PN"));
						r_p_server=r_p_server+"PN"+(noden-1);

					}

					for(int i=0;i<10;i++)
					{
						int an= node.allNodes[noden-1].getAttachedNode(i);


						if(node.allNodes[noden-1].getAttachedNode(i)==0)
						{
							break;
						}

						if(Integer.parseInt(previous_node)!=an)
						{
							int an_port=Integer.parseInt(node.allNodes[an-1].portNumber);
							//Thread.sleep(1000); //pause for readability
							System.out.println("r_p_server : "+r_p_server);
							System.out.println("Forwarding from port :"+port+"to :"+an_port);
							byte buff[]=r_p_server.getBytes();
							//System.out.println("buffer length"+buff.length);
							InetAddress addressT = InetAddress.getLocalHost();

							DatagramPacket packetSend=
									new DatagramPacket(buff, buff.length,
											addressT, an_port);
							socket.send(packetSend);
						}
					}
				}
			}
			catch(IOException ex){
				System.out.println(ex.getMessage());

			}
		}
	}
}

