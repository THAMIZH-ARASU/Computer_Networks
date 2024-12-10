import java.util.*;

public class RARP {
    private static Map<String, String> rarpTable = new HashMap<>();

    // Function to simulate RARP request and response
    public static String rarpRequest(String macAddress) {
        // If the MAC address is in the RARP table, return the associated IP address
        if (rarpTable.containsKey(macAddress)) {
            return rarpTable.get(macAddress);
        } else {
            // If not found in RARP table, simulate sending a RARP request to find the IP address
            System.out.println("Sending RARP request for MAC: " + macAddress);
            String ipAddress = getIpAddressFromNetwork(macAddress);
            if (ipAddress != null) {
                rarpTable.put(macAddress, ipAddress); // Add to RARP table
                return ipAddress;
            } else {
                return "IP address not found.";
            }
        }
    }

    // Simulating getting the IP address from the network (in reality, this would involve low-level network code)
    private static String getIpAddressFromNetwork(String macAddress) {
        // Simulate a RARP response with a dummy IP address
        if (macAddress.equals("00:14:22:01:23:45")) {
            return "192.168.1.1"; // Example IP address
        } else if (macAddress.equals("00:14:22:01:23:46")) {
            return "192.168.1.2"; // Example IP address
        }
        return null; // IP address not found for unknown MAC
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MAC address for RARP request (e.g., 00:14:22:01:23:45): ");
        String mac = scanner.nextLine();

        // Simulate RARP request and response
        String ipAddress = rarpRequest(mac);
        System.out.println("Resolved IP Address: " + ipAddress);

        scanner.close();
    }
}
