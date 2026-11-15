import handler.RequestsHandlerSocket;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This program demonstrates a simple TCP/IP socket server.
 *
 * @author www.codejava.net
 */
public class WebServerSocket {

    private static ExecutorService executorService;
    private static final int PORT = 8000;
    private static boolean running = true;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("WebServerSocket is listening on port " + PORT);

            // Choose the thread pool type:
            //   java WebServerSocket    -> newCachedThreadPool (threads created as needed)
            //   java WebServerSocket 2  -> newFixedThreadPool(2) (2 threads + waiting queue)
            if (args.length > 0) {
                int poolSize = Integer.parseInt(args[0]);
                executorService = Executors.newFixedThreadPool(poolSize);
                System.out.println("Thread pool: newFixedThreadPool(" + poolSize + ")");
            } else {
                executorService = Executors.newCachedThreadPool();
                System.out.println("Thread pool: newCachedThreadPool()");
            }

            while (running) {
                RequestsHandlerSocket handler = new RequestsHandlerSocket(serverSocket.accept());
                executorService.submit(handler);
            }

        } catch (IOException ex) {
            System.out.println("Web server socket exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }
}
