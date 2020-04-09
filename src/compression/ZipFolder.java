package compression;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ZipFolder {
	/*
	 * @pathToFolder is the absolute path to the folder
	 * where the interesting folder is excluding the folder
	 * @fullPathToFolder is the absolute path to the folder
	 * including the interesting folder
	 */
	private String pathToFolder = "";
	private String fullPathToFolder = "";
	private String fullPathToLastZippedFolder = "";


	public String getFullPathToLastZippedFolder() {
		return fullPathToLastZippedFolder;
	}

	public void setFullPathToLastZippedFolder(String fullPathToLastZippedFolder) {
		this.fullPathToLastZippedFolder = fullPathToLastZippedFolder;
	}

	public String getPathToFolder() {
		return pathToFolder;
	}

	public void setPathToFolder(String pathToFolder) {
		this.pathToFolder = pathToFolder;
	}

	public String getFullPathToFolder() {
		return fullPathToFolder;
	}

	public void setFullPathToFolder(String fullPathToFolder) {
		this.fullPathToFolder = fullPathToFolder;
	}

	public boolean verifyFolderExist(String fullFathToFolder, String folderName)  {
//		System.setProperty("user.dir", pathToFolder);
		System.out.println("Folder name: " + folderName);
		System.out.println("Path name: " + fullFathToFolder);
		File folder = new File(fullFathToFolder + "/" + folderName);
		return folder.exists() && folder.isDirectory();
	}
	
	public boolean verifyFileExist(String fullPathToFile, String fileName) {
		File file = new File(fullPathToFile + "/" + fileName);
		return file.exists() && file.isFile();
	}
	
	public boolean startZippingFolder(String pathToFolder, String folderName, String outputName) throws IOException, InterruptedException {
//		String outputName = fullPathToFolderToZip + ".tar.gz";
		String command = "tar -czvf " + outputName + " " + folderName;
		Runtime  r = Runtime.getRuntime();
		System.setProperty("user.dir", pathToFolder);
		System.out.println("Folder where zipping is done: " + System.getProperty("user.dir"));
		//we are going to verify if the folder exists
		//if not we are going to return false to the function call
		if(this.verifyFolderExist(pathToFolder, folderName)) {
			File dir = new File(pathToFolder);
			System.out.println("The folder " + pathToFolder + "/" + folderName + " exists.");
			Process p = r.exec(command, null, dir);
			final int returnCode = p.waitFor();
			
			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			while((line = is.readLine()) != null) {
				System.out.println("Zipping: " + line);
			}
			
			BufferedReader is2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line = is2.readLine()) != null) {
				System.out.println(line);
			}
			
			setFullPathToLastZippedFolder(outputName);
			
			//after zipping the folder we need to delete it
			//from disk
			if(this.deleteFolder(folderName, pathToFolder)) {
				System.out.println("Folder: " + folderName + ", deleted successfully");
			} else {
				System.out.println("Sorry Can't delete the folder: " + folderName);
			}
			
		} else {
			System.out.println("Sorry Folder: " + folderName + " doesn't exist.");
			return false;
		}
		
		return true;
	}
	
	public boolean startUnzippingFolder(String fullPathTofileToUnzip, String fileName) throws IOException, InterruptedException {
		String command = "tar -xzvf " + fileName;
		Runtime  r = Runtime.getRuntime();
		File dir = new File(fullPathTofileToUnzip);
		//verify if its a file and it exists 
		if(this.verifyFileExist(fullPathTofileToUnzip, fileName)) {
			Process p = r.exec(command, null, dir);
			final int returnCode = p.waitFor();
			
			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			while((line = is.readLine()) != null) {
				System.out.println("Unzipping: " + line);
			}
			
			BufferedReader is2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line = is2.readLine()) != null) {
				System.out.println(line);
			}
			
			//after unzipping the file we need to delete it
			//from disk
			if(this.deleteFile(fullPathTofileToUnzip, fileName)) {
				System.out.println("File: " + fileName + ", deleted successfully");
			} else {
				System.out.println("Sorry Can't delete the file: " + fileName);
			}
		} else {
			System.out.println("Sorry file: " + fileName + " doesn't exist.");
			return false;
		}
		return true;
	}
	
	public boolean deleteFolder(String folderName, String fullPathToFolder) throws IOException, InterruptedException {
		String command = "rm -rf " + folderName;
		Runtime  r = Runtime.getRuntime();
		System.setProperty("user.dir", pathToFolder);
		System.out.println("Start deleting folder");
		//verify if its a file and it exists 
		if(this.verifyFolderExist(fullPathToFolder, folderName)) {
			System.out.println("folder exists");
			System.out.println("Path deleting folder: " + fullPathToFolder);
			File dir = new File(fullPathToFolder);
			Process p = r.exec(command, null, dir);
			final int returnCode = p.waitFor();
			
			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			while((line = is.readLine()) != null) {
				System.out.println( line);
			}
			
			BufferedReader is2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line = is2.readLine()) != null) {
				System.out.println(line);
			}
		} else {
			System.out.println("folder doesnt exist");
			return false;
		}
		return true;
	}
	
	public boolean deleteFile(String fullPathToFile, String fileName) throws IOException, InterruptedException {
		String command = "rm " + fileName;
		Runtime  r = Runtime.getRuntime();
		File dir = new File(fullPathToFile);
		//verify if its a file and it exists 
		if(this.verifyFileExist(fullPathToFile, fileName)) {
			Process p = r.exec(command, null, dir);
			final int returnCode = p.waitFor();
			
			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			while((line = is.readLine()) != null) {
				System.out.println(line);
			}
			
			BufferedReader is2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line = is2.readLine()) != null) {
				System.out.println(line);
			}
		} else {
			return false;
		}
		return true;
	}

	public ZipFolder() {
		
	}
	
	public static void main(String[] argv) throws InterruptedException {
		ZipFolder zipFol = new ZipFolder(); 
		try {
//			zipFol.startZippingFolder("/home/tch10tch10/Desktop", "yourdoc", "yourdoc.tar.gz");
			zipFol.startUnzippingFolder("/home/tch10tch10/Desktop",  "yourdoc.tar.gz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}