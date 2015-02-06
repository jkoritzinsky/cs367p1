///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             RedditDB.java
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky <jeremy.koritzinsky@wisc.edu>
// CS Login:         koritzinsky
// Lecturer's Name:  Jim Skrentny
//

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * A database of users in our Reddit simulator.
 * @author Jeremy Koritzinsky
 *
 */
public class RedditDB {
	private List<User> users;

	/**
	 * Constructs an empty user database.
	 */
	public RedditDB() {
		this.users = new ArrayList<User>();
	}

	/**
	 * Gets a shallow copy of the list of current users.
	 * @return A shallow copy of the list of current users.
	 */
	public List<User> getUsers() {
		return new ArrayList<User>(users);
	}

	/**
	 * Adds a new user with the given name.
	 * @param name The name of the new user.
	 * @return If the user was successfully created, the new user;
	 *  otherwise, null.
	 */
	public User addUser(String name) {
		if(findUser(name) == null) {
			User newUser = new User(name);
			users.add(newUser);
			return newUser;
		}
		return null;
	}

	/**
	 * Finds a user by name in the database.
	 * @param name The name to search for.
	 * @return If the user is found, the user object; otherwise null.
	 */
	public User findUser(String name) {
		if(name == null)
			throw new IllegalArgumentException("name");
		for(int i = 0; i < users.size(); ++i){
			if(users.get(i).getName().equals(name))
				return users.get(i);
		}
		return null;
	}

	/**
	 * Deletes a user by name.
	 * @param name The name of the user to delete.
	 * @return If the user successfully deleted, true; otherwise, false.
	 */
	public boolean delUser(String name) {
		User userToDelete = findUser(name);
		if(userToDelete == null) {
			return false;
		}
		Iterator<Post> postedIter = userToDelete.getPosted().iterator();
		while(postedIter.hasNext()) {
			Post post = postedIter.next();
			Iterator<User> usersIter = users.iterator();
			while(usersIter.hasNext()) {
				User user = usersIter.next();
				user.undoLike(post);
				user.undoDislike(post);
			}
		}
		Iterator<Post> likedIter = userToDelete.getLiked().iterator();
		while(likedIter.hasNext()) {
			Post likedPost = likedIter.next();
			userToDelete.undoLike(likedPost);
		}
		Iterator<Post> dislikedIter = userToDelete.getDisliked().iterator();
		while(dislikedIter.hasNext()) {
			Post dislikedPost = dislikedIter.next();
			userToDelete.undoDislike(dislikedPost);
		}
		users.remove(userToDelete);
		return true;
	}

	/**
	 * Gets the front page for the user,
	 *  or all posts if the user is null
	 * @param user The user to get the front page for.
	 * @return A list of posts representing the front page.
	 */
	public List<Post> getFrontpage(User user) {
		List<Post> allPosts = new ArrayList<Post>();
		Iterator<User> userIter = users.iterator();
		while(userIter.hasNext()) {
			allPosts.addAll(userIter.next().getPosted());
		}
		if(user == null) return allPosts;
		List<String> subreddits = user.getSubscribed();
		List<Post> liked = user.getLiked();
		List<Post> disliked = user.getDisliked();
		Iterator<Post> postIter = allPosts.iterator();
		List<Post> frontpage = new ArrayList<Post>();
		while(postIter.hasNext()) {
			Post post = postIter.next();
			if(subreddits.contains(post.getSubreddit())) {
				if(post.getUser().equals(user)) {
					frontpage.add(post);
				}
				else {
					if(!liked.contains(post) && !disliked.contains(post)) {
						frontpage.add(post);
					}
				}
			}
		}
		return frontpage;
	}

	/**
	 * Gets the front page for the given user and subreddit.
	 * @param user The user to get the front page for.
	 * @param subreddit The subreddit to get the front page for.
	 * @return A list representing the front page for the user and subreddit.
	 */
	public List<Post> getFrontpage(User user, String subreddit) {
		if(subreddit == null)
			throw new IllegalArgumentException("subreddit");
		List<Post> allPosts = getFrontpage(null);
		Iterator<Post> allPostsIter = allPosts.iterator();
		List<Post> subredditPosts = new ArrayList<Post>();
		while(allPostsIter.hasNext()) {
			Post currentPost = allPostsIter.next();
			if(currentPost.getSubreddit().equals(subreddit)) {
				subredditPosts.add(currentPost);
			}
		}
		if(user == null) 
			return subredditPosts;
		List<Post> liked = user.getLiked();
		List<Post> disliked = user.getDisliked();
		List<Post> frontpage = new ArrayList<Post>();
		Iterator<Post> subredditIter = subredditPosts.iterator();
		while(subredditIter.hasNext()) {
			Post post = subredditIter.next();
			if(post.getUser().equals(user)) {
				frontpage.add(post);
			}
			else {
				if(!liked.contains(post) && !disliked.contains(post)) {
					frontpage.add(post);
				}
			}
		}
		return frontpage;
	}
}

