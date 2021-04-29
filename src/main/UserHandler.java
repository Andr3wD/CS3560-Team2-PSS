package main;

import java.util.Scanner;

/**
 * This handles all user input.
 * 
 * @author Andrew
 *
 */
public class UserHandler {

	private PSS pss;
	private Scanner scan;
	public static boolean running = true;
	
	public static void main(String[] args) {
		PSS pss = new PSS();
		UserHandler handler = new UserHandler(pss);
	}
	
	public UserHandler(PSS pss) {
		this.pss = pss;
		handleUser();
	}

	private void handleUser() {
		scan = new Scanner(System.in);

		while (running) {
			String input = scan.nextLine();
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
				pss.viewTask();
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
	
	public String getLine() {
		return scan.nextLine();
	}
	
	public float getFloat() {
		return scan.nextFloat();
	}
	
	public int getInt() {
		return scan.nextInt();
	}

	private void printHelp() {
		System.out.println("Commands:");
		System.out.println("createtask, edittask, viewtask, deletetask, viewschedule, writeschedule, loadfile, writefile.");
	}
}
