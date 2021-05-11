package main;

import org.json.JSONObject;

/**
 *
 */
public class RecurringTask extends Task {
	public static String[] types = { "Class", "Study", "Sleep", "Exercise", "Work", "Meal" };
	private int endDate;
	// startDate is just the regular date.
	@Deprecated
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
	public RecurringTask(String name, float startTime, float duration, int date, String tname, int end, int freq) throws Exception {
		super(name, startTime, duration, date, tname, Task.TaskType.RECURRING);
		// Make sure the type matches what this Task can take.
		if (!PSS.isIn(types, tname)) {
			throw new Exception(String.format("Invalid type %s for Recurring Task.", tname));
		}
		this.endDate = end;
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

	@Deprecated
	public void setStartDate(int startDate) throws Exception {
		setDate(startDate);
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	///////////////////////////// Getters /////////////////////////////

	public int getEndDate() {
		return endDate;
	}

	@Deprecated
	public int getStartDate() {
		return getDate();
	}

	public int getFrequency() {
		return frequency;
	}

	@Override
	public JSONObject toJson() {
		JSONObject jObj = new JSONObject();
		jObj.put("Name", getName());
		jObj.put("StartTime", getStartTime());
		jObj.put("StartDate", getDate());
		jObj.put("Duration", getDuration());
		jObj.put("Type", getTypeName());
		jObj.put("EndDate", getEndDate());
		jObj.put("Frequency", getFrequency());
		return jObj;
	}

	@Override
	public void print() {
		System.out.println("Name: " + getName() + "\nType: " + getTypeName() + "\nStart Date: "
				+ dateToHumanReadable(getStartDate()) + "\nStart Time: " + timeToHumanReadable(getStartTime())
				+ "\nDuration: " + durationToHumanReadable(getDuration()) + "\nEnd Date: "
				+ dateToHumanReadable(getEndDate()) + "\nFrequency: " + getFrequency());
	}

}
