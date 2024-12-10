import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Download {

    public static void main(String[] args) throws Exception {
        try {
            String fileName = "iana-logo-header.svg";
            String website
                    = "https://www.iana.org/_img/2022/" + fileName;
            System.out.println("Downloading File From: " + website);
            URL url = new URL(website);
            OutputStream outputStream;
            try (InputStream inputStream = url.openStream()) {
                outputStream = new FileOutputStream(fileName);
                byte[] buffer = new byte[2048];
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    System.out.println("Buffer Read of length: " + length);
                    outputStream.write(buffer, 0, length);
                }
            }
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
