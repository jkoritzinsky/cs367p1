//////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Reddit Simulator
// Files:            Reddit.java, Post.java, User.java, Karma.java,
//						RedditDB.java
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
 * 
 * @author Jeremy Koritzinsky
 *
 */
public class Reddit {
	/** An instance of RedditDB to hold our users. */
	private static RedditDB db;

	/**
	 * Entry point for the program
	 * 
	 * @param args The given command-line arguments.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java Reddit <FileNames>");
			return;
		}
		db = new RedditDB();
		db.addUser("admin");
		for (int i = 0; i < args.length; ++i) {
			File currentFile = new File(args[i]);
			// Although I have to catch the exception if the file does not exist
			// anyway, I check here if the file exists because exceptions are
			// slower than regular execution, so I try to use if-statement
			// checks on conditions to preempt and avoid an exception throw
			// where I can.
			if (!currentFile.exists()) {
				System.out.println("File " + args[i] + "not found.");
				return;
			}
			try (Scanner fileIn = new Scanner(currentFile)) {
				String name = currentFile.getName().toLowerCase(); // Get
																	// filename
				int pos = name.lastIndexOf(".");
				if (pos > 0) {
					name = name.substring(0, pos); // Remove extension off
													// filename to get user
													// name.
				}
				User newUser = db.addUser(name.toLowerCase());
				// Normalize username to lower-case
				if (fileIn.hasNextLine()) {
					String subscriptionLine = fileIn.nextLine();
					// First line is subreddit subscriptions
					String[] subscriptions = subscriptionLine.split(",");
					for (int j = 0; j < subscriptions.length; ++j) {
						// Remove extraneous whitespace from each end of the
						// subreddit name and normalize as lower-case.
						newUser
							.subscribe(subscriptions[j].trim().toLowerCase());
					}
				}
				while (fileIn.hasNextLine()) {
					// Limit to 3 pieces to preserve commas in post title.
					String[] postData = fileIn.nextLine().split(",", 3);
					String subreddit = postData[0].trim().toLowerCase();
					PostType type = Enum.valueOf(PostType.class,
						postData[1].trim()); // Use built-in Enum class to
												// parse PostType.
					String title = postData[2].trim();
					newUser.addPost(subreddit, type, title);
				}
			}
			catch (FileNotFoundException e) {
				/*
				 * Extremely unlikely to trigger unless the file currentFile
				 * references is deleted between the if statement and the
				 * scanner creation, but because it is a checked exception and
				 * there is still that small chance, we have to catch it.
				 */
				System.out.println("File " + args[i] + "not found.");
				return;
			}
		}
		runMainMenuPrompt();

	}

	/**
	 * Runs a loop that displays and runs interactions with the main menu.
	 */
	private static void runMainMenuPrompt() {
		User currentUser = null; // User object for currently logged in user.
									// null represents an anonymous
									// (not logged in) user
		Scanner scanner = new Scanner(System.in);
		boolean exitMainMenu = false;
		while (!exitMainMenu) {
			displayInputPrompt(currentUser);
			String entry = scanner.nextLine();
			String[] splitEntry = entry.split(" "); // Commands and arguments
													// are separated by a space,
													// so split on the space
			String command = splitEntry[0]; // Command to execute
			String argument = ""; // Default value for command argument
			if (commandAlwaysHasArgument(command)) {
				if (splitEntry.length == 1) {
					System.out.print("Invalid command!");
					continue;
				}
				argument = splitEntry[1];
			}
			else if (commandHasOptionalArgument(command)) {
				if (splitEntry.length == 1) {
					argument = ""; // Set the argument to a sentinel value for
									// commands that have an optional argument
				}
				else {
					argument = splitEntry[1];
				}
			}
			if (isAdminOnlyCommand(command)) {
				if (currentUser == null || currentUser.getName() != "admin") {
					System.out.println("Invalid command!");
					continue;
				}
			}
			switch (command) {
			case "s":
				Iterator<User> userIter = db.getUsers().iterator();
				StringBuilder builder = new StringBuilder();
				// StringBuilder to create post output and save memory from
				// excessive string concatenation.
				while (userIter.hasNext()) {
					User user = userIter.next();
					builder.append(user.getName()).append('\t');
					builder.append(user.getKarma().getLinkKarma()).append('\t');
					builder.append(user.getKarma().getCommentKarma()).append(
						'\n');
				}
				System.out.print(builder.toString());
				break;
			case "d": // argument is the username to delete
				db.delUser(argument.toLowerCase());
				System.out.println("User " + argument.toLowerCase()
					+ " deleted.");
				break;
			case "l":
				currentUser = doLoginAndLogout(currentUser,
					argument.toLowerCase());
				break;
			case "f":
				Iterator<Post> frontpageIter = db.getFrontpage(currentUser)
					.iterator(); // Get an iterator for the posts for the
									// front page
				System.out.println("Displaying the front page...");
				runSubmenu(frontpageIter, currentUser, scanner);
				break;
			case "r":
				// Get an iterator for the posts for the subreddit
				Iterator<Post> subredditIter = db.getFrontpage(currentUser,
					argument.toLowerCase()).iterator();
				System.out.println("Displaying /r/" + argument.toLowerCase()
					+ "...");
				runSubmenu(subredditIter, currentUser, scanner);
				break;
			case "u":
				User targetUser = db.findUser(argument); // Get user to display
															// posts for
				if (targetUser == null) { // If user does not exist
					System.out.println("Invalid Command!");
					break;
				}
				Iterator<Post> userPostsIter = targetUser.getPosted()
					.iterator(); // Get iterator for all posts by user
				System.out.println("Displaying /u/" + argument.toLowerCase()
					+ "...");
				runSubmenu(userPostsIter, currentUser, scanner);
				break;
			case "x":
				System.out.println("Exiting to the real world...");
				scanner.nextLine(); // Poll user for a newline to keep the
									// program open for them to see the final
									// output.
				exitMainMenu = true;
			default:
				System.out.println("Invalid command!");
				break;
			}
		}
	}

	/**
	 * Displays the input prompt for the current user.
	 * 
	 * @param currentUser The current user
	 */
	private static void displayInputPrompt(User currentUser) {
		if (currentUser == null) {
			System.out.print("[anon@reddit]$ ");
		}
		else {
			System.out.print("[" + currentUser.getName() + "@reddit]$ ");
		}
	}

	/**
	 * Runs the submenu for the given collection of posts.
	 * 
	 * @param postIter An iterator of a collection of posts.
	 * @param currentUser The current user.
	 * @param scanner A scanner that is connected to user input.
	 */
	private static void runSubmenu(Iterator<Post> postIter, User currentUser,
		Scanner scanner) {
		while (postIter.hasNext()) {
			boolean moveToNext = true; // Tracks whether or not to move to the
										// next value (false when an invalid
										// command is entered).
			Post currentPost = postIter.next();
			do { // Nested do-while loop allows a post to be re-displayed if an
					// invalid command is entered.
				moveToNext = true;
				System.out.println(currentPost.getKarma() + "\t"
					+ currentPost.getTitle());
				displayInputPrompt(currentUser);
				String command = scanner.nextLine(); // The command for the
														// submenu
				switch (command) {
				case "a":
					if (currentUser == null) {
						System.out.println("Login to like post.");
						moveToNext = false;
					}
					else {
						currentUser.like(currentPost);
					}
					break;
				case "z":
					if (currentUser == null) {
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
			} while (!moveToNext);
		}
		System.out.println("No posts left to display.");
		System.out.println("Exiting to the main menu...");
	}

	/**
	 * Runs the login and logout commands.
	 * 
	 * @param currentUser The current user.
	 * @param newUserName The name of the user to login or ""
	 * @return null if the user has been logged out, the newly logged in user on
	 *         successful login, or the currently logged in user on login
	 *         failure.
	 */
	private static User doLoginAndLogout(User currentUser, String newUserName) {
		if (newUserName.equals("")) { // Logout
			if (currentUser != null) {
				System.out.println("User " + currentUser.getName()
					+ " logged out.");
				currentUser = null;
			}
			else {
				System.out.println("No user logged in.");
			}
		}
		else { // Login
			if (currentUser != null) {
				System.out.println("User " + currentUser.getName()
					+ " already logged in.");
			}
			else {
				currentUser = db.findUser(newUserName);
				if (currentUser == null) {
					System.out.println("User " + newUserName + " not found.");
				}
				else {
					System.out.println("User " + currentUser.getName()
						+ " logged in.");
				}
			}
		}
		return currentUser;
	}

	/**
	 * Returns true if the command always requires an argument.
	 * 
	 * @param command The command to test.
	 * @return true if the command always requires an argument; otherwise,
	 *         false.
	 */
	private static boolean commandAlwaysHasArgument(String command) {
		return command.equals("d") || command.equals("r")
			|| command.equals("u");
	}

	/**
	 * Returns true if the command has optional arguments.
	 * 
	 * @param command The command to test.
	 * @return true if the command has optional arguments; otherwise, false.
	 */
	private static boolean commandHasOptionalArgument(String command) {
		return command.equals("l");
	}

	/**
	 * Returns true if the command can only be run by admin.
	 * 
	 * @param command The command to test.
	 * @return true if the command can only be run by admin; otherwise, false.
	 */
	private static boolean isAdminOnlyCommand(String command) {
		return command.equals("s") || command.equals("d");
	}

}
