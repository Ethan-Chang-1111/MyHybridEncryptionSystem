
// A Java program for a Client 
import java.net.*;
import java.io.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    //RSA public key
    private int n = 44832091;
    private int e = 16916903;

    // constructor to put ip address and port
    public Client(String address, int port, String userKey) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("\nConnected");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        // encrypt and sends Client's key with Servers encryption Key   
        String symKey = userKey;
        RSA RSAAlg = new RSA();
        String encryptedSymKey = "SymKey:" + RSAAlg.encrypt(e, n, symKey);
        try {
            out.writeUTF(encryptedSymKey);
        } catch (IOException i) {
            System.out.println(i);
        }
        
        // Starts Client-side user interface
        System.out.println("Type 'Help:' for help");
        
        String line = "";
        boolean repeat = true;
        String previousEncrypt = "";
        // keep reading until "Over" is input
        while (repeat) {
            try {
                line = input.readLine();
                if (line.contains("Encrypt:")) {
                    String message = line.substring(line.indexOf(":") + 1, line.length());
                    String temp = encryptMsg(message, symKey);
                    System.out.println("Encrypted the message: " + message + " to [" + temp + "]");
                    previousEncrypt = temp;
                } else if (line.contains("Send:")) {
                    out.writeUTF(line);
                } else if (line.contains("SendPrev:")) {
                    out.writeUTF("Send:" + previousEncrypt);
                } else if (line.equals("Help:")) {
                    System.out.println(
                            "\nCommands: \nHelp: - pulls up this text \nEncrypt: - Encrypts the text after the colon. The encrypted message is inside brackets [], but not including \nSend: - Sends the text after the colon to the server \nOver: - terminates the connection \nSendPrev: - Sends the last message that was passed through Encrypt:");
                    System.out.println(
                            "\nUse either unencrypted Entext or encrypted text to send a messag. In order to use encrypted text, use the 'Encrypt:' command to obtain an encrypted version of your message and use Send: to send it. In order to send unencrypted text, use the 'Send:' command to send text. You can use 'SendPrev:' to send the last encrypted message for ease of use.");
                } else if(line.equals("Over:")){
                    out.writeUTF("Over:");
                    repeat = false;
                }else{
                    System.out.println("Command not found");
                }
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000, "Hello Mr B");
    }

    public static String encryptMsg(String msg, String symKey) {
        HybridEncrypt Bob = new HybridEncrypt();
        int n = 44832091;
        int e = 16916903;
        Bob.encryptMsg(msg, symKey, e, n);
        return Bob.encryptedMsg;
    }

}