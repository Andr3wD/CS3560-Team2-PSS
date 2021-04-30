package main;

import java.util.ArrayList;
import java.util.Scanner;
import org.w3c.dom.ranges.Range;

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

    /**
     * Will take in user input for the name of the Task they want to delete
     * 
     * If the task is found in the schedule array:
     *      - If the task is recurring then delete any anti-tasks associated with it
     *      - If its an anti-task check for conflicts.If conflicts are found
     *          return the names of the conflicting tasks.
     * note: Anti-Task are only used to cancel recurring tasks.
     * 
     * If task is not found: Message will let the user know.
     * 
     * @param handler used to capture user input.
     */
    public void deleteTask(UserHandler handler) {
            System.out.println("Enter the name of the task you want to Delete:");
            Scanner input = new Scanner(System.in);
            String taskName = handler.getLine();
            Task targetTask;
            
            if((schedule == null) || schedule.isEmpty()){
                System.out.println("This Schedule is empty.");
            }//Checks if the schdule has not been initialized
            else{
                for(int i = 0; i < schedule.size(); i++){
                    if(taskName.equals(schedule.get(i).getName())){
                        targetTask = schedule.get(i);
                        
                        if(targetTask.getTaskType() == Task.TaskType.RECURRING){
                            System.out.print(taskName + " Has been deleted scuessfully");
                            deleteAntiTasks(targetTask);
                            schedule.remove(i);
                                                       
                            return;
                        }//End Options for Recurring task
                        else if (targetTask.getTaskType() == Task.TaskType.ANTI){
                            if(!checkForConflicts(targetTask)){
                                schedule.remove(i);
                                System.out.println("Anti-Task deleted sucessfully.");
                                return;
                            }//checkForConflicts if none remove anti-task
                            else{
                                return;
                            }// if conflicts found exit. checkForConflicts will provide output messege
                        }
                        else{
                            schedule.remove(i);
                            System.out.println(taskName + " Has been deleted scuessfully");
                            return;
                        }//For Transient Tasks 
                    }//If names match
                }//Search through Schedule list
                
                System.out.println("No Task Found with that Name returning to menu.");
            }//end else schedule exisits
	}//end deleteTask()

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

        /**
         * Deletes Anti-Tasks with same date, startTime and duration as RecurringTask
         * 
         * @param recurringTask the recurringTask we want to delete
         */
        private void deleteAntiTasks(Task recurringTask) {
            Task foundAntiTask;
            
            for(int i = 0;i < schedule.size(); i++){
                if(schedule.get(i).getTaskType() == Task.TaskType.ANTI){
                    foundAntiTask = schedule.get(i);
                    
                    if(foundAntiTask.getDate() == recurringTask.getDate()){
                        if(foundAntiTask.getStartTime() == recurringTask.getStartTime()){
                            if(foundAntiTask.getDuration() == recurringTask.getDuration()){
                                System.out.println(" along with its Anti-Tasks.");
                                schedule.remove(i);
                            }//check for matching Duration
                        }//check for matching starttimes
                    }//check for matching dates
                }//end check for anti task in schedule
            }
        }//end deleteAntiTasks()

    /**
     * Checks if any tasks (Transient or Recurring) will overlap if the anti task is deleted.
     * 
     * First finds the Corresponding Recurring Task to the AntiTask.
     * 
     * When found call Overlap method to check if there is any Overlap with other tasks.
     * 
     * @param targetTask anti-task that is passed in.
     * 
     * @return false if no conflicts. True and print conflicting task if found
     */
    private boolean checkForConflicts(Task targetTask) {
        boolean conflictFound = false;
        int targetDate = targetTask.getDate();
        float targetStartTime = targetTask.getStartTime();
        float targetDuration = targetTask.getDuration();
        Task matchingTask;//Task that matches the time frame of Anti task.
        
        for(int i = 0; i < schedule.size(); i++){
            matchingTask = schedule.get(i);
            
            if((matchingTask.getDate() == targetDate) && 
                    ((targetStartTime + targetDuration) == (matchingTask.getStartTime() + matchingTask.getDuration())) &&
                    matchingTask.getTaskType() == Task.TaskType.RECURRING){
                
                if(checkOverlap(matchingTask)){
                    return true;
                }//Conflict Overlap found
            }// find corresponding task.
        }
        
        return conflictFound;
    }//end checkForConflicts

    /**
     * Check if there is a date and time overlap between other tasks in the schedule with this recurring task.
     * 
     * If conflicts found put them in a list and print them out.
     * 
     * @return false if no overlaps were found. True if Overlaps found
     */
    private boolean checkOverlap(Task recurringTask) {
        ArrayList<Task> conflicts = new ArrayList<Task>();
        boolean overlapFound = false;
        int startDate = ((RecurringTask) recurringTask).getStartDate();
        int endDate = ((RecurringTask) recurringTask).getEndDate();
        float startTime = recurringTask.getStartTime();
        float endTime = recurringTask.getStartTime() + recurringTask.getDuration();
        
        for(int i = 0; i < schedule.size(); i++){
            if(startDate < schedule.get(i).getDate() && endDate > schedule.get(i).getDate()){
                float taskTime = schedule.get(i).getStartTime() + schedule.get(i).getDuration();
                if(startTime < taskTime && endTime > taskTime){
                    conflicts.add(schedule.get(i));//Conflict found add to list
                    overlapFound = true;
                }//Check if time overlap
            }//Check if in date range
        }
        
        if(overlapFound){
            System.out.println("Cannot be delete becaouse of conflicting tasks: ");
            
            for(int i = 0; i < conflicts.size(); i++){
                System.out.print(conflicts.get(i).getName() + " , ");
            }//Print list of conflicting tasks
        }//If Overlap found print tasks causing conflicts
        
        return overlapFound;
    }//end CheckOverlap
}
