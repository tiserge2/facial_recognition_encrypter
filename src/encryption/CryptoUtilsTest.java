package encryption;

import java.io.File;

/**
 * A tester for the CryptoUtils class.
 * @author www.codejava.net
 *
 */
public class CryptoUtilsTest {
    public static void main(String[] args) {
    	System.out.println("Where we are: ");
        String key = "Mary has one Sis";
        File inputFile = new File("/home/tch10tch10/Documents/testLock/tarred.tar.gz");
        File encryptedFile = new File("/home/tch10tch10/Documents/testLock/document.encrypted");
        File decryptedFile = new File("/home/tch10tch10/Documents/testLock/document.tar.gz");
         
//        try {
////            CryptoUtils.encrypt(key, inputFile, encryptedFile);
////            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
//            System.out.println("Encrytion done");
//        } catch (CryptoException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
    }
}
