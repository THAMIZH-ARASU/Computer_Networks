//Client : updownclient.java 

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;

public class updownclient {

    public static void main(String args[]) throws Exception {
        Socket soc;
        BufferedImage img = null;
        soc = new Socket("localhost", 4000);
        System.out.println("Client is runnimg");
        try {
            System.out.println("Reading image from disk.");
            img = ImageIO.read(new File("example.jpg"));
            byte[] bytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(img, "jpg", baos);
                baos.flush();
                bytes = baos.toByteArray();
            }
            System.out.println("Sending image to server.");
            OutputStream out = soc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);
            System.out.println("Image sent to server. ");
            dos.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Exception : " + e.getMessage());
            soc.close();
        }
        soc.close();
    }
}
