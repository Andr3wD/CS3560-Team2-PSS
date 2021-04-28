package main;

import org.json.JSONObject;

/**
 *
 * @author apedroza
 */
public class RecurringTask extends Task {
	protected int endDate;
	protected int startDate;
	protected int frequency;

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 * @param end
	 * @param start
	 * @param freq
	 */
	public RecurringTask(String name, float startTime, float duration, int date, Type tname, int end, int start, int freq) {
		super(name, startTime, duration, date, tname);
		this.endDate = end;
		this.startDate = start;
		this.frequency = freq;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject jObj = new JSONObject();
		jObj.put("Name", name);
		jObj.put("StartTime", startTime);
		jObj.put("Date", date);
		jObj.put("Duration", duration);
		jObj.put("Type", typeName.toString());
		jObj.put("EndDate", endDate);
		jObj.put("StartDate", startDate);
		jObj.put("Frequency", frequency);

		return jObj;
	}
}
