
import java.io.*;
import java.net.*;


public class Client{
    public static void main(String args[]){
        try{
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            int port = 5000;

            byte[] receiveBuffer = new byte[1024];
            byte[] sendBuffer;

            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage, serverMessage;
            while (true) { 
                System.out.print("You: ");
                clientMessage = cin.readLine();
                sendBuffer = clientMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                socket.send(sendPacket);
                if(clientMessage.equalsIgnoreCase("exit")){
                    System.out.println("Client Disconnected");
                    break;
                }

                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                serverMessage = new String(receivePacket.getData());
                System.out.println("Server: " + serverMessage);
                if(serverMessage.equalsIgnoreCase("exit")){
                    System.out.println("Server Disconnected");
                    break;
                }
            }
            socket.close();
            cin.close();
            System.out.println("Client has stopped");

        }catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}