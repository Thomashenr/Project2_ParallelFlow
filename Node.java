import java.util.*;
import java.io.*;


public class Node {
   private String name;
   private String configFile;
   private String nodes;
   private int attachedNodes[];
   private Node currentNode;
   private int numberNodes, value;
   private Node previousNode;
   private String machine;
   private String portNumber;
   private String location;
   
   
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
   
   public void linkNodes() {
      String allNodes = "";
      Queue<Node> path;
      Node currentNode = new Node("");
      String link;
      Node current;
      
   }
   
   public static void main(String[] args) {
      Node allNodes[] = new Node[10];
      String link;
      Node current;
      if (args.length < 0) {
         System.out.println("Missing argument: Configuration File needed.");
      }
      else {
         File file = new File(args[0]);
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
            for (int y = 0; y < 4; y++) {
               System.out.print(allNodes[y].getNodeName() + " ");
               System.out.print(allNodes[y].machine + " " + allNodes[y].portNumber + " " + allNodes[y].location + " links: ");
               for(int z = 0; z < 4; z++) {
                  System.out.print(allNodes[y].attachedNodes[z] + " ");
               }
               System.out.println("");
            }
         }
         catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
         
         
      }
   }
   
}