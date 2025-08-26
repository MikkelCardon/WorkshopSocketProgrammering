package opgave03.customDNS.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    private static Socket connectionSocket;
    private static DataOutputStream outFromClient;
    private static BufferedReader inFromServer;

    public static void main(String[] args){
        connectToServer("10.10.138.81", 10_000); //Connect to DNS

        String dnsReply = DnsRequest();
        System.out.println(dnsReply);

        connectToServer(dnsReply, 10_001); //Connect to server


        Thread readThread = new Thread(() -> readFromClient());
        Thread writeThread = new Thread(() -> writeToClient());

        readThread.start();
        writeThread.start();
    }

    private static void connectToServer(String ip_address, int port){
        try {
            connectionSocket = new Socket(ip_address, port);
            System.out.println("Connected to: " + ip_address + " Port: " + port);

            outFromClient = new DataOutputStream(connectionSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String DnsRequest(){
        Scanner scanner = new Scanner(System.in);
        String replyFromServer;

        while(true) {
            String commandToDNS = scanner.nextLine();
            String[] command = commandToDNS.split(" ");

            try {
                outFromClient.writeBytes(commandToDNS + "\n");
                replyFromServer = inFromServer.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (command[0].equals("connect")) {
                System.out.println("breaking");
                break;
            }
            System.out.println("Reply from server:" + replyFromServer);
        }
        return replyFromServer;
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
