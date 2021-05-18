package main;

/**
 *
 */
public class AntiTask extends Task {
	public static String[] types = { "Cancellation" };
        private String reccuringTaskName;

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
		// Check for overlap.
		PSS.newTaskOverLapCheckCode(this);
	}

	public AntiTask(UserHandler handler, String typeName) {
		super(handler);
		setTaskType(Task.TaskType.ANTI);
		setTypeName(typeName);
	}

        public String getReccuringTaskName(){
            return reccuringTaskName;
        }
        
        public void setReccuringTaskName(String name){
            this.reccuringTaskName = name;
        }
}
