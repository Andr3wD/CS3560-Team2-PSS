package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import main.Task.TaskType;

/**
 *
 */
public class PSS {

	////////////////////////Main Function Methods///////////////////////////////////////

	public static ArrayList<Task> schedule = new ArrayList<Task>();

	/**
	 * Creates a new task, asking each new Task to figure out the user input
	 * themselves.
	 * 
	 * @param handler
	 */
	public void createTask(UserHandler handler) {
		System.out.println("Please input a task type from the following options:\n"
				+ "- Class\n- Study\n- Sleep\n- Exercise\n- Work\n- Meal\n- Cancellation\n- Visit\n- Shopping\n- Appointment");
		String type = handler.getLine();

		Task newTask; // Declare shell task.
		if (isIn(TransientTask.types, type)) { // If input is of task type Transient
			// Make a new TransientTask, asking the new TransientTask to handle the user
			// interactions for us.
			System.out.println("You have selected type Transient Task.");
			newTask = new TransientTask(handler, type);
		} else if (isIn(RecurringTask.types, type)) { // If input is of task type Recurring
			// Make a new RecurringTask, asking the new RecurringTask to handle the user
			// interactions for us.
			System.out.println("You have selected type Recurring Task.");
			newTask = new RecurringTask(handler, type);
		} else if (isIn(AntiTask.types, type)) { // If input is of task type Anti
			// Make a new AntiTask, asking the new AntiTask to handle the user interactions
			// for us.
			System.out.println("You have selected type Anti-Task.");
			newTask = new AntiTask(handler, type);
		} else {
			System.out.println("That task type doesn't exist. Returning to menu.");
			return;
		}

		//Check for Overlap
		if (newTaskOverLapCheck(newTask)) {
			System.out.println("Task could not be created because it Overlaps with another task.");
		} else if (newTask.getTaskType() == TaskType.ANTI && !verifyRecurringExists((AntiTask) newTask)) {
			System.out.println("Anti-task could not be created because it has no associated Recurring task!");
		} else {
			schedule.add(newTask); //Add task to schedule
			newTask.print();  //Print task for user
			System.out.println("Task has been created.");
		}
	}

	public void viewTask(UserHandler handler) {
		System.out.println("Please input the name of the task to view.");
		String name = handler.getLine();
		boolean containsTask;// boolean to see if the schedule contains the task
		// true if the schedule does contain
		containsTask = schedule.stream().filter(task -> task.getName().equals(name)).findFirst().isPresent();
		// if the schedule does contain the task
		if (containsTask) {
			// loop through each task in the schedule
			for (Task view : schedule) {
				// check if the current task has the same name
				if (view.getName().equals(name)) {
					view.print();// call task's print method to print all attributes
				}
			}
		} else {
			System.out.println("There is no task with this name. Returning to menu.");
		}
	}

	/**
	 * Will take in user input for the name of the Task they want to delete
	 * 
	 * If the task is found in the schedule array: 
	 * 		- If the task is recurring then delete any anti-tasks associated with it 
	 * 		- If its an anti-task check for conflicts.
	 * 			If conflicts are found return the names of the conflicting tasks.
	 * note: Anti-Task are only used to cancel recurring tasks.
	 * 
	 * If task is not found: Message will let the user know.
	 * 
	 * @param handler used to capture user input.
	 */
	public void deleteTask(UserHandler handler) {
		System.out.println("Enter the name of the task you want to Delete:");
		String taskName = handler.getLine();
		Task targetTask;

		if ((schedule == null) || schedule.isEmpty()) { // Checks if the schedule has not been initialized
			System.out.println("This Schedule is empty.");
		} else {
			for (int i = 0; i < schedule.size(); i++) {
				// Search through Schedule list
				if (taskName.equals(schedule.get(i).getName())) {
					targetTask = schedule.get(i);

					if (targetTask.getTaskType() == Task.TaskType.RECURRING) {
						//Options for Recurring task

						deleteAntiTasks(targetTask);
						schedule.remove(i);
						System.out.print(taskName + " Has been deleted scuessfully" + "\n");

						return;
					} else if (targetTask.getTaskType() == Task.TaskType.ANTI) {
						if (!checkForConflicts((AntiTask) targetTask)) { //Need to fix does not detect overlap if antitask is deleted.
							schedule.remove(i);
							System.out.println("Anti-Task deleted sucessfully.");
							return;
						} // checkForConflicts if none remove anti-task
						else {
							return;
						} // if conflicts found exit. checkForConflicts will provide output messege
					} else {
						schedule.remove(i);
						System.out.println(taskName + " Has been deleted scuessfully");
						return;
					}
				}
			}

			System.out.println("No Task Found with that Name returning to menu.");
		}
	}

	/**
	 * Allows user to edit a specific task to have different values.
	 * If the task is found in the schedule array:
	 * 		- Delete anti-tasks if editing a recurring task
	 * 		- Utilize createTask method for editing
	 * 		- Upon any errors while editing a task, revert schedule prior to any changes
	 * 
	 * If task is not found: Message will let the user know.
	 * 
	 * @param handler used to capture user input.
	 */
	public void editTask(UserHandler handler) {
		System.out.println("Enter the name of the task you want to edit:");
		String taskName = handler.getLine();
		Task targetTask = getTaskByName(taskName);

		// Check if the task exists and print the attributes
		if (targetTask == null) {
			System.out.println("No Task Found with that name returning to menu.");
			return;
		}
		System.out.println("Current attributes for the task:");
		targetTask.print();
		ArrayList<Task> oldSchedule = new ArrayList<Task>(schedule); // Temporary schedule to hold the schedule prior to any changes

		// If the task being edited is recurring, then prompt the user
		// that all anti-tasks will be deleted on edit
		if (targetTask.getTaskType() == Task.TaskType.RECURRING) {
			if (hasAntiTask((RecurringTask) targetTask, targetTask.getDate())) {
				System.out.println("WARNING: This recurring task has anti-tasks associated with it. "
						+ "Upon edit, these anti-tasks will be deleted. \nContinue? (Y or N)");
				if (handler.getLine().equalsIgnoreCase("N")) {
					System.out.println("No changes made. Returning");
					return;
				} else {
					deleteAntiTasks(targetTask);
				}
			}
		}

		int initialSize = schedule.size(); // Variable to hold schedule size prior to changes

		// Remove the task to be edited and call createTask to simulate editing a task
		for (int i = 0; i < schedule.size(); i++) {
			if (targetTask.getName().equals(schedule.get(i).getName())) {
				schedule.remove(i);
				break;
			}
		}
		createTask(handler); // Calling create task allows checking for overlap with edited task

		// If the task is never made from createTask, then then reset schedule
		// the schedule prior to any changes
		if (initialSize > schedule.size()) {
			schedule = new ArrayList<Task>(oldSchedule);
			System.out.println("No changes were made.");
			return;
		}
		Task temp;

		// TODO replace with newTaskOverlapCheck().

		// Loop through the rest of the schedule to check every task for overlapping
		for (int i = 0; i < schedule.size() - 1; i++) {
			temp = schedule.get(0);
			schedule.remove(0);

			// If the task does overlap, then revert the schedule back and return
			if (newTaskOverLapCheck(temp)) {
				schedule = new ArrayList<Task>(oldSchedule);
				System.out.println("No changes were made.");
				return;
			}
			schedule.add(temp);
		}
		System.out.println("Task has been edited.");
	}

	/**
	 * Generates and prints the schedule for the user provided initial and end dates.
	 * @param handler
	 */
	public void generateSchedule(UserHandler handler) {

		//Handler inputs first date
		System.out.println("Enter the initial date: ");
		int Task_Initial = handler.getInt();

		// Validate that the Task_Initial date is a legitimate date
		try {
			validateDate(Task_Initial);
		} catch (Exception e) {
			System.out.println(e.getMessage() + " Returning to menu.");
			return;
		}

		//Handler inputs first date
		System.out.println("Enter the end date: ");
		int Task_Final = handler.getInt();

		// Validate that the Task_Final date is a legitimate date
		try {
			validateDate(Task_Final);
		} catch (Exception e) {
			System.out.println(e.getMessage() + " Returning to menu.");
			return;
		}

		if (Task_Final <= Task_Initial) {
			System.out.println("The end date must be larger than the initial date. Returning to menu.");
			return;
		}

		// Add all the dates in the range to the dateList.
		HashMap<Integer, ArrayList<Task>> dateList = new HashMap<>();
		Integer currentDate = Task_Initial;
		while (currentDate <= Task_Final) {
			dateList.put(currentDate, new ArrayList<Task>());
			currentDate = addDay(currentDate, 1);
		}

		//Parse through all tasks and add them to their date to display
		for (Task task : schedule) {

			// If the task is recurring
			if (task.getTaskType() == TaskType.RECURRING) {
				// Get a list of dates for this recurring task
				ArrayList<Integer> dates = createDays((RecurringTask) task);
				// Parse through recurring dates
				for (Integer d : dates) {
					// If the recurring date is within our date range
					if (d >= Task_Initial && d <= Task_Final) {
						// And if this date doesn't have an anti-task for this recurring task
						if (!hasAntiTask((RecurringTask) task, d)) {
							// Then add the recurring task to this date
							dateList.get(d).add(task);
						}
					}
				}
			} else if (task.getTaskType() == TaskType.TRANSIENT) {
				int d = task.getDate();
				if (d >= Task_Initial && d <= Task_Final) {
					dateList.get(d).add(task);
				}
			}
		}

		//Now print it all out
		// Parse through dates in range
		currentDate = Task_Initial;
		while (currentDate <= Task_Final) {
			if (!dateList.get(currentDate).isEmpty()) {

				System.out.println("=============" + Task.dateToHumanReadable(currentDate) + "============="); // Print header for the day.

				// Sort by time.
				dateList.get(currentDate).sort(new Comparator<Task>() {
					@Override
					public int compare(Task o1, Task o2) {
						if (o1.getStartTime() > o2.getStartTime()) {
							return 1;
						} else {
							return -1;
						}
					}
				});

				// Parse through tasks in dateList with the currentDate as the key
				for (Task task : dateList.get(currentDate)) {
					System.out.println(task.getName() + " | " + task.getTypeName() + " | " + task.getTimeRange());
				}
			}
			currentDate = addDay(currentDate, 1);
		}

	}

	/**
	 * Method to write daily, weekly, or monthly schedule determined by the user, to a file.
	 * @param handler
	 */
	public void writeSchedule(UserHandler handler) {
		int startDate;
		int endDate;
		String fileLocation = null;
		String timePeriod = null;

		// ask user for starting date
		System.out.println(
				"Hello! Please enter starting date for what day, week, or month you want the schedule for. (YYYYMMDD)");
		startDate = Integer.parseInt(handler.getLine());

		// user determines schedule for the day, week, or month
		System.out.println("Day, Week, Month");
		timePeriod = handler.getLine();

		// user determines file location
		System.out.println("Please input a file location to save the schedule to:");
		fileLocation = handler.getLine();

		// Sort according to project requirements.
		schedule.sort(new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				if (o1.getDate() > o2.getDate()) {
					return 1;
				} else if (o1.getDate() < o2.getDate()) {
					return -1;
				} else {
					if (o1.getStartTime() > o2.getStartTime()) {
						return 1;
					} else if (o1.getStartTime() < o2.getStartTime()) {
						return -1;
					} else {
						return 0;
					}
				}
			}

		});

		switch (timePeriod) {
		case "Day":
			ArrayList<Task> daySchedule = new ArrayList<Task>();
			// for every task in the schedule ArrayList compare to given date
			for (Task task : schedule) {
				// search for tasks with that day
				if (startDate == task.getDate()) {
					// add that task to daySchedule
					daySchedule.add(task);
					//task.print();
				}
			}

			//			printSchedule(daySchedule);

			try {
				DataFile.save(daySchedule, fileLocation);
				System.out.println("Day schedule has been saved to: " + fileLocation);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;

		case "Week":
			ArrayList<Task> weeklySchedule = new ArrayList<Task>();
			endDate = addDay(startDate, 6);
			// for every task in the schedule ArrayList compare to dates between startDate and endDate...
			for (Task task : schedule) {
				if ((startDate <= task.getDate()) && (task.getDate() <= endDate)) {
					// add tasks between startDate and endDate to a weekly schedule
					weeklySchedule.add(task);
				}
			}
			//			printSchedule(weeklySchedule);

			try {
				DataFile.save(weeklySchedule, fileLocation);
				System.out.println("Weekly schedule has been saved to: " + fileLocation);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;

		case "Month":
			ArrayList<Task> monthlySchedule = new ArrayList<Task>();
			endDate = addDay(startDate, 29);
			// for every task in the schedule ArrayList compare to dates between startDate and endDate...
			for (Task task : schedule) {
				if ((startDate <= task.getDate()) && (task.getDate() <= endDate)) {
					// add tasks between startDate and endDate to a monthly schedule
					monthlySchedule.add(task);
				}
			}
			//			printSchedule(monthlySchedule);

			try {
				DataFile.save(monthlySchedule, fileLocation);
				System.out.println("Month schedule has been saved to: " + fileLocation);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			break;

		default:
			System.out.println("Not a valid time period.");
			break;
		}
	}

	/**
	 * Writes the whole PSS schedule ArrayList to a file location provided by the user.
	 * @param userHandler
	 */
	public void writeWholeSchedule(UserHandler handler) {
		System.out.println("Please input a file location to save the whole schedule to:");
		String fileLocation = handler.getLine();
		try {
			DataFile.save(schedule, fileLocation);
			System.out.println("Schedule has been saved to: " + fileLocation);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Loads the user provided file location JSON schedule file into the PSS schedule, all while verifying the schedule.
	 * @param handler
	 */
	public void loadSchedule(UserHandler handler) {
		System.out.println("Please input a file location to load the whole schedule from:");
		String fileLocation = handler.getLine();

		ArrayList<Task> loadingSchedule = DataFile.load(fileLocation);
		if (loadingSchedule != null) {
			schedule.addAll(loadingSchedule);
		} else {
			System.out.println("Error loading schedule from file.");
		}
	}

	///////////////////////////Helper Methods for deleteTask()////////////////////////////////////////////

	/**
	 * Deletes Anti-Tasks with same date, startTime and duration as RecurringTask
	 * 
	 * @param recurringTask the recurringTask we want to delete
	 */
	private void deleteAntiTasks(Task recurringTask) {
		Task foundAntiTask;

		for (int i = 0; i < schedule.size(); i++) {
			if (schedule.get(i).getTaskType() == Task.TaskType.ANTI) {
				foundAntiTask = schedule.get(i);
				//Check that an Anti-task with the same parameters does not exist.
				if (foundAntiTask.getDate() == recurringTask.getDate()
						&& foundAntiTask.getStartTime() == recurringTask.getStartTime()
						&& foundAntiTask.getDuration() == recurringTask.getDuration()) {
					System.out.println("The Anti-Task: " + foundAntiTask.getName()
							+ " associated with the reccuring task have been deleted. ");
					schedule.remove(i);
				}
			}
		}
	}

	/**
	 * Checks if any tasks (Transient or Recurring) will overlap with recurring task if the anti task is deleted. 
	 * First finds any Transient tasks that fall into the AntiTask. 
	 * If found output which task would overlap. 
	 * 
	 * @param targetTask anti-task that is passed in.
	 * 
	 * @return false if no conflicts. True and print conflicting task if found
	 */
	public static boolean checkForConflicts(AntiTask targetTask) {
		//Remove the Associated Reccuring task from the list.
		ArrayList<Task> copyOfSchdule = schedule;
		for (int a = 0; a < copyOfSchdule.size(); a++) {
			if (copyOfSchdule.get(a).getName().equals(targetTask.getReccuringTaskName())) {
				copyOfSchdule.remove(a);
			}
		}

		boolean conflictFound = false;
		int targetDate = targetTask.getDate();
		float targetStartTime = targetTask.getStartTime();
		float targetEndTime = targetStartTime + targetTask.getDuration();
		Task matchingTask;// Task that matches the time frame of Anti task.
		ArrayList<Integer> Days;

		for (int i = 0; i < copyOfSchdule.size(); i++) {
			matchingTask = copyOfSchdule.get(i);
			float matchTime = matchingTask.getStartTime() + matchingTask.getDuration();

			if (matchingTask.getTaskType() == TaskType.TRANSIENT) {
				if (targetDate == matchingTask.getDate() && targetStartTime <= matchTime
						&& matchTime <= targetEndTime) {
					System.out.println("Could not delete Anti-Task " + targetTask.getName() + ", "
							+ matchingTask.getName() + " would conflict with the Recuring task.");
					conflictFound = true;
				}
			} else if (matchingTask.getTaskType() == TaskType.RECURRING) {
				Days = createDays((RecurringTask) matchingTask);

				for (int x = 0; x < Days.size(); x++) {
					if (Days.get(x).equals(targetDate)) {
						if (targetStartTime <= matchTime && matchTime <= targetEndTime) {
							System.out.println("Could not delete Anti-Task " + targetTask.getName() + ", "
									+ matchingTask.getName() + " would conflict with the Recuring task.");
							conflictFound = true;
						}
					}

				}
			}
		}

		return conflictFound;
	}

	//////////////////////// TOOLS ////////////////////////

	/**
	 * Checks all tasks in the schedule for the provided name.
	 * @param newName
	 * @return false if newName doesn't exist in the schedule.<br>true if newName exists in the schedule.
	 */
	public static Task getTaskByName(String newName) {
		for (Task task : schedule) {
			if (task.getName().equals(newName)) {
				return task;
			}
		}
		return null;
	}

	/**
	 * Checks if String s is in the String[] arr.
	 * 
	 * @param  arr
	 * @param  s
	 * @return If s is in arr.
	 */
	public static boolean isIn(String[] arr, String s) {
		for (String str : arr) {
			if (str.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifies that the given AntiTask has an associated RecurringTask to cancel out.
	 * @param task
	 * @return false if no RecurringTask exists, true if a RecurringTask exists.
	 */
	private boolean verifyRecurringExists(AntiTask task) {
		for (Task t : schedule) {
			if (t.getTaskType() == TaskType.RECURRING) {
				// This anti-task needs to fall within that recurring timeslot on that day.

				ArrayList<Integer> dayList = PSS.createDays((RecurringTask) t);

				for (Integer day : dayList) {
					if (day == task.getDate()) {
						// This anti-task has the same day as a recurring task.

						// Now verify the timeslot.
						// Start time and end time must match according to lecture 15.
						if (task.getStartTime() == t.getStartTime() && task.getDuration() == t.getDuration()) {
							task.setReccuringTaskName(t.getName()); //Save the name of the associated Reccuring task to the Anti-task
							return true;
						}

					}
				}
			}
		}
		return false;
	}

	/*
	 * Method to print any given schedule.
	 */
	private void printSchedule(ArrayList<Task> someSchedule) {
		System.out.println("Your schedule: ");
		for (Task task : someSchedule) {
			task.print();
		}
	}

	////////////////////// Overlap Checking Methods//////////////////////

	/**
	 * Used to check if a new Task will overlap with existing task.
	 * 
	 * Take into consideration the type of task we are trying to create(ANTI,TRANSIENT,RECURRING)
	 * 
	 * @param newTask task we want to add
	 * @return false is there is no overlap. True + give message if there is overlap.
	 */
	public boolean newTaskOverLapCheck(Task newTask) {
		int date = newTask.getDate();

		if (newTask.getTaskType() == Task.TaskType.ANTI) {
			//Check that another Anti Task does not already exist here
			Task foundAntiTask;

			for (int i = 0; i < schedule.size(); i++) {

				//Check for other Anti-Tasks in schedule
				if (schedule.get(i).getTaskType() == Task.TaskType.ANTI) {
					foundAntiTask = schedule.get(i);

					//Check that an Anti-task with the same permameters does not exist.
					if (foundAntiTask.getDate() == date) {

						// Check time overlap
						if (checkTimeOverlap(newTask, foundAntiTask, true)) {
							System.out.println("Another Anti-Task, " + foundAntiTask.getName()
									+ ", already exists for this time slot.");
							return true;
						}
					}
				}
			}
		} else if (newTask.getTaskType() == Task.TaskType.TRANSIENT) {
			for (int i = 0; i < schedule.size(); i++) {
				Task matchingTask = schedule.get(i);

				//Check newTask against other Transient Tasks(matchingTask)
				if (matchingTask.getTaskType() == Task.TaskType.TRANSIENT) {
					// Check if newTask matches date or is before or after date.
					if (date == matchingTask.getDate() || addDay(matchingTask.getDate(), 1) == date
							|| addDay(date, 1) == matchingTask.getDate()) {

						// Check time overlap
						if (checkTimeOverlap(newTask, matchingTask, date == matchingTask.getDate())) {
							System.out.println(
									"The Transient Task, " + matchingTask.getName() + ", overlaps with your New Task.");
							return true;
						}
					}
				} else if (matchingTask.getTaskType() == Task.TaskType.RECURRING) {
					ArrayList<Integer> matchingTaskDays = createDays((RecurringTask) matchingTask);

					for (int x = 0; x < matchingTaskDays.size(); x++) {
						// Check if newTask matches date or is before or after date.
						if (date == matchingTaskDays.get(x) || addDay(matchingTask.getDate(), 1) == date
								|| addDay(date, 1) == matchingTask.getDate()) {

							//Check if the times on the matching days overlap
							if (checkTimeOverlap(newTask, matchingTask, date == matchingTaskDays.get(x))) {
								//Check if Reccuring task has an anti task for this time and day.
								if (!hasAntiTask((RecurringTask) matchingTask, newTask.getDate())) {
									//If no anti task is found then there is overlap
									System.out.println("The Reccuring Task, " + matchingTask.getName()
											+ ", overlaps with your New Task.");
									return true;
								}
							}
						}
					}
				}
			}
		} else if (newTask.getTaskType() == Task.TaskType.RECURRING) {
			for (int i = 0; i < schedule.size(); i++) {
				Task matchingTask = schedule.get(i);

				//Check newTask against other Transient Tasks(matchingTask)
				if (matchingTask.getTaskType() == Task.TaskType.TRANSIENT) {
					ArrayList<Integer> matchingTaskDays = createDays((RecurringTask) newTask);

					for (int x = 0; x < matchingTaskDays.size(); x++) {
						// Check if newTask Date List matches date or is before or after date.
						if (matchingTask.getDate() == matchingTaskDays.get(x)
								|| addDay(matchingTask.getDate(), 1) == matchingTaskDays.get(x)
								|| addDay(matchingTaskDays.get(x), 1) == matchingTask.getDate()) {

							//Check if the times on the matching days overlap
							if (checkTimeOverlap(newTask, matchingTask,
									matchingTask.getDate() == matchingTaskDays.get(x))) {
								//Since this is a new Reccuring Task it will not have any anti-Tasks and neither will the Transient task we are checking
								System.out.println("The Transient Task, " + matchingTask.getName()
										+ ", overlaps with your New Task.");
								return true;
							}
						}
					}
				}
				//check newTask(Recurring) against other Recurring Tasks
				else if (matchingTask.getTaskType() == Task.TaskType.RECURRING) {
					ArrayList<Integer> newTaskDays = createDays((RecurringTask) newTask);
					ArrayList<Integer> matchingTaskDays = createDays((RecurringTask) matchingTask);
					int newDate;

					for (int a = 0; a < newTaskDays.size(); a++) {
						newDate = newTaskDays.get(a);

						for (int b = 0; b < matchingTaskDays.size(); b++) {
							// Check if newTask Date List matches dates in Date List or is before or after dates in Date List.
							if (newDate == matchingTaskDays.get(b) || addDay(matchingTaskDays.get(b), 1) == newDate
									|| addDay(newDate, 1) == matchingTaskDays.get(b)) {

								// Check for time overlap.
								if (checkTimeOverlap(newTask, matchingTask, newDate == matchingTaskDays.get(b))) {
									if (!hasAntiTask((RecurringTask) matchingTask, newTask.getDate())) {
										System.out.println("The Reccuring Task " + matchingTask.getName()
												+ ", overlaps with your new task.");
										return true;//add antitask and Sys message
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Used to check if a new Task will overlap with existing task from code.
	 * 
	 * Take into consideration the type of task we are trying to create(ANTI,TRANSIENT,RECURRING)
	 * 
	 * @param newTask task we want to add
	 * @return false is there is no overlap. Otherwise, throws Exception with message of problem.
	 * @throws Exception 
	 */
	public static boolean newTaskOverLapCheckCode(Task newTask) throws Exception {
		int date = newTask.getDate();

		if (newTask.getTaskType() == Task.TaskType.ANTI) {
			//Check that another Anti Task does not already exist here
			Task foundAntiTask;

			for (int i = 0; i < schedule.size(); i++) {

				//Check for other Anti-Tasks in schedule
				if (schedule.get(i).getTaskType() == Task.TaskType.ANTI) {
					foundAntiTask = schedule.get(i);

					//Check that an Anti-task with the same permameters does not exist.
					if (foundAntiTask.getDate() == date) {

						// Check time overlap
						if (checkTimeOverlap(newTask, foundAntiTask, true)) {
							throw new Exception("The Anti-Task Task " + foundAntiTask.getName()
									+ ", overlaps with Anti-Task: " + newTask.getName());
						}
					}
				}
			}
		} else if (newTask.getTaskType() == Task.TaskType.TRANSIENT) {
			for (int i = 0; i < schedule.size(); i++) {
				Task matchingTask = schedule.get(i);

				//Check newTask against other Transient Tasks(matchingTask)
				if (matchingTask.getTaskType() == Task.TaskType.TRANSIENT) {
					// Check if newTask matches date or is before or after date.
					if (date == matchingTask.getDate() || addDay(matchingTask.getDate(), 1) == date
							|| addDay(date, 1) == matchingTask.getDate()) {

						// Check time overlap
						if (checkTimeOverlap(newTask, matchingTask, date == matchingTask.getDate())) {
							throw new Exception("The Transient Task " + matchingTask.getName()
									+ ", overlaps with task: " + newTask.getName());
						}
					}
				} else if (matchingTask.getTaskType() == Task.TaskType.RECURRING) {
					ArrayList<Integer> matchingTaskDays = createDays((RecurringTask) matchingTask);

					for (int x = 0; x < matchingTaskDays.size(); x++) {
						// Check if newTask matches date or is before or after date.
						if (date == matchingTaskDays.get(x) || addDay(matchingTask.getDate(), 1) == date
								|| addDay(date, 1) == matchingTask.getDate()) {

							//Check if the times on the matching days overlap
							if (checkTimeOverlap(newTask, matchingTask, date == matchingTaskDays.get(x))) {
								//Check if Reccuring task has an anti task for this time and day.
								if (!hasAntiTask((RecurringTask) matchingTask, newTask.getDate())) {
									//If no anti task is found then there is overlap
									throw new Exception("The Reccuring Task " + matchingTask.getName()
											+ ", overlaps with task: " + newTask.getName());
								}
							}
						}
					}
				}
			}
		} else if (newTask.getTaskType() == Task.TaskType.RECURRING) {
			for (int i = 0; i < schedule.size(); i++) {
				Task matchingTask = schedule.get(i);

				//Check newTask against other Transient Tasks(matchingTask)
				if (matchingTask.getTaskType() == Task.TaskType.TRANSIENT) {
					ArrayList<Integer> matchingTaskDays = createDays((RecurringTask) newTask);

					for (int x = 0; x < matchingTaskDays.size(); x++) {
						// Check if newTask Date List matches date or is before or after date.
						if (matchingTask.getDate() == matchingTaskDays.get(x)
								|| addDay(matchingTask.getDate(), 1) == matchingTaskDays.get(x)
								|| addDay(matchingTaskDays.get(x), 1) == matchingTask.getDate()) {

							//Check if the times on the matching days overlap
							if (checkTimeOverlap(newTask, matchingTask,
									matchingTask.getDate() == matchingTaskDays.get(x))) {
								//Since this is a new Reccuring Task it will not have any anti-Tasks and neither will the Transient task we are checking
								throw new Exception("The Transient Task " + matchingTask.getName()
										+ ", overlaps with task: " + newTask.getName());
							}
						}
					}
				}
				//check newTask(Recurring) against other Recurring Tasks
				else if (matchingTask.getTaskType() == Task.TaskType.RECURRING) {
					ArrayList<Integer> newTaskDays = createDays((RecurringTask) newTask);
					ArrayList<Integer> matchingTaskDays = createDays((RecurringTask) matchingTask);
					int newDate;

					for (int a = 0; a < newTaskDays.size(); a++) {
						newDate = newTaskDays.get(a);

						for (int b = 0; b < matchingTaskDays.size(); b++) {
							// Check if newTask Date List matches dates in Date List or is before or after dates in Date List.
							if (newDate == matchingTaskDays.get(b) || addDay(matchingTaskDays.get(b), 1) == newDate
									|| addDay(newDate, 1) == matchingTaskDays.get(b)) {

								// Check for time overlap.
								if (checkTimeOverlap(newTask, matchingTask, newDate == matchingTaskDays.get(b))) {
									if (!hasAntiTask((RecurringTask) matchingTask, newTask.getDate())) {
										throw new Exception("The Reccuring Task " + matchingTask.getName()
												+ ", overlaps with task: " + newTask.getName());
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Checks if start1 and end1 overlap with start2 and end2. Also account for wrap around times TODO redo javadoc
	 * @param start1
	 * @param end1
	 * @param start2
	 * @param end2
	 * @return false if no overlap. True if overlap exists.
	 */
	public static boolean checkTimeOverlap(Task t1, Task t2, boolean sameDate) {

		// 1. End time falls within the matching task time frame.
		// 2. Start time falls within the matching task time frame.
		// 3. Start time falls before, and end time falls after.
		float start1 = t1.getStartTime();
		float end1 = start1 + t1.getDuration();

		float start2 = t2.getStartTime();
		float end2 = start2 + t2.getDuration();
		if (sameDate) {
			//Check for Overlapping times
			if (end1 <= end2 && end1 >= start2) {
				return true;
			} else if (start1 >= start2 && start1 < end2) { // start1 can be == end2
				return true;
			} else if (start1 <= start2 && end1 >= end2) {
				return true;
			}
		}

		// If this t1 data is before t2 date and t1 wraps to the next day:
		if (t1.getDate() < t2.getDate() && end1 > 24) { // Check if this task wraps over any other task in the next day.
			float nextDayEnd = end1 - 24;
			if (nextDayEnd > start2) {
				return true;
			}

		} else if (t1.getDate() > t2.getDate() && end2 > 24) { // If this t1 data is after t2 date and t2 wraps to the next day:
			float nextDayEnd = end2 - 24;
			if (nextDayEnd > start1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check to see if a Recurring task has an antiTask that matches the Parameters of another Task
	 * @param task Recurring task that we are check if it has antiTasks
	 * @param Date Date for the other task we are checking
	 * @param StartTime Start time for the other task we are checking
	 * @return true if found anti-task; False if not anti-task found
	 */
	public static boolean hasAntiTask(RecurringTask task, int Date) {
		// Parse through the schedule
		for (Task t : schedule) {
			// If the type is ANTI, and the date and time match the given date and time
			if (t.getTaskType() == TaskType.ANTI && t.getDate() == Date && t.getStartTime() == task.getStartTime()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Validates that the date is a date that exists.
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static boolean validateDate(int date) throws Exception {
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
					throw new Exception("Invalid Day, does not fall within the Month.");
				}
			} else {
				throw new Exception("Invalid Month, month " + month + " does not exist.");
			}
		} else {
			throw new Exception("Incorrect date format.");
		}
		return true;
	}

	/**
	 * Creates an ArrayList of the dates a Recurring Task Occurs
	 * Start with StartDate and add days base on frequency until endDate is reached
	 * @param task passed in task
	 * @return list of days that task occurs
	 */
	public static ArrayList<Integer> createDays(RecurringTask task) {
		ArrayList<Integer> daysScheduled = new ArrayList<>();
		int startDate = task.getDate();
		int endDate = task.getEndDate();
		int freq = task.getFrequency();
		int newDate = startDate; //Holds the New Date after Additon
		int totalDays = endDate - startDate;

		int i = 0;
		while (i < totalDays) {
			//Add days singlarly
			if (freq == 1) {
				daysScheduled.add(newDate);
				newDate = addDay(newDate, 1);
				i++;
			}
			//Add day on a weekly basis
			else if (freq == 7) {
				daysScheduled.add(newDate);
				newDate = addDay(newDate, 7);
				i += 7;
			}
		}
		return daysScheduled;
	}

	/**
	 * Adds n day to a Date and return the new Date. 
	 * 
	 * This method accounts for adding a day that rolls into the next month or year.
	 * 
	 * @param startDate Starting Date that we are adding to
	 * @param numOfDaysAdded how many days we are adding to this date (1 or 7)
	 * @return newDate
	 */
	public static int addDay(int startDate, int numOfDaysAdded) {
		int[] dayInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		int newDate = startDate + numOfDaysAdded;
		String sDate = String.valueOf(newDate);
		int month = Integer.parseInt(sDate.substring(4, 6));
		int day = Integer.parseInt(sDate.substring(6, 8));
		int year = Integer.parseInt(sDate.substring(0, 4));

		//Check if days are now out of bounds
		if (dayInMonth[month - 1] < day) {
			day = day - dayInMonth[month - 1]; //How many day roll over into the next month
			month++;
		}

		//Check if month is out of bounds
		if (month > 12) {
			month = 1;
			year++;
		}

		//Put Year, Month and Day back into Date Format
		String finalYear = String.valueOf(year);
		String finalDay = String.valueOf(day);
		String finalMonth = String.valueOf(month);

		//Add Zero to match Formating
		if (finalDay.length() == 1) {
			finalDay = "0" + finalDay;
		}

		if (finalMonth.length() == 1) {
			finalMonth = "0" + finalMonth;
		}

		String finalDate = finalYear + finalMonth + finalDay;
		newDate = Integer.parseInt(finalDate);

		return newDate;
	}
}
