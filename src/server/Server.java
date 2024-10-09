package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 65432;
    private static final Set<Socket> clientSockets = ConcurrentHashMap.newKeySet();
    private static int clientCount = 0;

    public static void main(String[] args) {
        System.out.println("[SERVER] Сервер запущено на порту " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                String clientName = "client-" + clientCount;
                clientSockets.add(clientSocket);
                System.out.println("[SERVER] " + clientName + " успішно підключився");
                new Thread(new ClientHandler(clientSocket, clientName)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final String clientName;

        public ClientHandler(Socket socket, String clientName) {
            this.socket = socket;
            this.clientName = clientName;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("[SERVER] " + clientName + " відключився");
                        break;
                    }
                    System.out.println("[SERVER] Отримано від " + clientName + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("[SERVER] Виникла помилка з " + clientName);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientSockets.remove(socket);
            }
        }
    }
}
