import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    ServerSocket server;
    Socket clientSocket;
    BufferedReader br;


    @Test
    void testForRequestMethod() throws IOException {
        server = new ServerSocket(8080);
        clientSocket = server.accept();
        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line).append("\r\n");
        }
        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String method = requestLine[0];

        assertEquals("GET", method);
        server.close();
        clientSocket.close();
    }

    @Test
    void getFilePath() throws IOException {
        server = new ServerSocket(8080);
        clientSocket = server.accept();
        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line).append("\r\n");
        }
        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String path = requestLine[1];

        assertEquals("/", path);
        server.close();
        clientSocket.close();
    }

    @Test
    void shouldTestForRequestVersion() throws IOException{
        server = new ServerSocket(8080);
        clientSocket = server.accept();
        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line).append("\r\n");
        }
        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String version = requestLine[2];

        assertEquals("HTTP/1.1", version);
        server.close();
        clientSocket.close();
    }





    @Test
    void ShouldTestTheContentType() throws IOException {
        String expected = Files.probeContentType(Path.of("src/main/java/MyFiles/javaindex.html"));
        assertEquals("text/html", expected);
    }
}