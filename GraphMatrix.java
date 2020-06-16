

/**
 * @author abhishekagarwal
 * This class depict the behaviour of the adjacent matrix to store our data 
 *
 */
public class GraphMatrix {
	// Class variables
	int [][] ad_mat;
	int[] dist;
	private int total_edge;
	
	/**
	 * init: This function behaves as a constructor to the class 
	 * @param n : n is the total number of vertices be present in the graph
	 *  
	 */
	public void init(int n) {
		ad_mat = new int[n][n];
		dist = new int[n];
		total_edge = 0;
	}



	
	/**
	 * @return : Return the number of vertices
	 */
	public int nodeCount() {
		return dist.length;
	}

	
	/**
	 * @return : Return the current number of edges
	 */
	public int edgeCount() {
		return total_edge;
	}

	/**
	 * @param v : The vertex distance we want to find.
	 * @return : The distance to reach that vertex
	 */
	public int getValue(int v) {
		return dist[v];
	}

	/**
	 * @param v : The vertex whose distance we want to update.
	 * @param val : The value of the distance
	 */
	public void setValue(int v, int val) {
		dist[v] = val;
	}
		  
	
	/**
	 * Adds a new edge from node v to node w with weight wgt
	 * @param v : The starting vertex
	 * @param w : The vertex where we want to join with v
	 * @param wgt : Weight between the two vertexes
	 */
	public void addEdge(int v, int w, int wgt) {
		if (wgt == 0) return; // Can't store weight of 0
		ad_mat[v][w] = wgt;
		ad_mat[w][v] = wgt;
	    total_edge++;
	}


	/**
	 * @param v :  The starting vertex
	 * @param w : The second vertex 
	 * @return : Get the weight value for an edge
	 */
	public int weight(int v, int w) {
		return ad_mat[v][w];
	}
	
	

	/**
	 * @param v : The vertex
	 * @return : Returns an array containing the indicies of the neighbors of v
	 */
	public int[] neighbors(int v) {
		int count = 0;
	    int[] temp;
	    
	    // Calculating the number of neighbors vertex v  has 
	    for (int i=0; i< dist.length; i++)
	      if (ad_mat[v][i] != 0) count++;
	    temp = new int[count];
	    count = 0;
	    // Adding all the neighbors in the array
	    for (int i=0; i < dist.length; i++)
	      if (ad_mat[v][i] != 0) temp[count++] = i;
	    return temp;
	}

			
}
