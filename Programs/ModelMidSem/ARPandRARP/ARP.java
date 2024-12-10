import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ARP {
    public static void main(String[] args) {
        String ipAddress = "255.255.255.255";  // Example IP address

        try {
            String macAddress = getMacAddress(ipAddress);
            if (macAddress != null) {
                System.out.println("MAC Address for IP " + ipAddress + ": " + macAddress);
            } else {
                System.out.println("MAC Address for IP " + ipAddress + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMacAddress(String ipAddress) {
        String macAddress = null;
        try {
            // Execute the arp command to get the ARP table
            String command = "arp -a " + ipAddress;
            Process process = Runtime.getRuntime().exec(command);

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Parse through the output to find the MAC address
            while ((line = reader.readLine()) != null) {
                if (line.contains(ipAddress)) {
                    // MAC Address is typically the 2nd field in the output after the IP
                    String[] tokens = line.split("\\s+");
                    macAddress = tokens[1];  // MAC Address is at position 1 in the output
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return macAddress;
    }
}
