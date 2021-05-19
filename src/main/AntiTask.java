package main;

/**
 *
 */
public class AntiTask extends Task {
	public static String[] types = { "Cancellation" };

	/**
	 * The associated RecurringTask for this AntiTask
	 */
	private String reccuringTaskName;

	/**
	 * Constructs a AntiTask in a direct manner with errors for any input problems.
	 * @param name
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 * @throws Exception
	 */
	public AntiTask(String name, float startTime, float duration, int date, String tname) throws Exception {
		super(name, startTime, duration, date, tname, Task.TaskType.ANTI);
		// Make sure the type matches what this Task can take.	
		if (!PSS.isIn(types, tname)) {
			throw new Exception(String.format("Invalid type %s for Anti-Task.", tname));
		}
		// Check for overlap.
		PSS.newTaskOverLapCheckCode(this);
	}

	/**
	 * Constructs an AntiTask with values obtained from the user, all while verifying them.
	 * @param handler
	 * @param typeName
	 */
	public AntiTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.ANTI);
		setTypeName(typeName);
	}

	/////////////////////////// Getters and Setters ///////////////////////////

	public String getReccuringTaskName() {
		return reccuringTaskName;
	}

	public void setReccuringTaskName(String name) {
		this.reccuringTaskName = name;
	}
}
