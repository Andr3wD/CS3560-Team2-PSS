package main;

/**
 *
 */
public class TransientTask extends Task {

	public static String[] types = { "Visit", "Shopping", "Appointment" };

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 * @throws Exception 
	 */
	public TransientTask(String name, float startTime, float duration, int date, String tname) throws Exception {
		super(name, startTime, duration, date, tname, Task.TaskType.TRANSIENT);
		// Make sure the type matches what this Task can take.
		if (!PSS.isIn(types, tname)) {
			throw new Exception(String.format("Invalid type %s for Transient Task.", tname));
		}
	}

	public TransientTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.TRANSIENT);
		setTypeName(typeName);
	}

}
