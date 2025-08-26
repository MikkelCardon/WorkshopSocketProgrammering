package opgave01;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpServer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try(ServerSocket welcomeSocket = new ServerSocket(10_000)){
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client joined from:" + connectionSocket.getInetAddress());

            DataOutputStream outFromServer = new DataOutputStream(connectionSocket.getOutputStream());
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            while(true){
                String messageFromClient = inFromClient.readLine();
                System.out.println("Client: \t" + messageFromClient);

                String responseMessage = scanner.nextLine();
                outFromServer.writeBytes(responseMessage + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
