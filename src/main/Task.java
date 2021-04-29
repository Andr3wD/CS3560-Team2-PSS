package main;

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
	
	
}
