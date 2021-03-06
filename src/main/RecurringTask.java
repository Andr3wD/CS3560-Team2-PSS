package main;

import org.json.JSONObject;

/**
 *
 */
public class RecurringTask extends Task {
	public static String[] types = { "Class", "Study", "Sleep", "Exercise", "Work", "Meal" };
	private int endDate;
	private int frequency;

	/**
	 * Constructs a RecurringTask in a direct manner with errors for any input problems.
	 * @param name
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 * @param end
	 * @param freq
	 * @throws Exception
	 */
	public RecurringTask(String name, float startTime, float duration, int date, String tname, int end, int freq)
			throws Exception {
		super(name, startTime, duration, date, tname, Task.TaskType.RECURRING);
		// Make sure the type matches what this Task can take.
		if (!PSS.isIn(types, tname)) {
			throw new Exception(String.format("Invalid type %s for Recurring Task.", tname));
		}
		setEndDate(end);
		setFrequency(freq);
		// Check for overlap.
		PSS.newTaskOverLapCheckCode(this);
	}

	/**
	 * Constructs a RecurringTask with values obtained from the user, all while verifying them.
	 * @param handler
	 * @param typeName
	 */
	public RecurringTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.RECURRING);
		setTypeName(typeName);

		System.out.println("Please enter an end date: ");
		setEndDate(handler);

		System.out.println("Please enter frequency: ");
		setFrequency(handler);
	}

	///////////////////////////// UserHandler Setters /////////////////////////////

	/**
	 * Setter for the endDate variable.
	 * Verifies that endDate follows basic formatting rules (YYYYMMDD pattern).
	 * @param handler
	 */
	public void setEndDate(UserHandler handler) {
		int date = handler.getInt();
		boolean valid = false;
		String sDate = String.valueOf(date);
		int[] dayInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month;
		int day;
		int startDate = getDate();

		do {
			if (date > startDate) {

				//Check if date is correct Length for formatting
				if (sDate.length() == 8) {
					//Check if it has a valid month
					month = Integer.parseInt(sDate.substring(4, 6));
					if (month <= 12 && month >= 1) {
						//Check if day is valid
						day = Integer.parseInt(sDate.substring(6, 8));

						int lastDayOfMonth = dayInMonth[month - 1];
						if (day <= lastDayOfMonth && day >= 1) {
							date = Integer.parseInt(sDate);
							// Check if end date is the same or larger than starting date
							valid = true;

						} else {
							System.out.println("Invalid Day, Please enter a day that falls in your month");
							sDate = handler.getLine();
						}
					} else {
						System.out.println("Invalid Month, Please enter a date with a month between 1-12");
						sDate = handler.getLine();
					}
				} else {
					System.out.println("Incorrect Format, Please enter a new date (YYYYMMDD)");
					sDate = handler.getLine();
				}
			} else {
				System.out.println("End day cannot be before or the same as the start day, please enter a new day");
				date = handler.getInt();
			}
		} while (!valid && date <= startDate);

		this.endDate = date;
	}

	/**
	 * Setter for the frequency variable.
	 * Verifies that the frequency follows basic formatting rules. Can only be 1 for daily, or 7 for weekly.
	 * @param handler
	 */
	public void setFrequency(UserHandler handler) {
		int freq = handler.getInt();
		boolean valid = false;

		while (!valid) {
			// Check if length of frequency is valid
			if (freq == 1 || freq == 7) {
				valid = true;
			} else {
				System.out
						.println("Invalid Frequency, Please enter a frequency equal to 1(once a day) of 7(every week)");
				freq = handler.getInt();
			}
		}
		this.frequency = freq;
	}

	///////////////////////////// Code Setters /////////////////////////////

	/**
	 * A direct setter for the endDate variable.
	 * @param endDate following (YYYYMMDD) pattern.
	 * @throws Exception if the endDate doesn't follow the proper format.
	 */
	public void setEndDate(int endDate) throws Exception {
		String sDate = String.valueOf(endDate);
		int[] dayInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month;
		int day;
		int startDate = getDate();

		// Check if end date is the same or larger than starting date
		if (endDate > startDate) {
			//Check if date is correct Length for formatting
			if (sDate.length() == 8) {
				//Check if it has a valid month
				month = Integer.parseInt(sDate.substring(4, 6));
				if (month <= 12 && month >= 1) {
					//Check if day is valid
					day = Integer.parseInt(sDate.substring(6, 8));
					int lastDayOfMonth = dayInMonth[month - 1];
					if (day <= lastDayOfMonth && day >= 1) {
						endDate = Integer.parseInt(sDate);
					} else {
						throw new Exception("Invalid Day, does not fall in Month");
					}
				} else {
					throw new Exception("Invalid Month,needs to be month between 1-12");
				}
			} else {
				throw new Exception("Incorrect Format for Date");
			}
		} else {
			throw new Exception("Incorrect End Date, is not greater than Start Date");
		}
		this.endDate = endDate;
	}

	/**
	 * @deprecated use setDate() instead.
	 */
	@Deprecated
	public void setStartDate(int startDate) throws Exception {
		setDate(startDate);
	}

	/**
	 * A direct setter for the frequency variable.
	 * @param frequency the frequency of this RecurringTask. Can only be 1 for daily, or 7 for weekly.
	 * @throws Exception
	 */
	public void setFrequency(int frequency) throws Exception {
		// frequency doesn't equal 1 or 7, give error
		if (frequency != 1 && frequency != 7)
			throw new Exception("Invalid Frequency, needs to be frequency equal to 1(once a day) of 7(every week)");
		this.frequency = frequency;
	}

	///////////////////////////// Getters /////////////////////////////

	public int getEndDate() {
		return endDate;
	}

	/**
	 * @deprecated use getDate() instead.
	 */
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
		float endTime = getStartTime() + getDuration();
		if (endTime >= 24) {
			endTime = endTime - 24;
		}
		System.out.println(
				"Name: " + getName() + "\nType: " + getTypeName() + "\nStart Date: " + dateToHumanReadable(getDate())
						+ "\nTime: " + timeToHumanReadable(getStartTime()) + " - " + timeToHumanReadable(endTime)
						+ "\nEnd Date: " + dateToHumanReadable(getEndDate()) + "\nFrequency: " + getFrequency());
	}

}
