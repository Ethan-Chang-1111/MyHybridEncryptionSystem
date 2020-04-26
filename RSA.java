import java.util.ArrayList;
import java.lang.Math;
public class RSA{//uses given keys to encrypt
    public int n;// both, encrypt and decrypt key, kept public
    public int d;// priv, decrypt key, kept private
    public int e;// publ, encrypt key, kept public

    // encrypts a string (msg) and returns an encrypted string
    public String encrypt(int pubKey, int n, String msg){
        e = pubKey;
        this.n = n;

        ArrayList<Integer> intRep = stringToAscii(msg);
        ArrayList<Integer> encrypIntRep = new ArrayList<Integer>();
        for(int i = 0; i<intRep.size(); i++){
            encrypIntRep.add(i, encryptChar(intRep.get(i)));
        }
        return addXes(encrypIntRep);
    }
    
    // encrypts an int (msg) and returns an encrypted int
    public int encrypt(int pubKey, int n, int msg){
        e = pubKey;
        this.n = n;

        return encryptChar(msg);
    }

    // decrypts a string(encryptMsg) and returns a decrypted string
    public String decrypt(int privKey, int n, String encryptMsg){
        d = privKey;
        this.n = n;
        ArrayList<Integer> intRep = removeXes(encryptMsg);
        ArrayList<Integer> output = new ArrayList<Integer>();
        for(int i = 0; i<intRep.size(); i++){
            output.add(decryptChar(intRep.get(i)));
        }

        String finalMsg = asciiToString(output);
        //use decryptChar
        return finalMsg;
    }

    // decrypts an int (msg) and returns a decrypted int
    public int decrypt(int privKey, int n, int msg){
        d = privKey;
        this.n = n;

        return decryptChar(msg);
    }

    // converts previous ArrayList output to a String output
    public String addXes(ArrayList<Integer> encryptedMsg){
        String finalMsg = "";
        for(int i = 0; i< encryptedMsg.size();i++){
            finalMsg += encryptedMsg.get(i).toString();
            finalMsg += "x";
        }
        return finalMsg;
    }

    // converts String output to ArrayList to be processed
    public ArrayList<Integer> removeXes(String encryptMsg){
        int start = 0;
        int end = 0;
        ArrayList<Integer> output = new ArrayList<Integer>();
        for(int i = 0; i<encryptMsg.length();i++){
            String test = encryptMsg.substring(i, i+1);
            if(test.equals("x")){
                end = i;
                int temp = Integer.valueOf(encryptMsg.substring(start, end));
                output.add(temp);
                start = i+1;
            }
        }
        return output;
    }

    // converts the string message into an int that can be encrypted
    public ArrayList<Integer> stringToAscii(String message) {
        ArrayList<Integer> convertedParsedMessage = new ArrayList<Integer>();
        for (int i = 0; i < message.length(); i++) {
            int num = -1 * (message.charAt(i) - (n - 1));// retuns the ASCII num for the char
            convertedParsedMessage.add(num);
        }
        return convertedParsedMessage;
    }

    // converts the int message into a string that can be read
    public String asciiToString(ArrayList<Integer> nums) {
        String output = "";
        for (int i = 0; i < nums.size(); i++) {
            int toInt = -1 * (nums.get(i)) + (n - 1);
            char newChar = (char) toInt;
            output += newChar;
        }
        return output;
    }

    // encrypts (message) which represents one character
    public int encryptChar(int message) {
        String binary = Integer.toBinaryString(e);
        String[] num = binary.split("");
        int[] binaryList = new int[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            binaryList[i] = Integer.parseInt(num[i]);
        }

        double newNum = 1.0;
        for (int i = 0; i < binaryList.length; i++) {
            newNum = (Math.pow(newNum, 2.0)) % n;
            if (binaryList[i] == 1) {
                newNum = (newNum * (double) message) % (double) n;
            }
        }
        return (int) newNum;
    }

    // decrypt (message) which represents one encrypted character
    public int decryptChar(int message) {
        String binary = Integer.toBinaryString(d);
        String[] num = binary.split("");
        int[] binaryList = new int[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            binaryList[i] = Integer.parseInt(num[i]);
        }

        double newNum = 1.0;
        for (int i = 0; i < binaryList.length; i++) {
            newNum = (Math.pow(newNum, 2.0)) % n;
            if (binaryList[i] == 1) {
                newNum = (newNum * (double) message) % (double) n;
            }
        }
        return (int) newNum;
    }
}