package edu.handong.csee.java;

import java.util.ArrayList;
import java.util.List;

public class Output {
	ArrayList<String> output;
	
	public Output(int startIndex, int nextStartIndex, ArrayList<String> output) {
		List<String> list = output.subList(startIndex+1,nextStartIndex);
		this.output = new ArrayList<String>(list.size());
		this.output.addAll(list);
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
}
