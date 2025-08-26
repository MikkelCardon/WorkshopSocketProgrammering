package opgave01;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TcpClient {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost", 10_000);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            Scanner inFromUser = new Scanner(System.in);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true){
                String message = inFromUser.nextLine();
                outputStream.writeBytes(message + "\n"); //Skriver ud til server

                message = inFromServer.readLine(); //Venter på svar fra server
                System.out.println("Server: \t" + message);
            }


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
