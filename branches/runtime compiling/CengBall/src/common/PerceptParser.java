package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * This class is used to parse a JSON file and
 * construct a list of percepts accordingly
 */
public class PerceptParser {
	
	private ArrayList<Percept> perceptList = null;
	private File inputFile = null;
	
	public PerceptParser(String filename) {
		try {
			this.inputFile = new File(filename);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public PerceptParser(File filename) {
		this.inputFile = filename;
	}
	
	public void parse() {
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Percept>>(){}.getType();
		
		try {
			perceptList = gson.fromJson(new BufferedReader(new FileReader(inputFile)), listType);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Percept> getPerceptList() {
		return perceptList;
	}
}