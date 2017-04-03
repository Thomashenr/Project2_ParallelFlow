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
import java.lang.*;

class Node extends JFrame {
   private String name;
   private String configFile;
   private String nodes;
   private int attachedNodes[];
   private Node currentNode;
   private int numberNodes, value, x, y;
   private Node previousNode;
   private String machine;
   public String portNumber;
   private String location;
   public Node allNodes[];
   private File file;
   private int distances[];
   private Node nodesConnected[];


   public Node(String nodeIn) {
      name = nodeIn;
      attachedNodes = new int[12];
      distances = new int[12];
      nodesConnected = new Node[12];
      for (int i = 0; i < 12; i++) {
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
      allNodes = new Node[12];
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
            for(int x = 0; x <= 12; x++) {
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
            index = currentNode.location.indexOf(" ");
            currentNode.x = Integer.parseInt(currentNode.location.substring(0, index));
            currentNode.location = currentNode.location.replace(currentNode.location.substring(0, index + 1), "");
            index = currentNode.location.indexOf(" ");
            currentNode.y = Integer.parseInt(currentNode.location.substring(0, index));
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
      for (int b = 0; b < 12; b++) {
         if(allNodes[b] != null) {
            for(int q = 0; q < 4; q++) {
               allNodes[b].distances[q] = ((int)Math.sqrt((Math.pow(Math.abs(allNodes[b].x - allNodes[q].x), 2) + Math.pow(Math.abs(allNodes[b].y - allNodes[q].y), 2))));   
               if(allNodes[b].distances[q] <= 100) {
                  allNodes[b].nodesConnected[q] = allNodes[q];
               }
               else {
                  allNodes[b].nodesConnected[q] = null;
               }
            }
         }
      }
      
      for(int y = 0; y < 12; y++) {
         for(int z = 0; z < 12; z++) {
            if (allNodes[y] != null) {
               if(allNodes[y].nodesConnected[z] != null) {
                  System.out.println(allNodes[y].getNodeName() + " Connected to: " +  allNodes[y].nodesConnected[z].getNodeName() + " Distance: " + allNodes[y].distances[z]);
               }
            }
         }
      }
   
   }
   public boolean gremlinFunctionManet(int distance) {
      Random randSend = new Random();
      int gremlin = randSend.nextInt(100) + 1;
      double results = 100 - (distance / 5);
      results = (int) results;
      System.out.println(results);
      if (gremlin < (int) results) {
         return true;
      }
      //if gremlin doesnt drop, then send it to the server
      else {
         return false;
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
      if (args.length == 0) {
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
         
            byte buff1[] = new byte[128];
            DatagramPacket packet = new DatagramPacket(buff1,buff1.length);
         
            socket.receive(packet);
            String r_p_server = new String(packet.getData());
            System.out.println("Data: " + r_p_server);
            String source_addr=r_p_server.substring(r_p_server.indexOf("SR")+2,r_p_server.indexOf("SR")+3);
            String dest_addr=r_p_server.substring(r_p_server.indexOf("DR")+2,r_p_server.indexOf("DR")+4);
         	
            int i1=r_p_server.indexOf("P");
            int j1 = 0;
            if(r_p_server.contains("H")) 
            {
               j1=r_p_server.indexOf("H");
            }
            else if (r_p_server.contains("T")) 
            {
               j1=r_p_server.indexOf("T");
            }
            else if (r_p_server.contains("L")) 
            {					
               j1=r_p_server.indexOf("L");
            }
            else if (r_p_server.contains("X")) 
            {					
               j1=r_p_server.indexOf("X");
            }
         	
            String packetNum="";
         	
            if(j1==0)
            {
               packetNum = r_p_server.substring((i1 + 1));
               System.out.println("Packet Number: " + packetNum );
            }
            else
            {
               packetNum = r_p_server.substring((i1 + 1),j1);
               System.out.println("Packet Number: " + packetNum );
            }
         	
         
            String previous_node="";
         
            int test = Integer.parseInt(packetNum);
            int test2 = Integer.parseInt(cache_table[0][1]);
            if(Integer.parseInt(packetNum) > Integer.parseInt(cache_table[0][1]))
            {
               cache_table[0][1]=packetNum;
               System.out.println("-----");
               previous_node = r_p_server.substring(r_p_server.indexOf("PN")+2);
               System.out.println("Previous Node: " + previous_node);
               r_p_server = r_p_server.substring(0, r_p_server.indexOf("PN"));
               r_p_server = r_p_server.trim() +"PN"+(noden);
            
               for(int i=0;i<12;i++)
               {
                  int an= node.allNodes[noden-1].getAttachedNode(i);
               
               
                  if(node.allNodes[noden-1].getAttachedNode(i)==0)
                  {
                     break;
                  }
               
                  if(Integer.parseInt(previous_node)!=an && Integer.parseInt(source_addr)!=noden)
                  {
                     int an_port = Integer.parseInt(node.allNodes[an-1].portNumber);
                  	//Thread.sleep(1000); //pause for readability
                     System.out.println("r_p_server : "+r_p_server);
                     System.out.println("Forwarding from port :"+port+"to :"+an_port);
                     byte buff[]=r_p_server.getBytes();
                  	//System.out.println("buffer length"+buff.length);
                     InetAddress addressT = InetAddress.getLocalHost();
                  
                     DatagramPacket packetSend = new DatagramPacket(buff, buff.length, addressT, an_port);
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