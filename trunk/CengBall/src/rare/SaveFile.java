package rare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import utils.CBLogger;


/**
 * This class represents the save file which keeps all of the information about a simulated match.
 *
 */
public class SaveFile {
	
	private Statistic stats;
	private Metadata metadata;
	private ArrayList<Percept> perceptList;
	
	private transient String saveFilePath;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
    
	/**
	 * Constructor for the SaveFile class with an empty percept list.
	 * @param saveFilePath path of the file.
	 */
	public SaveFile(String saveFilePath) {
		this.saveFilePath = saveFilePath;
		this.perceptList = new ArrayList<>();
	}
	
	/**
	 * Constructor for the SaveFile class 
	 * @param saveFilePath path of the save file.
	 * @param perceptList the list of the percepts in the match.
	 * @param metadata metadata of the match.(team names and colors.)
	 */
	public SaveFile(String saveFilePath, ArrayList<Percept> perceptList, Metadata metadata) {
		this.saveFilePath = saveFilePath;
		this.perceptList = perceptList;
		this.metadata = metadata;
	}
	
	/**
	 * Gets the percept list of the save file.
	 * @return a list of percepts.
	 */
	public ArrayList<Percept> getPerceptList() {

        return perceptList;
	}
	
	/**
	 * Gets the path of the save file.
	 * @return path of the save file.
	 */
	public String getSaveFilePath() {

        return saveFilePath;
	}

	/**
	 * This method writes the data to the file.
	 */
	public void saveToFile() {
		Gson gson = new GsonBuilder().create();
		
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(getSaveFilePath())));
			writer.write(gson.toJson(this));
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "IOException : " + e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "IOException : " + e);
			}
		}
	}

    /**
     * This method compresses then writes the data to the file.
     */
    public void saveCompressed() {
        Gson gson = new GsonBuilder().create();

        BufferedWriter writer = null;

        try {
            GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(new File(getSaveFilePath())));
            writer = new BufferedWriter(new OutputStreamWriter(gzip, "UTF-8"));
            writer.append(gson.toJson(this));
        } catch (IOException e) {
        	LOGGER.log(Level.WARNING, "IOException : " + e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            	LOGGER.log(Level.WARNING, "IOException : " + e);
            }
        }
    }

    /**
     * Gets the data from a compressed save file.
     * @param filePath path of the save file.
     * @return save file object of the given file.
     */
    public static SaveFile getFromCompressed(String filePath) {
        SaveFile savefile = null;
        Gson gson = new Gson();


        try {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(new File(filePath)));
            savefile = gson.fromJson(new BufferedReader(new InputStreamReader(gzip, "UTF-8")), SaveFile.class);
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
        	LOGGER.log(Level.WARNING, "Exception : " + e);
        }

        return savefile;
    }
	
	/**
	 * Gets the data from a save file.
	 * @param filePath path of the save file.
	 * @return save file object of the given file.
	 */
	public static SaveFile getFromFile(String filePath) {
		SaveFile savefile = null;
		Gson gson = new Gson();
		
		try {
			savefile = gson.fromJson(new BufferedReader(new FileReader(new File(filePath))), SaveFile.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			LOGGER.log(Level.WARNING, "Exception : " + e);
		}
		
		
		return savefile;
	}

	/**
	 * Gets thge metadata of the save file. 
	 * @return metadata object of the save file.
	 */
	public Metadata getMetadata() {

        return metadata;
	}

	/**
	 * Sets the percpet list of the save file.
	 * @param perceptList the list to be set.
	 */
	public void setPerceptList(ArrayList<Percept> perceptList) {

        this.perceptList = perceptList;
	}

	/**
	 * Sets the path of the save file.
	 * @param saveFilePath path of the save file.
	 */
	public void setSaveFilePath(String saveFilePath) {

        this.saveFilePath = saveFilePath;
	}

	/**
	 * Sets the meta data of the save file.
	 * @param metadata the metada object to be set.
	 */
	public void setMetadata(Metadata metadata) {

        this.metadata = metadata;
	}

	public Statistic getStats() {
		return stats;
	}

	public void setStats(Statistic stats) {
		this.stats = stats;
	}
}
