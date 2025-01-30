// Server.java
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static HashMap<String, String> addressMap = new HashMap<>();
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String request = in.readLine();
                    String[] parts = request.split(",");
                    String operation = parts[0];
                    String response = "";

                    switch (operation) {
                        case "SAVE":
                            String ip = parts[1];
                            String mac = parts[2];
                            addressMap.put(ip, mac);
                            response = "Saved successfully";
                            break;

                        case "RETRIEVE":
                            String searchType = parts[1];
                            String searchValue = parts[2];
                            if (searchType.equals("IP")) {
                                // Find IP using MAC
                                response = addressMap.entrySet().stream()
                                    .filter(entry -> entry.getValue().equals(searchValue))
                                    .map(Map.Entry::getKey)
                                    .findFirst()
                                    .orElse("Not found");
                            } else {
                                // Find MAC using IP
                                response = addressMap.getOrDefault(searchValue, "Not found");
                            }
                            break;

                        case "DELETE":
                            String ipToDelete = parts[1];
                            if (addressMap.remove(ipToDelete) != null) {
                                response = "Deleted successfully";
                            } else {
                                response = "IP address not found";
                            }
                            break;

                        default:
                            response = "Invalid operation";
                    }

                    out.println(response);
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}