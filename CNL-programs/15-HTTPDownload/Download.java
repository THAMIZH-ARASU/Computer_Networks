import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Download{
    public static void main(String[] args){
        String filename = "Omen.jpg";
        String website = "https://i.pinimg.com/originals/93/31/24/933124fe6b508c2ab7cec6770bc085f5.jpg";
        System.out.println("Downloading File from " + website);
        int totalBytes = 0;

        try {
            URI uri = new URI(website);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK){
                System.out.println("Failed to download file: " + responseCode);
            }

            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(filename);

            byte[] buffer = new byte[2048];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1){
                totalBytes += bytesRead;
                System.out.println("Buffer Read of length: " + bytesRead);
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("Download Completed: " + filename);
            System.out.println("Total Bytes Downloaded: " + totalBytes);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}