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
	String unpassSavedPath; // 0
	ArrayList<String> testInputList; // 1
	ArrayList<Output> outputList; // 2
	ArrayList<String> javaFileList; // 3
	String fullyQualifiedClassNameThatContainsMainMethod; // 4	
	ArrayList<String> studentPathList = new ArrayList<String>(); // 5
	String projectRootFolderName; // 6
	
	ArrayList<String> evaluationResults;

	/**
	 * @param args  0: csv파일 저장되는 경로,
	 *              1: test input 값이 저장된 파일, 
	 *              2: input에 대한 output값이 저장된 파일 
	 *              3: 컴파일 해야 할 파일의 목록 
	 *              4: 실행해야 할 fully qualified class name
	 *              5: 학생들 폴더 목록
	 *              6: 프로젝트 root 폴더 이름 e.g., HW2
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		JavaHomeworkChecker checker = new JavaHomeworkChecker();
		checker.run(args);

	}

	public void run(String[] args) throws Exception {

		// (1) Directory path where unpassed.csv file is saved.
		unpassSavedPath = args[0];

		// (2) Test input file name e.g., input.txt
		testInputList = readFile(args[1]);

		// (3) Read test output file. e.g., output.txt
		outputList = readOutputFile(args[2]);

		// (4) java file list for compile
		javaFileList = readFile(args[3]);

		// (5) Read class file list
		fullyQualifiedClassNameThatContainsMainMethod = readFile(args[4]).get(0);

		// (6) Read student path list
		studentPathList = readFile(args[5]);

		// (7) Project root folder name
		projectRootFolderName = readFile(args[6]).get(0);

		evaluationResults = new ArrayList<String>();
		executeProgram();
		
		saveResultsInCSVFile();	
	}
	
	ArrayList<String> readFile(String path) throws Exception{

		ArrayList<String> lines = new ArrayList<String>();

		File inputFile = new File(path);
		FileReader inputFileReader = new FileReader(inputFile);
		BufferedReader inputBufReader = new BufferedReader(inputFileReader);
		String line = "";
		while((line = inputBufReader.readLine()) != null){
			lines.add(line);
		}
		//.readLine()은 끝에 개행문자를 읽지 않는다.            
		inputBufReader.close();

		return lines;

	}
	
	ArrayList<Output> readOutputFile(String path) throws Exception{
		ArrayList<Output> outputList = new ArrayList<Output>();
		
		ArrayList<String> lines = readFile(path);
		ArrayList<Integer> ouputDividerIndice = new ArrayList<Integer>();
		
		for(int i=0; i < lines.size(); i++) {
			if(lines.get(i).startsWith("%%%%%")) {
				ouputDividerIndice.add(i);
			}
		}
		
		for(int i=0; i < ouputDividerIndice.size()-1; i++) {
			outputList.add(new Output(ouputDividerIndice.get(i),ouputDividerIndice.get(i+1),lines));
		}
		
		return outputList; 
	}

	void executeProgram() throws Exception{

		//Execute hw program
		for(String studentPath:studentPathList) {
			
			String classpath = studentPath + "/" + projectRootFolderName;
			String javaFiles = getJavaFiles(classpath);
			String javacCommand = "javac -encoding utf-8" + javaFiles; //if we execute this code, this code will modify original class file.
			
			System.out.println(javacCommand);
			runJavacProcess(javacCommand);

			for(int i=0; i<testInputList.size();i++) {
				String javaCommand = "java -cp " + classpath + " "+ 
						fullyQualifiedClassNameThatContainsMainMethod +" " + testInputList.get(i);
				System.out.println(javaCommand);
				runJavaProcess(javaCommand,i);
			}
		}
	}

	String getJavaFiles(String projectRoot) {
		String strJavaFiles = "";
		for(String javaFile : javaFileList) {
			strJavaFiles = strJavaFiles + " " +	projectRoot + "/" + javaFile;
		}

		return strJavaFiles;
	}

	private void check(InputStream ins, String command,int inputIndex) throws Exception {
		int idx = 0;
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		checker:
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			try {
				if(!line.equals(outputList.get(inputIndex).getOutput().get(idx))) {
					System.out.println("%%Expected line: " + outputList.get(inputIndex).getOutput().get(idx));
					//System.out.println("%%Actual line: " + );
					System.out.println("%%%%%%%%%%%%%%unpassed");
					storeUnpassed("0TESTCASE FAILED " + command);
					break checker;
				}
				idx++;
			} catch(IndexOutOfBoundsException e) {
				// got the correct line but there are unnecessary result lines
				// System.out.println("Exception: " + line);
				storeUnpassed("0TESTCASE minor issue unnecessary result lines " + command);
			}
			
		}
	}

	private void runJavacProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		
		pro.waitFor();
		if(pro.exitValue() >= 1) {
			storeUnpassed(pro.exitValue() + "COMPILE ERROR " + command);
			System.out.println("EXIT CODE " + pro.exitValue());
		}
		else
			System.out.println("Compile successful!! EXIT CODE=" + pro.exitValue());
	}

	private void runJavaProcess(String command,int inputIndex) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		check(pro.getInputStream(), command, inputIndex);
		check(pro.getErrorStream(), command, inputIndex);
		pro.waitFor();
		if(pro.exitValue() >= 1) {
			storeUnpassed(pro.exitValue()+ "TEST CASE FAILED " + command);
		}
	}

	public void storeUnpassed(String drivepath){
		
		evaluationResults.add(drivepath);
		
	}
	
	public void saveResultsInCSVFile()  throws IOException {
		FileWriter file = new FileWriter(unpassSavedPath+ File.separator + "unpassed.csv", false); 
		PrintWriter writer = new PrintWriter( file );
		
		for(String evaluationResult:evaluationResults) {
			writer.printf( "%s" + "%n" , evaluationResult);
		}
		
		writer.close();
	}
}