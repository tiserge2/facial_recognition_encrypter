package encryption;

import java.io.File;
import java.io.IOException;
import compression.*;

public class Encrypter {
	ZipFolder zipFolder;
	String ouputName = "";
	
	public String getOuputName() {
		return ouputName;
	}

	public void setOuputName(String fullPathToFolder) {
		this.ouputName = fullPathToFolder + ".sos";
	}

	public Encrypter() {
		//let's have a reference of the zipFolder
		//into memory
		System.out.println("constructor");
	}
	
	public boolean encryptFile(String folderName, String tarredName, String key, String folderPath) {
		//to encrypt a folder, we need to have the tarred filed from the folder
		//in argument will be passed the tarred file of the related folder
		//also a way is developed to keep the same folder name when 
		//encrytion to add the extension to the encrypted file
		System.out.println("Inside encryption");
		this.setOuputName(folderName);
		String fullPath = folderPath ;
		String outputName = this.getOuputName();
		
		File inputFile = new File(folderPath + "/" + tarredName);
		File encryptedFile = new File(folderPath + "/" + outputName);
		
		System.out.println(folderPath + "/" + tarredName);
		System.out.println(folderPath + "/" + outputName);
		
		ZipFolder zip = new ZipFolder();
		System.out.println("start zipping");
		
		String firstPath = System.getProperty("user.dir");
		System.out.println("Strarting path: " + firstPath);
		System.setProperty("user.dir",folderPath);
		System.out.println("New folder path: " + System.getProperty("user.dir"));
		System.out.println("Full path to folder: " + fullPath);
		try {
			zip.startZippingFolder(folderPath, folderName, tarredName);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("start encryption");
		try {
			CryptoUtils.encrypt(key, inputFile, encryptedFile, folderPath);
			System.out.println("The file: " + folderName + " has been encrypted.");	
			try {
				zip.deleteFile(fullPath, tarredName);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} catch(CryptoException ex) {
			System.out.println(ex.getMessage());
            ex.printStackTrace();
		}
		return false;
	}
	
	public boolean decryptFile(String pathToEncryptedFile, String pathToTarredFolder, String key, String folderPath) {
		System.out.println("Inside decryption");
		File encryptedFile = new File(folderPath + "/" + pathToEncryptedFile);
		File decryptedFile = new File(folderPath + "/" + pathToTarredFolder);
		ZipFolder zip = new ZipFolder();
		
		String firstPath = System.getProperty("user.dir");
		System.setProperty("user.dir", folderPath);
		try {
			CryptoUtils.decrypt(key, encryptedFile, decryptedFile, folderPath);
			System.out.println("The file has: " + pathToEncryptedFile + " been decrypted.");
			try {
				zip.startUnzippingFolder(folderPath, pathToTarredFolder);
				try {
					zip.deleteFile(folderPath, pathToEncryptedFile);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} catch(CryptoException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		System.setProperty("user.dir",firstPath);
		return false;
	}
	
	public static void main(String argv[]) {
		Encrypter encr = new Encrypter();
		String pathTo = "/home/tch10tch10/Documents";
		String folderName = "ulcer";
		String tarred = "ulcer.tar.gz";
		String encrypted = "ulcer.sos";
		String key = "Mary has one Sis";
		
		
//		encr.encryptFile(folderName, tarred, key, pathTo);
		encr.decryptFile(encrypted, tarred, key, pathTo);
//		System.out.println("Working directory: " + System.getProperty("user.dir"));
//		System.setProperty("user.dir", "/home/tch10tch10/");
//		System.out.println("New Working directory: " + System.getProperty("user.dir"));
	}
}
