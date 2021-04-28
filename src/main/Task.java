package main;

import org.json.JSONObject;

/**
 *
 * @author apedroza
 */
public class Task {
	protected String name; // Name of task
	protected float startTime;
	protected float duration;
	protected int date;

	protected Type typeName;

	// TODO LOOKAT there must be a better way to do this, giving the subclasses
	// their own enums and still having the ability for this class to reference the
	// enum for toJson()
	public enum Type {
		Class, Study, Sleep, Exercise, Work, Meal, Visit, Shopping, Appointment, Cancellation
	}

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 */
	public Task(String name, float startTime, float duration, int date, Type tname) {
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.date = date;
		this.typeName = tname;
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
}
