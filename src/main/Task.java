package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

/**
 *
 * @author apedroza, Andrew
 */
public abstract class Task {
	private String name; // Name of task
	private float startTime;
	private float duration;
	private int date;
	private String typeName;
	private TaskType taskType;

	// TODO LOOKAT there must be a better way to do this, giving the subclasses
	// their own enums and still having the ability for this class to reference the
	// enum for toJson()
	@Deprecated
	public enum Type {
		Class, Study, Sleep, Exercise, Work, Meal, Visit, Shopping, Appointment, Cancellation
	}
	
	public enum TaskType {
		ANTI,
		RECURRING,
		TRANSIENT
	}

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 */
	public Task(String name, float startTime, float duration, int date, String tname, TaskType taskType) {
		// TODO validation in setters.
		setName(name);
		setStartTime(startTime);
		setDuration(duration);
		setDate(date);
		setTypeName(tname);
		setTaskType(taskType);
	}
	
	public Task(UserHandler handler) {
		// TODO validation on inputted data.
		System.out.println("Please enter a name:");
		setName(handler.getLine());
		System.out.println("Please enter a start time:");
		setStartTime(handler.getFloat());
		System.out.println("Please enter a duration:");
		setDuration(handler.getFloat());
		System.out.println("Please enter a start date:");
		setDate(handler.getInt());
	}
	
	/**
	 *
	 * @param start
	 * @param dur
	 * @return
	 */
	public boolean overlap(float start, float dur) {
		return false;
	}
	
	/**
	 * Method that translates the time from decimal
	 * to a human readable string based on the
	 * 12 hour AM/PM clock
	 *
	 * @param time of task to translate
	 * @return readable string
	 */
	public String timeToHumanReadable(float time) {
		double timeAsDouble = time;
		
		//If the float value is greater than 13, then 
		//adjust to PM values
		if(time >=13) {
			timeAsDouble = time % 12;
		}
		int hours = (int) timeAsDouble; //Variable to hold hour value of time
		double decimal = timeAsDouble - hours;
		if(hours == 0) {
			hours = 12; //If the time has a 0 for hour, or the midnight hour, then hours is 12
		}
		int minutes = (int) (decimal * 60); //Variable to translate decimal value to minutes
		String minutesAsString = Integer.toString(minutes); //Convert minutes to string for editing if needed
		if(minutesAsString.equals("0")) {				
			minutesAsString = "00"; //If the minutes for time is 0, then print 00 instead
		}
		//If the time is greater or equal to 12, then it is considered PM
		String readable = (hours + ":" + minutesAsString + " " + ((time>= 12) ? "PM" : "AM"));
		return readable;
	}
	
	/**
	 * Method that translates the duration from decimal
	 * to a human readable string with hours and minutes
	 * @param duration of task to translate
	 * @return readable string
	 */
	public String durationToHumanReadable(float duration) {
		int hours = (int) duration; //Store the hour value of the duration
		double decimal = duration - hours; 
		int minutes = (int) (decimal * 60); //Store the decimal value of duration as minutes
		String readable = (hours + " hours and " + minutes + " minutes");
		return readable;
	}
	
	/**
	 * Method that translates the date of the task
	 * to a human readable string in the format
	 * MM-DD-YYYY
	 * @param date of task to translate
	 * @return formattedDate in MM-DD-YYYY
	 */
	public String dateToHumanReadable(int date) {
		String stringDate = String.valueOf(date); //Store the date value as a string
		
		//Date is initially in form YYYYMMDD, so convert to MM-DD-YYYY
	    LocalDate convertedDate = LocalDate.parse(stringDate, DateTimeFormatter.BASIC_ISO_DATE);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	    String formattedDate = convertedDate.format(formatter);
	    return formattedDate;
	}

	public JSONObject toJson() {
		JSONObject jObj = new JSONObject();
		jObj.put("Name", name);
		jObj.put("StartTime", startTime);
		jObj.put("Date", date);
		jObj.put("Duration", duration);
		jObj.put("Type", typeName.toString());

		return jObj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getStartTime() {
		return startTime;
	}

	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	
	public void print() {
		System.out.println("Name: " + getName() + 
							"\n Type: " + getTypeName() + 
							"\n Date: " + dateToHumanReadable(getDate()) + 
							"\n Start Time: " + timeToHumanReadable(getStartTime()) +
							"\n Duration: " + durationToHumanReadable(getDuration()));
	}
	
	
}
