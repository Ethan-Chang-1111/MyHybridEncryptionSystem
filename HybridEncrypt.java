//import java.util.ArrayList;
//import java.lang.Math;
public class HybridEncrypt{
    //simulates correspondence between Alice and Bob
    /* -To send a messege from Alice to Bob-
    Bob generates a public and private RSA key
    Bob sends the public key to Alice
    
    Alice generates a AES key
    Alice encrypts her message with the AES key
    Alice encrypts her AES key with Bob's public key
    Alice sends her encrypted message and AES key

    Bob uses his private key to decrypt Alice's AES key
    Bob uses Alice's AES key to decrypt the message
    Bob can now read the message
    */

    int privateKey;
    int publicKey;
    int n;

    String encryptedMsg;
    String encryptedSymKey;

    String finalMsg;
    //prepares the RSA keys
    public void prep(int upbound){//used by receiver
        RSAAlg genKey = new RSAAlg();
        genKey.genNums(upbound);
        privateKey = genKey.d;//decrypt key-unlock key
        
        publicKey = genKey.e;//encrypt key-lock key
        n = genKey.n;//used in both, kept public

        System.out.println("Public Key: " + publicKey);
        System.out.println("N: " + n);
        
    }
    
    // encrypts the message and AESKey
    public void encryptMsg(String msg, String symKey, int pubKey, int n){//used by sender
        encryptedMsg = AES.encrypt(msg, symKey);//encrypt message with new symKey

        RSA RSAAlg = new RSA();
        encryptedSymKey = RSAAlg.encrypt(pubKey, n, symKey);//encrypt symKey with Bob's pubKey
    }

    // recieves and decrypts the encrypted message and key
    public void receiveMsg(String encryptMsg, String encryptSymKey){//used by receiver
        RSA RSAAlg = new RSA();
        String symKey = RSAAlg.decrypt(privateKey, n, encryptSymKey);//decrypt Alice's symKey with Bob's privKey

        finalMsg = AES.decrypt(encryptMsg, symKey);//decrypt Alice's message with Alice's newly decrypted symKey

        //read message
    }
}