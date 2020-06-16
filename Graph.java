import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author abhishekagarwal
 * The class will act as our main class, where we read the .dot file, apply 
 * Diskstra's Algorithm and write back.
 */
public class Graph{
	
	// Class variables
	private static char[] vertex;
	private static String[] nodes  = new String[20];
	private static String[] edges  = new String[20];
	private static String[] file = new String[20];
	private static int vertex_size = 0;
	private static int nodes_size = 0;
	private static int edge_size = 0;
	private static int file_size = 0;
	private static char  start ;
	private static char end;
	// Store if a Vertex is marked or not
	private static int [] Mark;
	// Stores the parent of each vertex.
	private static int [] Parent;
	
	/**
	 * @param agrs
	 */
	public static void main(String[]agrs) {
		newParsing();
		// Create a object for the class GraphMatrix
		GraphMatrix GM = new GraphMatrix();
		GM.init(nodes_size);
		Mark = new int[nodes_size];
		Parent = new int[nodes_size];
		newcreateVertex(GM);
		newcreateEdge(GM);
		Dijkstra(GM);
	}
	
	/**
	 * newParsing(): This method will help us to read our .dot file
	 */
	public static void newParsing() {
		try {
			File myObj = new File("start.dot");
		    Scanner myReader = new Scanner(myObj);
		    
		    while (myReader.hasNextLine()) {
		    	String line = myReader.nextLine();
		    	// Storing the data of the file initial,
		    	// So that it will help us to write back to file
		    	if(file_size == file.length) {
		    		file = addFactor(file);
		    	}
		    	file[file_size] = line;
		    	file_size++;
		    	
		    	// Empty line
		    	if (line.trim().isEmpty()) {
		    		continue;
		    	}
		    	String[] arrOfStr = line.split(" ");
		    	for (String a : arrOfStr) {
		    		// Line with Starting Index
		    		if(a.contains("starting")) {
		    			start = arrOfStr[0].charAt(0);
		    		}
		    		// Line with Ending Index
		    		if (a.contains("ending")) {
		    			end = arrOfStr[0].charAt(0);
		    		}
		    		// Line containing the Nodes
		            if(a.contains("Cost:")) {
		            	// If the node limit is reached double the size
		            	if (nodes_size == nodes.length -1) {
		    				nodes = addFactor(nodes);
		    			}
		    			nodes[nodes_size] = line;
		    			nodes_size ++;
		            }
		            // Line containing the edges
		            if (a.contains("weight")) {
		            	if (edge_size == edges.length -1) {
		    				edges = addFactor(edges);
		    			}
		    			edges[edge_size] = line;
		    			edge_size ++;
		            }
		            
		    	}
		       
		    }
		    myReader.close();
		}catch (FileNotFoundException e) {
		    System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	/**
	 * @param lst : The String array which want to double its size
	 * @return : The original array with double the size
	 */
	public static String[] addFactor(String[] lst) {
		String[]temp = new String[lst.length * 2];
		for (int i = 0; i < lst.length; i++){
		      temp[i] = lst[i];
		}
		return temp;
	}
	
	/**
	 * @param V : The position of the vertex in the array
	 * @return : It will return a integer index
	 * 
	 * This function will help us to determine the position of  our nodes in the vertex
	 * array. As our nodes value are not integer. We are required to create a array to
	 * represent them in integer value. 
	 * 
	 */
	public  static int findVertex(char V) {
		int pos = -1;
		for (int i = 0 ; i < vertex_size ; i++) {
			if (vertex[i] == V) {
				pos = i;
				break;
			}
		}
		return pos;
	}

	/**
	 * @param GM : The class object for class GraphMatrix
	 * This function will initial the GraphMatrix class with all the nodes and their cost
	 * The cost will be represented as distance in the class GraphMatrix
	 * -1 = INFINITY
	 */
	public static void newcreateVertex(GraphMatrix GM) {
		vertex = new char[nodes_size];
		for (int i = 0 ; i < nodes_size ; i++) {
			String line = nodes[i];
			vertex[vertex_size] = line.charAt(0);
			vertex_size++;
			String[] arrOfStr = line.split(" ");
			int encount = 0;
			for (String a : arrOfStr) {
				if(a.contains("Cost")) {
					// I used a variable to detect the position of the cost in the line
					encount = 1; 
 				}
				else if (encount == 1) {
					encount = 2;
					String cost = a.substring(0, a.indexOf('"'));
					if (cost.equals("Inf")) {
						GM.setValue(i, -1);
					}else {
						GM.setValue(i, Integer.parseInt(cost));
					}
				}
			}	
		}
	}
	
	
	
	/**
	 * @param GM : The class object for class GraphMatrix
	 * This function will help us to create edge between two vertex.
	 * All our data is stored in a array for edges:
	 * For which we will extract our nessecessary details
	 */
	public static void newcreateEdge(GraphMatrix GM) {
		for (int i =  0  ; i < edge_size ; i ++) {
			String line = edges[i];
			String[] arrOfStr = line.split(" ");
			// Extracting the index of the starting and ending vertexes from the vertex array
			int startV = findVertex(arrOfStr[0].charAt(0));
			int endV = findVertex(arrOfStr[2].charAt(0));
			for (String a : arrOfStr) {
				if(a.contains("weight")) {
					String[] withWeightStrings = a.split("=");
					String  wt = withWeightStrings[1].substring(0,withWeightStrings[1].length()-1);
					int weight = Integer.parseInt(wt);
					GM.addEdge(startV, endV, weight);
				}		
			}
		}
	}
	
	/**
	 * @param GM: The class object for class GraphMatrix
	 * This function will the bone of for Dijkstra algorithm
	 */
	public static void Dijkstra(GraphMatrix GM) {
		// Process the vertices
		int fileindex = 00;
		for (int i=0; i < nodes_size; i++) {  
			// Unmark all the vertex and make parent of each vertex -1
			Mark[i] = 0;
			Parent[i] = -1;
		}
		// For loop through all the vertex
		for (int cnt = 0 ; cnt < nodes_size ; cnt++) {
			// Find the next vertex with the minimum distance and which is unmarked
			int v = minVertex(GM);
			if (GM.dist[v] == -1) return; // Unreachable
			Mark[v] = 1; // The vertex
			writeDot(GM);
			outputfile(++fileindex);
			if (vertex[v]==end) break;
			
			int[] nList = GM.neighbors(v); // get the list of all neighbors
			for (int j=0; j<nList.length; j++) {
				int w = nList[j]; // Weight of all neighbors
				// The -1 represents the number INFINITY
				if ( (GM.dist[w] > (GM.dist[v] + GM.weight(v, w)) || GM.dist[w]==-1)) {
					GM.dist[w] = GM.dist[v] + GM.weight(v, w);
					Parent[w] = v;
				}
			}
			writeDot(GM);
			outputfile(++fileindex);
		}
		
	}
	
	/**
	 * @param GM : The class object for class GraphMatrix
	 * @return: The index of 
	 */
	public static int minVertex(GraphMatrix GM) {
		int v = 0;  // Initialize v to any unvisited vertex;
		for (int i=0; i < GM.nodeCount(); i++) {
		    if (Mark[i] != 1) { v = i; break; }
		    
		}
		for (int i=0; i< nodes_size; i++)  // Now find smallest value
		    if ((Mark[i] != 1) && GM.dist[i] != -1 ) {
		    	if ( (GM.dist[i] < GM.dist[v]))
		    		v = i;
		    }		
		  return v;
	}
	
	/**
	 * @param GM : The class object for class GraphMatrix
	 * This function will helps us write data back to the correct file.
	 */
	public static void writeDot(GraphMatrix GM) {
		for(int i = 0 ; i < file_size ; i++) {
			// Calling each individual lines
			String line = file[i];
			// To detect the line with cost in it.
			// SO that we could update the cost to reach at individual vertex
			if (file[i].contains("Cost")) {
				String[] arr = line.split(" ");
				int encount = 0;
				// Find the vertex
				int V = findVertex(arr[0].charAt(0));
				String newdata = "";
				for(int ind = 0 ; ind < arr.length ; ind++) {
						if(arr[ind].contains("Cost")) {
							// I used a variable to detect the position of the cost in the line
							// This is the actual cost and not the word cost. 
							encount = 1; 
		 				}
						else if (encount == 1) {
							encount = 2;
							// If distance is -1: Make the cost INIFINITY
							if (GM.dist[V] == -1) arr[ind] = "Inf\" ";
							// Else update the new integer cost
							else arr[ind] = Integer.toString(GM.dist[V]) + "\" ";
							// If the cost has been marked color it salmon
							if(Mark[V] == 1) {
								arr[ind]+= "color=\"salmon\" ";
							}
							if(start == arr[0].charAt(0)) arr[ind]+= "starting=true";
							if(end == arr[0].charAt(0)) arr[ind]+= "ending=true";
							
							newdata += arr[ind].trim() + "]";
							break;
						}
		
						newdata += arr[ind] + " ";
				}
				file[i] = (newdata.trim());
				
		 }
		// This statement help us identify the line with edges
		 if (file[i].contains("weight")) {
			 String[] ed = line.split(" ");
			 // The index of the two vertex which are stored in Vertex array
			 int checkv = findVertex(ed[0].charAt(0));
			 int checkw = findVertex(ed[2].charAt(0));
			 // If both the vertex are marked, and the checkw is the child of checkv then color the edge
			 if (Mark[checkv] == 1 && Mark[checkw] == 1 && ed.length < 6 && Parent[checkw] == checkv) {
				 file[i] = line.substring(0 , line.length() -1) + " color=\"salmon\"]";
			 }
			 
		 }	
		}
	}
	
	/**
	 * @param index : Number of the file to be created
	 * This function will create our dot file everytime we call it
	 */
	public static void outputfile(int index) {
			// Create a unique of the file with the number passed as a argument
			String filename = String.valueOf(index)+".dot";
			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Create a PrintWriter object for the file
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    for (int i = 0 ; i < file_size ; i++) {
		    printWriter.println(file[i]);
		    }
		    // Create the file with the data
		    File f = new File(filename);
		    // Print the absolute path of the file for you
		    System.out.println(f.getAbsolutePath());
		    printWriter.close();
	}
}
