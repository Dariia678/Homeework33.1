package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 65432;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("[CLIENT] Підключено до сервера");

            String message;
            while (true) {
                System.out.print("Введіть повідомлення (або 'exit' для виходу): ");
                message = in.readLine();
                out.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("[CLIENT] Не вдалося підключитися до сервера");
            e.printStackTrace();
        }
    }
}
