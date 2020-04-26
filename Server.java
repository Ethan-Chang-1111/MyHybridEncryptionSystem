
// A Java program for a Server 
import java.net.*;

import java.io.*;

public class Server {
    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    // constructor with port
    public Server(int port) {
        try {
            // starts server and waits for a connection
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");
            // takes input from the client socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            //starts Server-side program
            String line = "";
            String encryptedSymKey = "";
            boolean haveSymKey = false;
            // reads message from client until "Over" is sent
            while (!line.equals("Over:")) {
                try {
                    line = in.readUTF();
                    // System.out.println(line);
                } catch (IOException i) {
                    System.out.println(i);
                }
                
                // recieves the Client's key, only active once
                if (line.contains("SymKey:") && !haveSymKey) {
                    encryptedSymKey = line.substring(line.indexOf(":") + 1, line.length());
                    System.out.println("Client SymKey recieved");
                    haveSymKey = true;
                } else if (haveSymKey && line.contains("Send:")) {
                    String newMessage = line.substring(line.indexOf(":") + 1, line.length());
                    System.out.println("\n" + "New Message");
                    String temp = "";
                    try {
                        temp = decryptMsg(newMessage, encryptedSymKey);
                    } catch (IllegalArgumentException i) {}

                    if (temp != null) {//message was encrypted and successfully decrypted
                        System.out.println("Successfully Decrypted Message:" + temp);
                    }else{//message was not encrypted or failed to decrypt
                        System.out.println("Message was not encrypted or failed to decrypt");
                        System.out.println("Original Message:" + newMessage);
                    }
                } else if(line.equals("Over:")){
                    System.out.println("Connection prompted to end");
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000);

    }

    public static String decryptMsg(String msg, String key) {
        HybridEncrypt Alice = new HybridEncrypt();
        Alice.n = 44832091;
        Alice.publicKey = 16916903;
        Alice.privateKey = 59239;

        Alice.receiveMsg(msg, key);
        return Alice.finalMsg;
    }

}