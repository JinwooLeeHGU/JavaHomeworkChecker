package edu.handong.csee.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class JavaHomeworkChecker {
	static ArrayList<String> outputParameters = new ArrayList<String>(); // 나중에 method로 나누면서 정리하기 
	static String unpassSavedPath;
	public static void main(String[] args) throws Exception {
		unpassSavedPath  =args[0];
		// Read input file 
		ArrayList<String> inputParameters = new ArrayList<String>();
		File inputFile = new File(args[1]);
        FileReader inputFileReader = new FileReader(inputFile);
        BufferedReader inputBufReader = new BufferedReader(inputFileReader);
        String line = "";
        while((line = inputBufReader.readLine()) != null){
        		inputParameters.add(line);
        }
        //.readLine()은 끝에 개행문자를 읽지 않는다.            
        inputBufReader.close();
		
        //Read output file

		File outputFile = new File(args[2]);
        FileReader outputFileReader = new FileReader(outputFile);
        BufferedReader outputBufReader = new BufferedReader(outputFileReader);
        line = "";
        while((line = outputBufReader.readLine()) != null){
        		outputParameters.add(line);
        }        
        inputBufReader.close();
        
        //Read class file list
		ArrayList<String> classFileList = new ArrayList<String>();
		File classFile = new File(args[3]);
        FileReader classFileReader = new FileReader(classFile);
        BufferedReader classBufReader = new BufferedReader(classFileReader);
        line = "";
        while((line = classBufReader.readLine()) != null){
        		classFileList.add(line);
        }        
        classBufReader.close();
        
		// Read student path list
		File studentPathListfile = new File(args[4]);
        FileReader studentPathListfilereader = new FileReader(studentPathListfile);
        BufferedReader studentPathListbufReader = new BufferedReader(studentPathListfilereader);
        String studentPath = "";
        String javacCommand = "";
        String javaCommand = "";	
        int idx = 0;
        
        //Execute hw program
        while((studentPath = studentPathListbufReader.readLine()) != null){
        	javacCommand = "javac -cp  "+cutClasspath(studentPath) +" "+ studentPath; //if we execute this code, this code will modify original class file.
            System.out.println(javacCommand);
            runJavacProcess(javacCommand);
        	
        	javaCommand = "java -cp " + classFileList.get(idx) + " "+classFileList.get(++idx)+" " + inputParameters.get(0) + " " + inputParameters.get(1);
            System.out.println(javaCommand);
            runJavaProcess(javaCommand);
        }           
        studentPathListbufReader.close();
        
        

//		txt file read -> 한줄 한줄 읽어와서 AbsSrcPath에 넣기 
//		String AbsSrcPath = "/Users/eunjiwon/Desktop/HW2/edu/handong/csee/java/hw2/CalculatorForFourArithmeticOperators.java /Users/eunjiwon/Desktop/HW2/edu/handong/csee/java/hw2/Calculator.java";
//		String javacCommand = "javac -d . " + AbsSrcPath;
		
//		runProcess(javacCommand);
//		runProcess("java edu/handong/csee/java/hw2/CalculatorForFourArithmeticOperators 12 12");
	}
	
	private static String cutClasspath(String classdir)
	{
		String[] classdirs = classdir.split(" ");
		int lastslash = classdirs[0].lastIndexOf('\\');
		classdirs[0]=classdirs[0].substring(0, lastslash-1);
//		System.out.println(classdirs[0]);
		return classdirs[0];
		
	}
	private static void check(InputStream ins, String command) throws Exception {
		int idx = 0;
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
    			System.out.println(line);
            if(!line.equals(outputParameters.get(idx++))) {
            	System.out.println("unpassed");
            		// string 넘기기  
            		StoreUnpassed(command, unpassSavedPath);
            		break;
            }
        }
    }
    private static void runJavacProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
    }
    
    
    
    private static void runJavaProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        check(pro.getInputStream(), command);
        check(pro.getErrorStream(), command);
        pro.waitFor();
        if(pro.exitValue() == 1) {
        		//string 넘기기 
        		StoreUnpassed(command, unpassSavedPath);
        }
        
//        System.out.println(command + " exitValue() " + pro.exitValue());
        
     }
    
    public static void StoreUnpassed(String drivepath,String path) throws IOException{
         FileWriter file = new FileWriter(path+"/unpassed.csv", true); 
         PrintWriter print_line = new PrintWriter( file );
         print_line.printf( "%s" + "%n" , drivepath);
          print_line.close();
    }
}