package opgave02;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpServer {
    private static Socket connectionSocket;
    private static DataOutputStream outFromServer;
    private static BufferedReader inFromClient;

    public static void main(String[] args) throws IOException {
        try(ServerSocket welcomeSocket = new ServerSocket(10_000)){
            connectionSocket = welcomeSocket.accept();
            System.out.println("Client joined from:" + connectionSocket.getInetAddress());

            outFromServer = new DataOutputStream(connectionSocket.getOutputStream());
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            Thread readThread = new Thread(() -> readFromClient());
            Thread writeThread = new Thread(() -> writeToClient());

            readThread.start();
            writeThread.start();
        }
    }
    private static void readFromClient(){
        while(true){
            String messageFromClient = null;
            try {
                messageFromClient = inFromClient.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Client: \t" + messageFromClient);
        }
    }

    private static void writeToClient() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            try {
                outFromServer.writeBytes(message + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
