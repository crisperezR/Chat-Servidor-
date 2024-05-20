package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final String username;
    private static List<ClientHandler> clients;


    public ClientHandler(Socket clientSocket, String username, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.username = username;
        ClientHandler.clients = clients;
    }

    @Override
    public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.write(username + " se ha conectado");
                out.flush();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("RECEIVED FROM CLIENT: " + inputLine);
                    if (".".equals(inputLine)) {
                        break;
                    }
                    synchronized (clients) {
                        for (ClientHandler client : clients) {
                            if (client != this) {
                                PrintWriter clientOut = new PrintWriter(client.clientSocket.getOutputStream(), true);
                                clientOut.write(username + ": " + inputLine);
                                clientOut.flush();
                            }
                        }
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
