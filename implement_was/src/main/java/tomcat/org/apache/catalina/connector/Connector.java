package tomcat.org.apache.catalina.connector;

import main.controller.design.Controller;
import tomcat.org.apache.coyote.http11.Http11Processor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.util.Map;

public class Connector implements Runnable {

    private static final int DEFAULT_PORT = 8199;
    private static final int DEFAULT_MAX_THREAD_COUNT = 100;

    private Map<String, Controller> controllers = null;

    private ServerSocket serverSocket;
    private boolean stopped = false;

    public Connector() {
        this(DEFAULT_PORT, DEFAULT_MAX_THREAD_COUNT, null);
    }

    public Connector(Map<String, Controller> controllers) {
        this(DEFAULT_PORT, DEFAULT_MAX_THREAD_COUNT, controllers);
    }

    public Connector(int port, int maxThreadCount, Map<String, Controller> controllers) {
        try {
            this.controllers = controllers;
            serverSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

        stopped = false;
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("connector run...");
        if (serverSocket != null) {
           while(!stopped) {
               connect();
           }
        }
    }

    public void connect() {
        try {
            process(serverSocket.accept());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void process(Socket socket) throws IOException {
        if (serverSocket == null) return;

        Runnable runnable = new Http11Processor(socket, controllers);
        (new Thread(runnable)).start();
    }
}
