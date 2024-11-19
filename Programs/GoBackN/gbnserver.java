import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class gbnserver{ 
    private static final int PORT = 12345;
    private static final int TIMEOUT_MS = 2000; 
    private static Random random = new Random();

    
    private static int base = 0; 
    private static int nextSeqNum = 0; 
    private static int frameCount; 
    private static int windowSize;
    private static String[] messages; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        System.out.print("Enter the total number of frames to send: ");
        frameCount = scanner.nextInt();
        System.out.print("Enter the window size: ");
        windowSize = scanner.nextInt();
        scanner.nextLine();

        messages = new String[frameCount];
        for (int i = 0; i < frameCount; i++) {
            System.out.print("Enter message for frame " + i + ": ");
            messages[i] = scanner.nextLine();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Client connected. Ready to send messages...");

            while (nextSeqNum < frameCount) {
               
                while (nextSeqNum < base + windowSize && nextSeqNum < frameCount) {
                    sendFrame(out, nextSeqNum);
                    nextSeqNum++;
                }

                
                waitForAcks(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFrame(PrintWriter out, int seqNum) {
        String frame = "Frame " + seqNum + ": " + messages[seqNum];
        System.out.println("Server: Sent " + frame);

      
        if (random.nextInt(100) < 30) {
            System.out.println("Server: " + frame + " lost during transmission.");
            return; 
        }

        out.println(frame); 
    }

    private static void waitForAcks(BufferedReader in, PrintWriter out) throws IOException {
        long startTime;

        while (true) {
            startTime = System.currentTimeMillis();
            boolean ackReceived = false;

           
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
                if (in.ready()) {
                    String ack = in.readLine();
                    if (ack != null) {
                        int ackNum = Integer.parseInt(ack.split(" ")[1]);
                        System.out.println("Server: Received " + ack);
                        
                        
                        if (ackNum >= base) {
                            base = ackNum + 1;
                            ackReceived = true;
                        }
                    }
                }
            }

           
            if (!ackReceived) {
                System.out.println("Server: Timeout! Resending frames from " + base + " to " + (nextSeqNum - 1));
                nextSeqNum = base;
                for (int i = base; i < nextSeqNum + windowSize && i < frameCount; i++) {
                    sendFrame(out, i);
                }
            }

            
            if (base >= frameCount) {
                break;
            }
        }
    }
}
