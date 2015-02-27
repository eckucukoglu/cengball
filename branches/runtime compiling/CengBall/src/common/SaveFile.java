package common;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class SaveFile {
	
	private Metadata metadata;
	private ArrayList<Percept> perceptList;
	private String saveFilePath;
    
	public SaveFile(String saveFilePath) {
		this.saveFilePath = saveFilePath;
		this.perceptList = new ArrayList<>();
	}
	
	public SaveFile(String saveFilePath, ArrayList<Percept> perceptList, Metadata metadata) {
		this.saveFilePath = saveFilePath;
		this.perceptList = perceptList;
		this.metadata = metadata;
	}
	
	public ArrayList<Percept> getPerceptList() {
		return perceptList;
	}
	
	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void saveToFile() {
		//PerceptWriter writer = new PerceptWriter(getPerceptList(), getSaveFilePath());
		//writer.write();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(getSaveFilePath())));
			writer.write(gson.toJson(this));
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
	
	public static SaveFile getFromFile(String filePath) {
		//PerceptParser parser = new PerceptParser(filePath);
		//parser.parse();
		SaveFile savefile = null;
		Gson gson = new Gson();
		
		try {
			savefile = gson.fromJson(new BufferedReader(new FileReader(new File(filePath))), SaveFile.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return savefile;
		//return new SaveFile(filePath, parser.getPerceptList());
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
}
