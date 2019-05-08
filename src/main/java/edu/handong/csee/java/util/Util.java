package edu.handong.csee.java.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Util {

	public static void writeAFile(String path,ArrayList<String> lines) {
		FileWriter file;
		try {
			file = new FileWriter(path, false);
			PrintWriter writer = new PrintWriter( file );

			for(String evaluationResult:lines) {
				writer.printf( "%s" + "%n" , evaluationResult);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
