package edu.handong.csee.java.datamodel;

public class Student {
	String name;
	String id;
	String github;
	boolean evalateOnRecentOne;
	
	public Student(String line, String projectName) {
		String[] values = line.split("\t"); // 0: s_id, 1: name, 2: github
		
		name = values[1];
		id = values[0];
		github = getOnlyGithubURL(values[2],projectName);
		
		if(values.length > 3 && values[3].trim().equals("Y"))
			evalateOnRecentOne = true;
		else
			evalateOnRecentOne = false;
	}
	
	private String getOnlyGithubURL(String url, String projectName) {
		// https://github.com/choisaeam/HGUCoursePatternAnalyzer.git
		// --> https://github.com/choisaeam
		
		if(url.contains(projectName))
			return url.substring(0,url.lastIndexOf(projectName));
		
		return url.endsWith("/") || url.length()==1? url:url + "/";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGithub() {
		return github;
	}

	public void setGithub(String github) {
		this.github = github;
	}
	
	public boolean getEvalateOnRecentOne() {
		return evalateOnRecentOne;
	}
}
