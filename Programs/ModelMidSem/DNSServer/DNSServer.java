import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class DNSServer {
    private static final int PORT = 1053;
    private static HashMap<String, String> localDNS = new HashMap<>();

    static {
        // Local references (example)
        localDNS.put("example.com", "192.168.1.1");
        localDNS.put("test.com", "192.168.1.2");
    }

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("DNS Server is running...");

            while (true) {
                byte[] buffer = new byte[512];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String domain = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received request for: " + domain);

                String ipAddress;
                if (isInternetAvailable()) {
                    ipAddress = getIPAddressFromDNS(domain);
                } else {
                    ipAddress = localDNS.getOrDefault(domain, "0.0.0.0");
                }

                byte[] response = ipAddress.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
                System.out.println("Sent response: " + ipAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isInternetAvailable() {
        try {
            final int timeout = 2000;
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8", 53), timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static String getIPAddressFromDNS(String domain) {
        try {
            return InetAddress.getByName(domain).getHostAddress(); 
        } catch (UnknownHostException e) {
            return "0.0.0.0"; // Not found
        }
    }
}
