package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CustomSocketServer server = new CustomSocketServer();
        server.start(6666);
        server.stop();
    }
}