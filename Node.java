import java.util.*;
import java.io.*;


public class Node {
   private String name;
   private String configFile;
   private String nodes;
   private Node attachedNodes[];
   private Node currentNode;
   private int numberNodes, value;
   private Node previousNode;
   
   public Node(String nodeIn) {
      name = nodeIn;
      for (int i = 0; i < 4; i++) {
         attachNewNode(null, i);
      }
   }
   
   public void setNodeName(String nodeName) {
      name = nodeName;   
   }
   
   public String getNodeName() {
      return name;
   }
   
   public void attachNewNode(Node newNode, int index) {
      attachedNodes[index] = newNode;
   }
   
   public Node getAttachedNode(int index) {
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
      if (args.length < 0) {
         System.out.println("Missing argument: Configuration File needed.");
      }
      else {
         File file = new File(args[0]);
         String line;
         BufferedReader buffer = null;
         try {
            buffer = new BufferedReader(new FileReader(file));
            while ((line = buffer.readLine()) != null) {
               System.out.println(line);
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