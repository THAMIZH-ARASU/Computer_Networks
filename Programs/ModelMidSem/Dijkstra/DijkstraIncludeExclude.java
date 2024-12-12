import java.util.*;

public class DijkstraIncludeExclude {
    int totalVertex;

    DijkstraIncludeExclude(int totalVertex) {
        this.totalVertex = totalVertex;
    }

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

    void printSolution(int[] distance) {
        System.out.println("The shortest distance from the source to all other nodes:");
        for (int i = 0; i < totalVertex; i++) {
            System.out.println("To node " + i + " the shortest distance is: " + distance[i]);
        }
    }

    void dijkstra(int[][] graph, int src) {
        int[] distance = new int[totalVertex];
        Boolean[] spSet = new Boolean[totalVertex];

        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(spSet, false);

        distance[src] = 0;

        for (int count = 0; count < totalVertex - 1; count++) {
            int u = minimumDistance(distance, spSet);
            spSet[u] = true;

            List<String> includedEdges = new ArrayList<>();
            List<String> excludedEdges = new ArrayList<>();

            for (int v = 0; v < totalVertex; v++) {
                if (!spSet[v] && graph[u][v] != -1 && distance[u] != Integer.MAX_VALUE
                        && distance[u] + graph[u][v] < distance[v]) {
                    distance[v] = distance[u] + graph[u][v];
                    includedEdges.add("(" + u + " -> " + v + ")");
                } else if (graph[u][v] != -1 && !spSet[v]) {
                    excludedEdges.add("(" + u + " -> " + v + ")");
                }
            }

            System.out.println("Iteration " + (count + 1) + ":");
            System.out.println("Included Edges: " + includedEdges);
            System.out.println("Excluded Edges: " + excludedEdges);
        }

        printSolution(distance);
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the number of vertices: ");
            int totalVertex = sc.nextInt();

            int[][] graph = new int[totalVertex][totalVertex];
            System.out.println("Enter the adjacency matrix (-1 for no edge): ");
            for (int i = 0; i < totalVertex; i++) {
                for (int j = 0; j < totalVertex; j++) {
                    graph[i][j] = sc.nextInt();
                }
            }

            System.out.print("Enter the source vertex: ");
            int src = sc.nextInt();

            DijkstraIncludeExclude obj = new DijkstraIncludeExclude(totalVertex);
            obj.dijkstra(graph, src);
        }
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
Iteration 1:
Included Edges: [(1 -> 0), (1 -> 2), (1 -> 3), (1 -> 4)]
Excluded Edges: []
Iteration 2:
Included Edges: [(4 -> 0), (4 -> 3)]
Excluded Edges: [(4 -> 2)]
Iteration 3:
Included Edges: []
Excluded Edges: [(2 -> 0), (2 -> 3)]
Iteration 4:
Included Edges: []
Included Edges: []
Excluded Edges: [(3 -> 0)]
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