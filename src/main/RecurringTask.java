package main;

import org.json.JSONObject;

/**
 *
 */
public class RecurringTask extends Task {
	public static String[] types = { "Class", "Study", "Sleep", "Exercise", "Work", "Meal" };
	private int endDate;
	private int startDate;
	private int frequency;

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 * @param end
	 * @param start
	 * @param freq
	 * @throws Exception 
	 */
	public RecurringTask(String name, float startTime, float duration, int date, String tname, int end, int start, int freq) throws Exception {
		super(name, startTime, duration, date, tname, Task.TaskType.RECURRING);
		this.endDate = end;
		this.startDate = start;
		this.frequency = freq;
	}

	public RecurringTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.RECURRING);
		setTypeName(typeName);
		// TODO get EndDate, StartDate, and Frequency from user using handler.
	}

	
	///////////////////////////// UserHandler Setters /////////////////////////////

	public void setEndDate(UserHandler handler) {
		
	}

	public void setStartDate(UserHandler handler) {
		
	}

	public void setFrequency(UserHandler handler) {
		
	}
	
	
	///////////////////////////// Code Setters /////////////////////////////
	
	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
	///////////////////////////// Getters /////////////////////////////
	
	public int getEndDate() {
	return endDate;
	}
	
	public int getStartDate() {
	return startDate;
	}
	
	public int getFrequency() {
	return frequency;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject jObj = new JSONObject();
		jObj.put("Name", getName());
		jObj.put("StartTime", getStartTime());
		jObj.put("Date", getDate());
		jObj.put("Duration", getDuration());
		jObj.put("Type", getTypeName());
		jObj.put("EndDate", getEndDate());
		jObj.put("StartDate", getStartDate());
		jObj.put("Frequency", getFrequency());
		return jObj;
	}
	
	@Override
	public void print() {
		System.out.println("Name: " + getName() + 
							"\n Type: " + getTypeName() + 
							"\n Start Date: " + dateToHumanReadable(getStartDate()) + 
							"\n Start Time: " + timeToHumanReadable(getStartTime()) +
							"\n Duration: " + durationToHumanReadable(getDuration()) +
							"\n End Date: " + dateToHumanReadable(getEndDate()) + 
							"\n Frequency: " + getFrequency());
	}

}
