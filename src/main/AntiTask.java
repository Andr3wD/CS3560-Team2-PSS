package main;

/**
 *
 */
public class AntiTask extends Task {
	public static String[] types = { "Cancellation" };

	/**
	 *
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
	}

	public AntiTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.ANTI);
		setTypeName(typeName);
		// TODO do specific validation for anti-tasks.
	}

}
