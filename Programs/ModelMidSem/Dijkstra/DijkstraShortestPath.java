import java.util.*;

public class DijkstraShortestPath {

    // Dijkstra's Algorithm for finding the shortest path
    public static void dijkstra(int graph[][], int source, int numNodes) {
        // Distance array to store the shortest distance to each node
        int[] dist = new int[numNodes];
        Arrays.fill(dist, Integer.MAX_VALUE); // Set initial distance to infinity
        dist[source] = 0;

        // Priority queue for selecting the minimum distance node
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(node -> dist[node]));
        pq.add(source);

        // To keep track of predecessors (for path reconstruction)
        int[] prev = new int[numNodes];
        Arrays.fill(prev, -1); // Initialize all predecessors to -1

        // To keep track of visited nodes
        boolean[] visited = new boolean[numNodes];

        // Start processing the graph
        while (!pq.isEmpty()) {
            int u = pq.poll();  // Get the node with the smallest distance

            if (visited[u]) continue;  // Skip if already processed
            visited[u] = true;  // Mark the node as visited

            System.out.println("Processing node: " + u);

            // Explore neighbors of node u
            for (int v = 0; v < numNodes; v++) {
                if (graph[u][v] != 0 && !visited[v]) {  // Check if there is an edge u -> v
                    int weight = graph[u][v];
                    if (dist[u] + weight < dist[v]) {  // Relax the edge
                        dist[v] = dist[u] + weight;
                        prev[v] = u;
                        pq.add(v);
                        System.out.println("Edge included: " + u + " -> " + v);
                    } else {
                        System.out.println("Edge excluded: " + u + " -> " + v);
                    }
                }
            }
        }

        // Print the final shortest distances and paths
        System.out.println("\nShortest distances from node " + source + ":");
        for (int i = 0; i < numNodes; i++) {
            System.out.println("Distance to " + i + ": " + dist[i]);
        }

        // Print the path from source to each node
        System.out.println("\nPaths from source node " + source + ":");
        for (int i = 0; i < numNodes; i++) {
            System.out.print("Path to " + i + ": ");
            printPath(prev, i);
            System.out.println();
        }
    }

    // Method to print the path from source to a node
    public static void printPath(int[] prev, int node) {
        if (node == -1) {
            return; // No path exists
        }
        printPath(prev, prev[node]);
        System.out.print(node + " ");
    }

    public static void main(String[] args) {
        // Representation of the graph (adjacency matrix)
        // graph[u][v] represents the edge u -> v with weight
        int numNodes = 6;
        int[][] graph = new int[numNodes][numNodes];

        // Fill the adjacency matrix for the given graph
        graph[0][1] = 3;  // 1 -> 2 (weight 3)
        graph[0][5] = 6;  // 1 -> 6 (weight 6)
        graph[1][0] = 7;  // 2 -> 1 (weight 7)
        graph[1][2] = 2;  // 2 -> 3 (weight 2)
        graph[1][3] = 3;  // 2 -> 4 (weight 3)
        graph[2][1] = 7;  // 3 -> 2 (weight 7)
        graph[2][3] = 8;  // 3 -> 4 (weight 8)
        graph[3][2] = 6;  // 4 -> 3 (weight 6)
        graph[3][4] = 20; // 4 -> 5 (weight 20)
        graph[4][3] = 4;  // 5 -> 4 (weight 4)
        graph[5][1] = 6;  // 6 -> 2 (weight 6)
        graph[5][2] = 8;  // 6 -> 3 (weight 8)

        // Call Dijkstra's algorithm from source node 3
        dijkstra(graph, 2, numNodes); // Source node is 3 (index 2)
    }
}
