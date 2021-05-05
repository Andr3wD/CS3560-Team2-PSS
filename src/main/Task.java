package main;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

/**
 *
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
		ANTI, RECURRING, TRANSIENT
	}

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 * @throws Exception 
	 */
	public Task(String name, float startTime, float duration, int date, String tname, TaskType taskType)
			throws Exception {
		// Let the code that calls this constructor deal with the poor inputs.
		setName(name);
		setStartTime(startTime);
		setDuration(duration);
		setDate(date);
		setTypeName(tname);
		setTaskType(taskType);
	}

	public Task(UserHandler handler) {
		System.out.println("Please input a unique name for the task.");
		setName(handler);

		System.out.println("Please enter a start time:");
		setStartTime(handler);

		System.out.println("Please enter a duration:");
		setDuration(handler);

		System.out.println("Please enter a start date:");
		setDate(handler);

		// TODO validate no overlap with other tasks here.
	}

	/**
	 * Method that translates the time from decimal to a human readable string based
	 * on the 12 hour AM/PM clock
	 *
	 * @param time of task to translate
	 * @return readable string
	 */
	public String timeToHumanReadable(float time) {
		double timeAsDouble = time;

		// If the float value is greater than 13, then
		// adjust to PM values
		if (time >= 13) {
			timeAsDouble = time % 12;
		}
		int hours = (int) timeAsDouble; // Variable to hold hour value of time
		double decimal = timeAsDouble - hours;
		if (hours == 0) {
			hours = 12; // If the time has a 0 for hour, or the midnight hour, then hours is 12
		}
		int minutes = (int) (decimal * 60); // Variable to translate decimal value to minutes
		String minutesAsString = Integer.toString(minutes); // Convert minutes to string for editing if needed
		if (minutesAsString.equals("0")) {
			minutesAsString = "00"; // If the minutes for time is 0, then print 00 instead
		}
		// If the time is greater or equal to 12, then it is considered PM
		String readable = (hours + ":" + minutesAsString + " " + ((time >= 12) ? "PM" : "AM"));
		return readable;
	}

	/**
	 * Method that translates the duration from decimal to a human readable string
	 * with hours and minutes
	 * 
	 * @param duration of task to translate
	 * @return readable string
	 */
	public String durationToHumanReadable(float duration) {
		int hours = (int) duration; // Store the hour value of the duration
		double decimal = duration - hours;
		int minutes = (int) (decimal * 60); // Store the decimal value of duration as minutes
		String readable = (hours + " hours and " + minutes + " minutes");
		return readable;
	}

	/**
	 * Method that translates the date of the task to a human readable string in the
	 * format MM-DD-YYYY
	 * 
	 * @param date of task to translate
	 * @return formattedDate in MM-DD-YYYY
	 */
	public String dateToHumanReadable(int date) {
		String stringDate = String.valueOf(date); // Store the date value as a string

		// Date is initially in form YYYYMMDD, so convert to MM-DD-YYYY
		LocalDate convertedDate = LocalDate.parse(stringDate, DateTimeFormatter.BASIC_ISO_DATE);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String formattedDate = convertedDate.format(formatter);
		return formattedDate;
	}

	/**
	 * This returns a JSONObject representing this class and its values, to then be
	 * written directly to a file.
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		// Make a JSON object and put the values in it.
		JSONObject jObj = new JSONObject();
		jObj.put("Name", name);
		jObj.put("StartTime", startTime);
		jObj.put("Date", date);
		jObj.put("Duration", duration);
		jObj.put("Type", typeName.toString());
		return jObj;
	}

	///////////////////////// Getters /////////////////////////

	public String getName() {
		return name;
	}

	public float getStartTime() {
		return startTime;
	}

	public float getDuration() {
		return duration;
	}

	public int getDate() {
		return date;
	}

	public String getTypeName() {
		return typeName;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	///////////////////////// Program Setters /////////////////////////

	public void setName(String name) throws Exception {
		// Check if task name already exists.
		Task existingTask = PSS.getTaskByName(name);
		if (existingTask != null && existingTask == this) {
			// TODO lookat. Depends on how edit task is implemented.
			// If task name already exists, and the task with the name is this task, then we're probably just editing it.
			this.name = name;
		} else if (existingTask != null && existingTask != this) {
			// If task name already exists, and it's not our task, then throw exception.
			throw new Exception("Task name already exists");
		} else if (existingTask == null) {
			// Normally adding name to task.
			this.name = name;
		}
	}

	public void setStartTime(float startTime) throws Exception {
		///Make sure the given time is within 24 hours.
		if (!(startTime <= 23.75 && startTime >= 0.0)) {
			throw new Exception("Task startTime is not within 23.75 - 0.0 hour range");
		}
		if ((startTime % 0.25) > 0) {
			throw new Exception("Task startTime is not in 15 minute increments.");
		}

		this.startTime = startTime;
	}

	public void setDuration(float duration) throws Exception {
		if (!(duration <= 23.75 && duration >= 0.25)) {
			throw new Exception("Task duration is not within 23.75 - 0.25 hour range");
		}
		if ((duration % 0.25) > 0) {
			throw new Exception("Task duration is not in 15 minute increments.");
		}

		this.duration = duration;
	}

	public void setDate(int date) throws Exception {
		String sDate = String.valueOf(date);
		int[] dayInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month;
		int day;

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
				} else {
					throw new Exception("Invalid Day, does not fall within the Task.");
				}
			} else {
				throw new Exception("Invalid Month, month " + month + " does not exist.");
			}
		} else {
			throw new Exception("Incorrect date format.");
		}

		this.date = date;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	///////////////////////// UserHandler Setters /////////////////////////

        /**
         * Check if the entered name is not the same as another task in PSS.
         * Use static method getTaskByName(name) to check against the schedule of tasks in PSS
         * If taskName already exists as for user to enter another name.
         * @param handler used for user input
         */
	public void setName(UserHandler handler) {
		String name = handler.getLine();

		// Check if task name already exists, and it's not this.
		Task existingTask = PSS.getTaskByName(name);

		while (existingTask != null && existingTask != this) {
			System.out.println("That name already exists. Please input a new unique name.");
			name = handler.getLine();
			existingTask = PSS.getTaskByName(name);
		}
		this.name = name;
	}

	/**
	 * Check if the passed startTime is within the 24-hour clock
	 * if not ask for a new number.
	 * Round number to 2 decimals and the nearest Quarter.
	 * @param startTime passed in float time.
	 */
	public void setStartTime(UserHandler handler) {
		float startTime = handler.getFloat();
		boolean valid = false;

		///Make sure the given time is within 24 hours. If not ask for another number.
		while (!valid) {
			if (startTime <= 23.75 && startTime >= 0.0) {
				valid = true;
			} else {
				System.out.println("Not a valid startTime, Please enter another time Expressed as a 24-hour time.");
				startTime = handler.getFloat();
			}
		}

		//Round Float 2 decimal places.
		DecimalFormat twoDec = new DecimalFormat("#.##");
		startTime = Float.valueOf(twoDec.format(startTime));

		//Round to the nearest 15 minutes
		startTime = Math.round(startTime * 4) / 4f;

		this.startTime = startTime;
	}//end setStartTime

	/**
	 * Round duration to the nearest 2 decimals and round to the nearest 15 min.
	 * @param duration passed in duration time.
	 */
	public void setDuration(UserHandler handler) {
		// TODO add restriction from 0.25 -> 23.75
		float duration = handler.getFloat();
		boolean valid = false;

		///Make sure the given time is within 24 hours. If not ask for another number.
		while (!valid) {
			if (duration <= 23.75 && duration >= 0.25) {
				valid = true;
			} else {
				System.out.println("Not a valid duration, Please enter another time between 15 minutes and 23.75 hours");
				duration = handler.getFloat();
			}
		}

		//Round Float 2 decimal places.
		DecimalFormat twoDec = new DecimalFormat("#.##");
		duration = Float.valueOf(twoDec.format(duration));

		//Round to the nearest 15 minutes
		duration = Math.round(duration * 4) / 4f;

		this.duration = duration;
	}//end setDuration 

	/**
	 * Checks Date to see if it is in the valid format YYYYMMDD
	 * First check that the length of date meets criteria
	 * Check if the Month is valid.
	 * Check if the day is valid for that month.
	 * If One of these checks is not meet as for user to input another date.
	 * @param date passed in date.
	 */
	public void setDate(UserHandler handler) {
		int date = handler.getInt();
		boolean valid = false;
		String sDate = String.valueOf(date);
		int[] dayInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month;
		int day;

		while (!valid) {
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
		}

		this.date = date;
	}//end setDate

	/**
	 * Print a formatted version of the Task we just created
	 */
	public void print() {
		System.out.println("\n" + "Name: " + getName() + "\n Type: " + getTypeName() + "\n Date: "
				+ dateToHumanReadable(getDate()) + "\n Start Time: " + timeToHumanReadable(getStartTime())
				+ "\n Duration: " + durationToHumanReadable(getDuration()) + "\n");
	}//end Print
}
