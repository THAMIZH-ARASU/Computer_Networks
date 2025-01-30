import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your broadcast message: ");
        String message = br.readLine();
        byte[] buffer = message.getBytes();

        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, 4445);

        socket.send(packet);
        System.out.println("Broadcast message sent!");
        socket.close();
    }
}
