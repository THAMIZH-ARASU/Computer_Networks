import java.util.*;

public class Dijkstra {
    // Total number of vertices in the graph
    int totalVertex;

    // Constructor to set the number of vertices
    Dijkstra(int totalVertex) {
        this.totalVertex = totalVertex;
    }

    // Method to find the vertex with the minimum distance value
    int minimumDistance(int[] distance, Boolean[] spSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < totalVertex; v++) {
            if (!spSet[v] && distance[v] <= min) {
                min = distance[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Method to print the solution
    void printSolution(int[] distance) {
        System.out.println("The shortest distance from the source to all other nodes:");
        for (int i = 0; i < totalVertex; i++) {
            System.out.println("To node " + i + " the shortest distance is: " + distance[i]);
        }
    }

    // Dijkstra's algorithm implementation
    void dijkstra(int[][] graph, int src) {
        int[] distance = new int[totalVertex]; // Shortest distances from src to i
        Boolean[] spSet = new Boolean[totalVertex]; // spSet[i] will be true if vertex i is included in the shortest path tree

        // Initialize all distances as INFINITE and spSet[] as false
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(spSet, false);

        // Distance from the source vertex to itself is always 0
        distance[src] = 0;

        // Find shortest path for all vertices
        for (int count = 0; count < totalVertex - 1; count++) {
            int u = minimumDistance(distance, spSet); // Pick the minimum distance vertex
            spSet[u] = true; // Mark the picked vertex as processed

            // Update distance value of the adjacent vertices
            for (int v = 0; v < totalVertex; v++) {
                if (!spSet[v] && graph[u][v] != -1 && distance[u] != Integer.MAX_VALUE
                        && distance[u] + graph[u][v] < distance[v]) {
                    distance[v] = distance[u] + graph[u][v];
                }
            }
        }

        // Print the constructed distance array
        printSolution(distance);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input the number of vertices
        System.out.print("Enter the number of vertices: ");
        int totalVertex = sc.nextInt();

        // Create the graph as an adjacency matrix
        int[][] graph = new int[totalVertex][totalVertex];
        System.out.println("Enter the adjacency matrix (-1 for no edge): ");
        for (int i = 0; i < totalVertex; i++) {
            for (int j = 0; j < totalVertex; j++) {
                graph[i][j] = sc.nextInt();
            }
        }

        // Input the source vertex
        System.out.print("Enter the source vertex: ");
        int src = sc.nextInt();

        // Create an object of the Dijkstra class and call the method
        Dijkstra obj = new Dijkstra(totalVertex);
        obj.dijkstra(graph, src);

        sc.close();
    }
}


/*

Enter the number of vertices: 5
Enter the adjacency matrix (-1 for no edge): 
-1 12 10 19 8
12 -1 3 7 2
10 3 -1 6 20
19 7 6 -1 4
8 2 20 4 -1
Enter the source vertex: 1
The shortest distance from the source to all other nodes:
To node 0 the shortest distance is: 10
To node 1 the shortest distance is: 0
To node 2 the shortest distance is: 3
To node 3 the shortest distance is: 6
To node 4 the shortest distance is: 2


*/

/*
Adjacency Matrix (9x9):

-1 3 -1 -1 -1 -1 -1 7 -1
 3 -1  7 -1 -1 -1 -1 10  4
-1  7 -1  6 -1  2 -1 -1  1
-1 -1  6 -1  8 13 -1 -1  3
-1 -1 -1  8 -1  9 -1 -1 -1
-1 -1  2 13  9 -1  4 -1  5
-1 -1 -1 -1 -1  4 -1  2  5
 7 10 -1 -1 -1 -1  2 -1  6
-1  4  1  3 -1  5  5  6 -1


 */