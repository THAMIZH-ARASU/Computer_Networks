import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server{
    // This is the main method of the Server class
    public static void main(String[] args){
        try{
            DatagramSocket socket = new DatagramSocket(5000);
            System.out.println("UDP Server has started on port : " + 5000);
            byte[] receiveBuffer = new byte[1024];
            byte[] sendBuffer;

            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage, serverMessage;
            
            while (true) { 
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Client: " + clientMessage);
                if(clientMessage.equalsIgnoreCase("exit")){
                    System.out.println("Client Disconnected");
                    break;
                }

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                System.out.print("You: ");
                serverMessage = cin.readLine();
                sendBuffer = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, 0, sendBuffer.length, clientAddress, clientPort);
                socket.send(sendPacket);
                if(serverMessage.equalsIgnoreCase("exit")){
                    System.out.println("Server Disconnected");
                    break;
                }

            }
            socket.close();
            cin.close();
            System.out.println("Server has stopped");

        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}