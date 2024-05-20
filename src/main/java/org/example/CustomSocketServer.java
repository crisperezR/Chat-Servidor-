package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CustomSocketServer {
    private ServerSocket serverSocket;
    private static List<ClientHandler> clients = new ArrayList<>();
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true){
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String username = in.readLine();

            ClientHandler handler = new ClientHandler(clientSocket, username, clients);
            clients.add(handler);
            Thread tc = new Thread(handler);
            tc.start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
