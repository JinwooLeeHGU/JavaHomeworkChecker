
import java.io.*;


public class GettingFileName {
	static WriteTxt txt1;
	static WriteTxt txt2;

public static void main(String[] args) throws FileNotFoundException{
	txt1 = new WriteTxt("SECTIONJAVA.txt");
	getJavaFileName("C:\\Users\\À±È£\\eclipse-workspace\\Vehicle");
	txt2 = new WriteTxt("SECTIONCLASS.txt");
	getClassFileName("C:\\Users\\À±È£\\eclipse-workspace\\Vehicle");
	
}

public static void getJavaFileName(String path) {
	File folder = new File(path);
	File[] listOfFiles = folder.listFiles();
	for (int i = 0; i < listOfFiles.length; i++) {
		if (listOfFiles[i].isDirectory()) {
			  path = path + listOfFiles[i].getName();
			  getJavaFileName(listOfFiles[i].toString());
			} 
		if (i+1 < listOfFiles.length && listOfFiles[i].isFile() && listOfFiles[i].getName().matches("(.*)java") 
				&& listOfFiles[i+1].getName().matches("(.*)java")) {
			  try {
				txt1.writeToFile(path+ "\\" +listOfFiles[i].getName(), path+ "\\" +listOfFiles[i+1].getName());
			} catch (IOException e) {
				e.printStackTrace();
						}
			 		}
			}
	 }


public static void getClassFileName(String path) {
	File folder = new File(path);
	File[] listOfFiles = folder.listFiles();
	
	for (int i = 0; i < listOfFiles.length; i++) {
		
		if (listOfFiles[i].isDirectory()) {
			  path = path + listOfFiles[i].getName();
			  getClassFileName(listOfFiles[i].toString());
			} 
		
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().matches("(.*)class")) {
			// System.out.println((path+ "\\" +listOfFiles[i].getName()));
			  try { 
				txt2.writeToFile((path+ "\\" +listOfFiles[i].getName()).replace(".class", ""));
			} catch (IOException e) {
				e.printStackTrace();
				}
			}
		}

	}

}