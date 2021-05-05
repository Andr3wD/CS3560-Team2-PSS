package main;

import java.util.Scanner;

/**
 * This handles all user input. No other object should handle direct input from
 * the user except this object.
 *
 */
public class UserHandler {

	private PSS pss;
	private Scanner scan;
	public static boolean running = true;

	public static void main(String[] args) {
		PSS pss = new PSS(); // Init PSS
		UserHandler handler = new UserHandler(pss); // Init the user handler.
                System.out.println("* Enter One of the Commands Below *");
                handler.printHelp(); // Show user Options at Start-up
		handler.handleUser(); // Start handling user.
               
	}

	public UserHandler(PSS pss) {
		this.pss = pss;
	}

	/**
	 * Handles all user input from the terminal (System.in).
	 */
	private void handleUser() {
		// Make scanner for user input.
		scan = new Scanner(System.in);

		// Loop while the application is running
		while (running) {
			String input = scan.nextLine(); // Get next input from user.

			// Hand the commands off to their respective handlers.
			switch (input.toLowerCase()) {
			case "help":
				printHelp();
				break;
			case "createtask":
				pss.createTask(this);
				break;
			case "edittask":
				pss.editTask();
				break;
			case "viewtask":
				pss.viewTask(this);
				break;
			case "deletetask":
				pss.deleteTask(this);
				break;
			case "viewschedule":
				pss.generateSchedule();
				break;
			case "writeschedule":
				pss.writeSchedule();
				break;
			case "loadfile":
				pss.loadSchedule();
				break;
			case "writefile":
				pss.writeSchedule();
				break;
			case "quit":
				running = false;
				System.out.println("Goodbye!");
				break;
			default:
				System.out.println("Unknown command. Input \"help\" to see commands.");
				break;
			}
		}
		scan.close();
	}

	/**
	 * Gets the next String line from the user.
	 * 
	 * @return The next line put in by the user.
	 */
	public String getLine() {
		return scan.nextLine();
	}

	/**
	 * Gets the next float from the user.
	 * 
	 * @return The next float put in by the user.
	 */
	public float getFloat() {
		return scan.nextFloat();
	}

	/**
	 * Gets the next int from the user.
	 * 
	 * @return The next int put in by the user.
	 */
	public int getInt() {
		return scan.nextInt();
	}

	/**
	 * Prints the commands available to the user.
	 */
	private void printHelp() {
		System.out.print("Commands: ");
		System.out.println("createtask, edittask, viewtask, deletetask, viewschedule, writeschedule, loadfile, writefile.");
	}
}
