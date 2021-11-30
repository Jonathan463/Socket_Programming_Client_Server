import Service.ServiceImplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

    public static void main(String[] args) throws IOException {
        ServiceImplementation serviceImplementation = new ServiceImplementation();
        ServerSocket server = new ServerSocket(8089);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        while(server != null) {
        Socket clientSocket = server.accept();

        executor.execute(() -> {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                StringBuilder requestBuilder = new StringBuilder();
                String line;
                while (!(line = br.readLine()).isBlank()) {
                    requestBuilder.append(line).append("\r\n");
                }

                String request = requestBuilder.toString();
                String[] requestsLines = request.split("\r\n");
                String[] requestLine = requestsLines[0].split(" ");
                String method = requestLine[0];
                String path = requestLine[1];
                String version = requestLine[2];
                String host = requestsLines[1].split(" ")[1];


                Path filePath = serviceImplementation.getFilePath("src/main/java/MyFiles/javaindex.html");
                Path filePath1 = serviceImplementation.getFilePath("src/main/java/MyFiles/javaindex1.json");
                Path filePath2 = serviceImplementation.getFilePath("src/main/java/MyFiles/error.html");
                if (path.equals("/") && Files.exists(filePath)) {
                    String contentType = serviceImplementation.guessContentType(filePath);
                    serviceImplementation.sendResponse(clientSocket, "200 OK", contentType, Files.readAllBytes(filePath));
                } else if (path.equals("/json") && Files.exists(filePath1)) {
                    String contentType = serviceImplementation.guessContentType(filePath1);
                    serviceImplementation.sendResponse(clientSocket, "200 OK", contentType, Files.readAllBytes(filePath1));
                } else {
                    String contentType = serviceImplementation.guessContentType(filePath2);
                    serviceImplementation.sendResponse(clientSocket, "200 OK", contentType, Files.readAllBytes(filePath2));
                }
                System.out.println(requestBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        }
    }
}
