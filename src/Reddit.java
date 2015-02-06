///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Reddit Simulator
// Files:            Reddit.java, Post.java, User.java, Karma.java, RedditDB.java
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky
// Email:            jeremy.koritzinsky@wisc.edu
// CS Login:         koritzinsky
// Lecturer's Name:  Jim Skrentny
//

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;


/**
 * The main class for the Reddit simulator
 * @author Jeremy Koritzinsky
 *
 */
public class Reddit {
	private static RedditDB db;
	/**
	 * Entry point for the program
	 * @param args The given command-line arguments.
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Usage: java Reddit <FileNames>");
			return;
		}
		db = new RedditDB();
		db.addUser("admin");
		for(int i = 0; i < args.length; ++i) {
			File currentFile = new File(args[i]);
			if(!currentFile.exists()) {
				System.out.println("File " + args[i] + "not found.");
				return;
			}
			try (Scanner fileIn = new Scanner(currentFile)) {
				String name = currentFile.getName().toLowerCase();
				int pos = name.lastIndexOf(".");
				if (pos > 0) {
				    name = name.substring(0, pos);
				}
				User newUser = db.addUser(name);
				String subscriptionLine = fileIn.nextLine();
				String[] subscriptions = subscriptionLine.split(",");
				for(int j = 0; j < subscriptions.length; ++j) {
					newUser.subscribe(subscriptions[j].trim().toLowerCase());
				}
				while(fileIn.hasNextLine()) {
					String[] postData = fileIn.nextLine().split(",", 3);
					String subreddit = postData[0].trim().toLowerCase();
					PostType type = Enum.valueOf(PostType.class, postData[1].trim());
					String title = postData[2];
					newUser.addPost(subreddit, type, title);
				}
			}
			catch (FileNotFoundException e) {
				/* Extremely unlikely to trigger unless the file currentFile references is
				 * deleted between the if statement and the scanner creation, but because
				 * it is a checked exception and there is still that small chance, we
				 * still have to catch it. 
				*/
				System.out.println("File " + args[i] + "not found.");
				return;
			}
		}
		runMainMenuPrompt();
		
	}
	
	/**
	 * Runs a loop that displays interacts with the main menu.
	 */
	private static void runMainMenuPrompt() {
		User currentUser = null;
		Scanner scanner = new Scanner(System.in);
		while(true) {
			displayInputPrompt(currentUser);
			String entry = scanner.nextLine();
			String[] splitEntry = entry.split(" ");
			String command = splitEntry[0];
			String argument = "";
			if(commandAlwaysHasArgument(command)) {
				if(splitEntry.length == 1) {
					System.out.print("Invalid command!");
					continue;
				}
				argument = splitEntry[1];
			}
			else if(commandHasOptionalArgument(command)) {
				if(splitEntry.length == 1) {
					argument = "";
				}
				else {
					argument = splitEntry[1];
				}
			}
			if(isAdminOnlyCommand(command)) {
				if(currentUser == null || currentUser.getName() != "admin") {
					System.out.println("Invalid command!");
					continue;
				}
			}
			switch(command) {
			case "s":
				Iterator<User> userIter = db.getUsers().iterator();
				StringBuilder builder = new StringBuilder();
				while(userIter.hasNext()) {
					User user = userIter.next();
					builder.append(user.getName()).append('\t');
					builder.append(user.getKarma().getLinkKarma()).append('\t');
					builder.append(user.getKarma().getCommentKarma()).append('\n');
				}
				System.out.print(builder.toString());
				break;
			case "d":
				db.delUser(argument.toLowerCase());
				System.out.println("User " + argument.toLowerCase() + " deleted.");
				break;
			case "l":
				currentUser = doLoginAndLogout(currentUser, argument.toLowerCase());
				break;
			case "f":
				Iterator<Post> frontpageIter = db.getFrontpage(currentUser).iterator();
				System.out.println("Displaying the front page...");
				runSubmenu(frontpageIter, currentUser, scanner);
				break;
			case "r":
				Iterator<Post> subredditIter = db.getFrontpage(currentUser, argument.toLowerCase()).iterator();
				System.out.println("Displaying /r/" + argument.toLowerCase() + "...");
				runSubmenu(subredditIter, currentUser, scanner);
				break;
			case "u":
				User targetUser = db.findUser(argument);
				if(targetUser == null) {
					System.out.println("Invalid Command!");
					break;
				}
				Iterator<Post> userPostsIter = targetUser.getPosted().iterator();
				System.out.println("Displaying /u/" + argument.toLowerCase() + "...");
				runSubmenu(userPostsIter, currentUser, scanner);
				break;
			case "x":
				System.out.println("Exiting to the real world...");
				scanner.nextLine();
				return;
			default:
				System.out.println("Invalid command!");
				break;
			}
		}
	}
	
	/**
	 * Displays the input prompt for the current user.
	 * @param currentUser The current user
	 */
	private static void displayInputPrompt(User currentUser) {
		if(currentUser == null) {
			System.out.print("[anon@reddit]$ ");
		}
		else {
			System.out.print("[" + currentUser.getName() + "@reddit]$ ");
		}
	}
	
	/**
	 * Runs the submenu for the given collection of posts.
	 * @param postIter An iterator of a collection of posts.
	 * @param currentUser The current user.
	 * @param scanner A scanner that is connected to user input.
	 */
	private static void runSubmenu(Iterator<Post> postIter, User currentUser, Scanner scanner) {
		while(postIter.hasNext()) {
			boolean moveToNext = true;
			Post currentPost = postIter.next();
			do {
				moveToNext = true;
				System.out.println(currentPost.getKarma() + "\t" + currentPost.getTitle().trim());
				displayInputPrompt(currentUser);
				String command = scanner.nextLine();
				switch(command) {
				case "a":
					if(currentUser == null) {
						System.out.println("Login to like post.");
						moveToNext = false;
					}
					else {
						currentUser.like(currentPost);
					}
					break;
				case "z":
					if(currentUser == null) {
						System.out.println("Login to dislike post.");
						moveToNext = false;
					}
					else {
						currentUser.dislike(currentPost);
					}
					break;
				case "j":
					// Do nothing and move to next post
					break;
				case "x":
					System.out.println("Exiting to the main menu...");
					return;
				default:
					System.out.println("Invalid command!");
					moveToNext = false;
					break;
				}
			} while(!moveToNext);
		}
		System.out.println("No posts left to display.");
		System.out.println("Exiting to the main menu...");
	}
	
	/**
	 * Runs the login and logout commands.
	 * @param currentUser The current user.
	 * @param argument The name of the user to login or ""
	 * @return null if the user has been logged out, or the newly logged in user.
	 */
	private static User doLoginAndLogout(User currentUser, String argument) {
		if(argument.equals("")) { // Logout
			if(currentUser != null) {
				System.out.println("User " + currentUser.getName() + " logged out.");
				currentUser = null;
			}
			else {
				System.out.println("No user logged in.");
			}
		}
		else { // Login
			if(currentUser != null) {
				System.out.println("User " + currentUser.getName() + " already logged in.");
			}
			else {
				currentUser = db.findUser(argument);
				if(currentUser == null) {
					System.out.println("User " + argument + " not found.");
				}
				else {
					System.out.println("User " + currentUser.getName() + " logged in.");
				}
			}
		}
		return currentUser;
	}
	
	/**
	 * Returns true if the command always requires an argument.
	 * @param command The command to test.
	 * @return true if the command always requires an argument; otherwise, false.
	 */
	private static boolean commandAlwaysHasArgument(String command) {
		return command.equals("d") || command.equals("r") || command.equals("u");
	}
	
	/**
	 * Returns true if the command has optional arguments.
	 * @param command The command to test.
	 * @return true if the command has optional arguments; otherwise, false.
	 */
	private static boolean commandHasOptionalArgument(String command) {
		return command.equals("l");
	}
	
	/**
	 * Returns true if the command can only be run by admin.
	 * @param command The command to test.
	 * @return true if the command can only be run by admin; otherwise, false.
	 */
	private static boolean isAdminOnlyCommand(String command) {
		return command.equals("s") || command.equals("d");
	}

}
