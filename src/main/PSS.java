package main;

import java.util.ArrayList;

/**
 *
 * @author apedroza, Andrew
 */
public class PSS {
	
	
	private ArrayList<Task> schedule = new ArrayList<Task>();

	public void createTask(UserHandler handler) {
		System.out.println("Please input a task type.");
		// TODO print all types.
		String type = handler.getLine();

		Task newTask;
		if (isIn(TransientTask.types, type)) {
			newTask = new TransientTask(handler, type);
		} else if (isIn(RecurringTask.types, type)) {
			newTask = new RecurringTask(handler, type);
		} else if (isIn(AntiTask.types, type)) {
			newTask = new AntiTask(handler, type);
		} else {
			System.out.println("That task type doesn't exist. Returning to menu.");
			return;
		}
		schedule.add(newTask);
		System.out.println("Task has been created.");
	}

	public void viewTask(UserHandler handler) {
		System.out.println("Please input the name of the task to view.");
		String name = handler.getLine();
		boolean containsTask;//boolean to see if the schedule contains the task
							 //true if the schedule does contain
		containsTask = schedule.stream().filter(task -> task.getName().equals(name)).findFirst().isPresent();
		if(containsTask) {
			for(Task view : schedule) {
				if(view.getName().equals(name)) {
					view.print();//call task's print method to print all attributes
				}//check if the current task has the same name
			}//loop through each task in the schedule
		}//if the schedule does contain the task
		else {
			System.out.println("There is no task with this name. Returning to menu.");
		}
	}

	public void deleteTask() {

	}

	public void editTask() {

	}

	public void generateSchedule() {

	}

	public void writeSchedule() {

	}

	public void loadSchedule() {

	}
	
	/**
	 * Checks if String s is in the String[] arr.
	 * @param arr
	 * @param s
	 * @return If s is in arr.
	 */
	private boolean isIn(String[] arr, String s) {
		for (String str : arr) {
			if (str.equals(s)) {
				return true;
			}
		}
		return false;
	}
}
