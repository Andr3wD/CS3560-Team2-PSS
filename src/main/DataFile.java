package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;

/**
 *
 * @author apedroza
 */
public class DataFile {

	/**
	 *
	 * @param filepath
	 * @return
	 */
	public static ArrayList<Task> load(String filepath) {
		ArrayList<Task> output = null;

		return output;
	}

	/**
	 *
	 * @param filepath
	 * @return
	 */
	public static boolean verify(String filepath) {
		return false;
	}

	/**
	 * Saves the given Task data to the given filePath in JSON format.
	 * 
	 * @param data
	 * @param filePath
	 */
	public static boolean save(ArrayList<Task> data, String filePath) {
		try {
			File file = new File(filePath);
			FileOutputStream fStream = new FileOutputStream(file); // Make an output stream to the file.
			
			JSONArray jArr = new JSONArray();
			for (Task task : data) {
				jArr.put(task.toJson());
			}
			
			fStream.write(jArr.toString().getBytes());
			fStream.close();
			
			return true;
		} catch (FileNotFoundException e) {
			// filePath couldn't be found!
			System.err.println("ERR: Could not find file to save to!");
			return false;
		} catch (IOException e) {
			// Problem writing jArr to file!
			System.err.println("ERR: Problem with saving to file!");
			return false;
		}

	}
}
