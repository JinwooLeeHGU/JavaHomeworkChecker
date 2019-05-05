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

import edu.handong.csee.java.datamodel.Student;


public class JavaHomeworkChecker {
	String unpassSavedPath; // 0
	ArrayList<String> testInputList; // 1
	ArrayList<Output> outputList; // 2
	ArrayList<String> javaFileList; // 3
	String fullyQualifiedClassNameThatContainsMainMethod; // 4	
	//ArrayList<String> studentPathList = new ArrayList<String>(); // 5
	ArrayList<Student> students; // 5
	String projectRootFolderName; // 6


	ArrayList<String> evaluationResults;

	/**
	 * @param args  0: csv파일 저장되는 경로,
	 *              1: test input 값이 저장된 파일, 
	 *              2: input에 대한 output값이 저장된 파일 
	 *              3: 컴파일 해야 할 파일의 목록 
	 *              4: 실행해야 할 fully qualified class name
	 *              5: 학생들 github주소
	 *              6: 프로젝트 root 폴더 이름 e.g., HW2
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		JavaHomeworkChecker checker = new JavaHomeworkChecker();
		checker.run(args);

	}

	public void run(String[] args) throws Exception {
		
		evaluationResults = new ArrayList<String>();

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

		// (7) Project root folder name. Need to get this info first
		projectRootFolderName = readFile(args[6]).get(0);

		// (6) Read student path list
		//studentPathList = readFile(args[5]);
		students = getStudentInfo(readFile(args[5]));

		executeProgram();

		saveResultsInCSVFile();	
	}

	private ArrayList<Student> getStudentInfo(ArrayList<String> lines) {

		ArrayList<Student> students = new ArrayList<Student>();

		for(String line:lines) {
			Student student = new Student(line, projectRootFolderName);
			if(!(student.getGithub().equals("-") || student.getGithub().isEmpty()))
				students.add(student);
			else
				evaluationResults.add("===No repository: " + student.getId() + " " + student.getName());
		}

		return students;
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
		for(Student student:students) {

			// check git repo is existing for the student.
			System.out.println("==== " + student.getId() + " " + student.getName() + " is evaluating!");
			gitClone(student);
			gradleBuild(student);

			String classpath = "git/" + student.getId() + "/" + projectRootFolderName +
					                   "/build/classes/java/main/";
			//String javaFiles = getJavaFiles(classpath);
			//String javacCommand = "javac -encoding utf-8" + javaFiles; //if we execute this code, this code will modify original class file.

			//System.out.println(javacCommand);
			//runJavacProcess(javacCommand);

			for(int i=0; i<testInputList.size();i++) {
				String javaCommand = "java -cp " + classpath + " "+ 
						fullyQualifiedClassNameThatContainsMainMethod +" " + testInputList.get(i);
				System.out.println(javaCommand);
				runJavaProcess(javaCommand,i);
			}
		}
	}

	private void gradleBuild(Student student) {
		System.out.println("Gradle build!! " + student.getId() + " " + student.getName());
		try {
			Process pro = Runtime.getRuntime().exec("gradle.bat build -p git" + File.separator + student.getId() + File.separator + projectRootFolderName);
			String line = null;
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			
			in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

			pro.waitFor();
			if(pro.exitValue() >= 1) {
				System.err.println("!!! " + pro.exitValue()+ " GRADLE BUILD FAILED " + student.getGithub() + " " + student.getName() + " " + student.getGithub() + projectRootFolderName);
			}
			
			pro.destroy();

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void gitClone(Student student) {

		// create git directory.
		File gitDir = new File("git");
		if(!gitDir.exists()) gitDir.mkdir();
		try {
			File studentGitPath = new File(gitDir + File.separator + student.getId() + File.separator + projectRootFolderName);

			if(!studentGitPath.exists()) {
				Process pro = Runtime.getRuntime().exec("git clone " + student.getGithub() + projectRootFolderName  + " " +
						"git" + File.separator + 
						student.getId() + File.separator + 
						projectRootFolderName);
				
				String line = null;
				
				BufferedReader in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
				
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}

				pro.waitFor();
				if(pro.exitValue() >= 1) {
					System.err.println("!!!" + pro.exitValue()+ " GIT CLONE FAILED " + student.getGithub() + " " + student.getName() + " " + student.getGithub() + projectRootFolderName);
				}
				
				pro.destroy();
			} else
				System.out.println("Git already cloned!");

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
						storeUnpassed("!!! 0 TEST CASE FAILED " + command);
						break checker;
					}
					idx++;
				} catch(IndexOutOfBoundsException e) {
					// got the correct line but there are unnecessary result lines
					// System.out.println("Exception: " + line);
					storeUnpassed("!!! 0 TEST CASE minor issue unnecessary result lines " + command);
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
		
		pro.destroy();
	}

	private void runJavaProcess(String command,int inputIndex) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		check(pro.getInputStream(), command, inputIndex);
		check(pro.getErrorStream(), command, inputIndex);
		pro.waitFor();
		if(pro.exitValue() >= 1) {
			storeUnpassed("!!! " + pro.exitValue()+ " FAILED by error (e.g., compile error)" + command);
		}
		
		pro.destroy();
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