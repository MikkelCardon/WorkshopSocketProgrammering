package opgave03.customDNS;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CustomDNS {
    private static Map<String, String> serverList = new HashMap<>();
    private static DataOutputStream outFromDNS;
    private static BufferedReader inFromClient;

    public static void main(String[] args) {
            try(ServerSocket welcomeSocket = new ServerSocket(10_000);){
                System.out.println("DNS running....");

                while(true){
                    Socket connectionSocket = welcomeSocket.accept();
                    System.out.println("\u001B[32m" + "New connection : " + connectionSocket.getInetAddress() + "\u001B[0m");

                    outFromDNS = new DataOutputStream(connectionSocket.getOutputStream());
                    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    while(true) {
                        String commandFromClient = inFromClient.readLine().toLowerCase();
                        System.out.println(commandFromClient);
                        String[] command = commandFromClient.split(" ");

                        String messageToClient = switch (command[0]) {
                            case "connect" -> getIpAddress(command[1]);
                            case "add" -> addToServerList(command[1], command[2]);
                            case "list" -> returnServerList();
                            default -> throw new IllegalStateException("Unexpected value: " + command[0]);
                        };
                        System.out.println(serverList);

                        System.out.println("Message to client: \t" + messageToClient);
                        outFromDNS.writeBytes(messageToClient + "\n");
                        if (command[0].equals("connect")) break;
                    }
                    System.out.println("\u001B[33m" + "Client left connection..."+ "\u001B[0m");
                    connectionSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    public static String getIpAddress(String domain){
        return serverList.get(domain);
    }

    public static String addToServerList(String domain, String ip_address){
        serverList.put(domain, ip_address);
        return domain + " " + ip_address;
    }

    public static String returnServerList(){
        return serverList.toString();
    }
}
