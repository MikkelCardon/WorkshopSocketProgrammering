package opgave02;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TcpClient {
    private static Socket connectionSocket;
    private static DataOutputStream outFromClient;
    private static BufferedReader inFromServer;

    public static void main(String[] args){
        try {
            Socket socket = new Socket("10.10.138.27", 10_000);

            outFromClient = new DataOutputStream(socket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread readThread = new Thread(() -> readFromClient());
        Thread writeThread = new Thread(() -> writeToClient());

        readThread.start();
        writeThread.start();
    }

    private static void readFromClient(){
        while(true){
            String messageFromServer = null;
            try {
                messageFromServer = inFromServer.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Client: \t" + messageFromServer);
        }
    }

    private static void writeToClient() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            try {
                outFromClient.writeBytes(message + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
