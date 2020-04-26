import java.util.ArrayList;
import java.lang.Math;

public class RSAAlg {//generates a public and private RSA key, can be used to encrypt

    public int pOne;// generated inital prime 1, kept private
    public int pTwo;// generated inital prime 2, kept private
    public int phi;// generated from pOne & pTwo, kept private

    public int n;// generated from pOne & pTwo, encrypt and decrypt key, kept public
    public int d;// generated from pOne & pTwo, decrypt key, kept private
    public int e;// generated from phi & d, encryption key, kept public

    public int testCounter = 0;
    public int testCounterOne = 0;

    public void run(String initM) {
        ArrayList<Integer> convertedChars = new ArrayList<Integer>();
        ArrayList<Integer> encryptedChars = new ArrayList<Integer>();
        ArrayList<Integer> decryptedChars = new ArrayList<Integer>();

        System.out.println("Inital message: " + initM);// the inital message
        convertedChars = convertTextToInts(initM);
        //System.out.println("Converted to ints: " + convertedChars + "\n");// the inital message into a list of ints


        for (int i = 0; i < convertedChars.size(); i++) {
            encryptedChars.add(encryptChar(convertedChars.get(i)));
        }
        System.out.println("Encrypted ints:" + encryptedChars + "\n");// the list of ints encrypted

        /*
        String translateEncrypted = convertIntsToText(encryptedChars);
        System.out.println("Attempted convertion: " + translateEncrypted + "\n");//if you try to translate the encrypted message
        */

        for (int i = 0; i < encryptedChars.size(); i++) {
            decryptedChars.add(decryptChar(encryptedChars.get(i)));
        }
        System.out.println("Decrypted ints: " + decryptedChars + "\n");// list of encrypted ints decrypted

        String decryptedMessage = convertIntsToText(decryptedChars);
        System.out.println("Decrypted message: " + decryptedMessage);// final message


        if (initM.equals(decryptedMessage)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILURE");
            testCounter++;
        }
    }

    // generates the init numbers
    // sets values for pOne, pTwo, n, phi, d, and e
    public void genNums(int upBound) {
        ArrayList<Integer> primes = createListOfPrimes(10, upBound);
        //System.out.println(primes + " " + primes.size());
        pOne = randomPrime(primes, 0);
        pTwo = randomPrime(primes, pOne);
        n = pOne * pTwo;
        phi = (pOne - 1) * (pTwo - 1);
        setDAndE(upBound);
    }


    // create a list of prime numbers bounded by parameters
    // can be changed to a list of primes instead of generating new ones each time
    // for effeciency
    public ArrayList<Integer> createListOfPrimes(int lowBound, int upBound) {
        ArrayList<Integer> returned = new ArrayList<Integer>();
        for (int i = lowBound; i <= upBound; i++) {
            if (checkPrime(i)) {
                returned.add(i);
            }
        }
        return returned;
    }

    // gets a random prime number from the list of primes,
    // so long it is not equal to parameter
    public int randomPrime(ArrayList<Integer> selection, int not) {
        double random = Math.random() * (selection.size() - 1);
        int index = (int) random;
        int n = selection.get(index).intValue();
        if (n == not) {
            return randomPrime(selection, not);
        }else {
            return n;
        }
    }

    // checks if n is prime using wilson's theorum
    // n is a prime if satisfies ((n-1)! + 1 % n) = 0;
    // simple primality test
    public boolean checkPrime(int n) {
        if (n <= 3) {
            return n > 1;// 1,2,3 are primes, also check # < 0
        } else if (n % 2 == 0 || n % 3 == 0) {// if divisable by 2 or 3
            return false;
        }
        int i = 5;
        while (i * i <= n) {//check for squares and multiples of 5
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
            i = i + 6;
        }
        return true;
    }

    public void setDAndE(int upBound) {
        d = getD(upBound);
        e = getE(upBound);
        double tester = Math.log(n) / Math.log(2);
        if (e < tester || e < 0) {
            setDAndE(upBound);
        }
    }

    // d any prime number above max(p,q)
    public int getD(int upBound) {
        int max = Math.max(pOne, pTwo);
        ArrayList<Integer> range = createListOfPrimes(max, (max + upBound));
        return randomPrime(range, max);
    }

    // extended euclidian algorithm to find d
    // find gcd(phi,d)
    // gcd(phi,d) = x(sub k-1) when x(sub k) = 0

    // solve e for (d⋅e) % phi = 1
    public int getE(int upBound) {
        ArrayList<Integer> listOfX = new ArrayList<Integer>();
        listOfX.add(phi);
        listOfX.add(d);
        boolean notZero = true;
        int counter = 2;
        while (notZero) {
            int xi = (listOfX.get(counter - 2)) % (listOfX.get(counter - 1));
            if (xi == 0) {
                notZero = false;
            } else {
                listOfX.add(xi);
                counter++;
            }
        }
        if (listOfX.get(listOfX.size() - 1) != 1) {
            return -1; // e failed, so choose another d
        }
        // if xi = 1, calculate a and b for xi
        // equation: xi=a(phi) + b(d)
        // (1-(a*phi))/d = b
        double a = 0.0;
        double b = 0.0;
        int check = 0;
        for (int i = 0; i > (-10 * upBound); i--) {
            a = (double) i;
            b = (listOfX.get(listOfX.size() - 1) - (a * phi)) / d;
            check = (int) b;
            if (b == check) {
                return check;
            }
        }
        return -1;// e failed so choose another d
    }

    // prints the numbers generated
    public void printAllNums() {
        System.out.println("p: " + pOne + " q: " + pTwo);
        System.out.println("phi: " + phi + " n: " + n);
        System.out.println("d: " + d + " e: " + e);
    }

    // converts the string message into an int that can be encrypted
    // the message has to be scaled from 0 to n-1 so that the number representation
    // is under n
    public ArrayList<Integer> convertTextToInts(String message) {
        ArrayList<Integer> convertedParsedMessage = new ArrayList<Integer>();
        for (int i = 0; i < message.length(); i++) {
            int num = -1 * (message.charAt(i) - (n - 1));// retuns the ASCII num for the char
            convertedParsedMessage.add(num);
        }
        return convertedParsedMessage;
    }

    // converts the int message into an string that can be read
    // the int message has to be scaled from 0 to n-1 so that the string is
    // comprehensable
    public String convertIntsToText(ArrayList<Integer> nums) {
        String output = "";
        for (int i = 0; i < nums.size(); i++) {
            int toInt = -1 * (nums.get(i)) + (n - 1);
            char newChar = (char) toInt;
            output += newChar;
        }
        return output;
    }

    // encrypts m that represents one character using e and n
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

    // decrypt m that represents one encrypted character using these d and n
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