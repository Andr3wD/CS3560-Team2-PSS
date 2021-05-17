package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class DataFile {

	/**
	 * 
	 * @param handler
	 * @return
	 */
	public static ArrayList<Task> load(String filePath) {
		ArrayList<Task> output = new ArrayList<>();

		try {
			File file = new File(filePath); // Open file at filePath.
			FileInputStream fStream = new FileInputStream(file); // Open inputStream for file.

			// Convert file to String.
			String JSONString = new String(fStream.readAllBytes());
			// Convert String to JSONArray.
			JSONArray arr = new JSONArray(JSONString);

			// Parse through all objects in JSONArray and try to create the Tasks and save them to the output.
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jobj = arr.getJSONObject(i);
				String type = jobj.getString("Type");

				Task newTask; // Declare shell task.
				if (PSS.isIn(TransientTask.types, type)) {

					newTask = new TransientTask(jobj.getString("Name"), jobj.getFloat("StartTime"),
							jobj.getFloat("Duration"), jobj.getInt("Date"), type);

				} else if (PSS.isIn(RecurringTask.types, type)) {

					newTask = new RecurringTask(jobj.getString("Name"), jobj.getFloat("StartTime"),
							jobj.getFloat("Duration"), jobj.getInt("StartDate"), type, jobj.getInt("EndDate"),
							jobj.getInt("Frequency"));

				} else if (PSS.isIn(AntiTask.types, type)) {

					newTask = new AntiTask(jobj.getString("Name"), jobj.getFloat("StartTime"),
							jobj.getFloat("Duration"), jobj.getInt("Date"), type);

				} else {
					System.out.println("Schedule JSON file doesn't follow format!");
					fStream.close();
					return null;
				}
				output.add(newTask);
				System.out.println("Task: " + newTask.getName() + " has been loaded into memory.");
			}

			fStream.close();
		} catch (FileNotFoundException e) {
			// filePath couldn't be found!
			System.out.println("Could not find file path to save to!");
			return null;
		} catch (IOException e) {
			// Problem closing the file stream.
			e.printStackTrace();
		} catch (JSONException e) {
			// Problem creating JSONArray from String or problem getting JSONObject from JSONArray.
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(
					"Schedule JSON file doesn't follow format, or there was another problem loading the schedule.");
			System.out.println(e.getMessage());
			return null;
		}

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
	 * @return boolean, if the save was successful or not.
	 * @throws Exception 
	 */
	public static void save(ArrayList<Task> data, String filePath) throws Exception {
		try {
			File file = new File(filePath); // Open file at filePath.
			FileOutputStream fStream = new FileOutputStream(file); // Make an output stream to the file.

			// Make JSON array, and fill it with JSONObjects for all Tasks.
			JSONArray jArr = new JSONArray();
			for (Task task : data) {
				jArr.put(task.toJson());
			}

			// Write JSON array string representation to the filePath.
			fStream.write(jArr.toString().getBytes());

			fStream.close();
		} catch (FileNotFoundException e) {
			// filePath couldn't be found!
			throw new Exception("ERR: Could not find file to save to!");
		} catch (IOException e) {
			// Problem writing jArr to file!
			throw new Exception("ERR: Problem with saving to file!");
		}

	}
}
