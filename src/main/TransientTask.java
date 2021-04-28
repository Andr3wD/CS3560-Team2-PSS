package main;

/**
 *
 * @author apedroza
 */
public class TransientTask extends Task {

	public static String[] types = { "Visit", "Shopping", "Appointment" };

	/**
	 *
	 * @param startTime
	 * @param duration
	 * @param date
	 * @param tname
	 */
	public TransientTask(String name, float startTime, float duration, int date, String tname) {
		super(name, startTime, duration, date, tname, Task.TaskType.TRANSIENT);
	}
	
	public TransientTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.TRANSIENT);
		setTypeName(typeName);
	}
}
