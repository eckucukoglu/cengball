package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class PerceptWriter {
	
	private ArrayList<Percept> perceptList = null;
	private File outputFile = null;
	
	public PerceptWriter(ArrayList<Percept> perceptList, String outputFile) {
		this.perceptList = perceptList;
		
		try {
			this.outputFile = new File(outputFile);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public String getJson() {
		// TODO might be inefficient to use with large lists. Check the performance!
		Gson gson = new Gson();
		
		return gson.toJson(perceptList);
	}
	
	public void write() {
		
		Gson gson = new Gson();
		BufferedWriter writer = null;
		String json = gson.toJson(perceptList);
		
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}