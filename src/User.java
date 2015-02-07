///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             User.java
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky <jeremy.koritzinsky@wisc.edu>
// CS Login:         koritzinsky
// Lecturer's Name:  Jim Skrentny
//

import java.util.List;
import java.util.ArrayList;

/**
 * A user in our Reddit simulation
 * 
 * @author Jeremy Koritzinsky
 *
 */
public class User {
	/** Name of the user */
	final private String name;
	/** Amount of karma the user has */
	final private Karma karma;
	/** Subreddits the user is subscribed to */
	private List<String> subscribed;
	/** Posts the user has posted */
	private List<Post> posted;
	/** Posts the user has liked */
	private List<Post> liked;
	/** Posts the user has disliked */
	private List<Post> disliked;

	/**
	 * Constructs a user with their name
	 * 
	 * @param name The name of the new user.
	 */
	public User(String name) {
		if (name == null)
			throw new IllegalArgumentException("name");
		this.name = name;
		this.karma = new Karma();
		subscribed = new ArrayList<String>();
		posted = new ArrayList<Post>();
		liked = new ArrayList<Post>();
		disliked = new ArrayList<Post>();
	}

	/**
	 * Gets the name of the user.
	 * 
	 * @return The name of the user
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the {@link Karma} of the user.
	 * 
	 * @return The karma of the user.
	 */
	public Karma getKarma() {
		return this.karma;
	}

	/**
	 * Gets a shallow copy of the subreddits that the user is subscribed to.
	 * 
	 * @return A list of subreddits this user is subscribed to.
	 */
	public List<String> getSubscribed() {
		return new ArrayList<String>(subscribed);
	}

	/**
	 * Gets a shallow copy of the list of posts this user has posted.
	 * 
	 * @return A list of the posts this user has posted.
	 */
	public List<Post> getPosted() {
		return new ArrayList<Post>(posted);
	}

	/**
	 * Gets a shallow copy of the list of posts this user has liked.
	 * 
	 * @return A list of the posts this user has liked.
	 */
	public List<Post> getLiked() {
		return new ArrayList<Post>(liked);
	}

	/**
	 * Gets a shallow copy of the list of posts this user has disliked.
	 * 
	 * @return A list of the posts this user has disliked.
	 */
	public List<Post> getDisliked() {
		return new ArrayList<Post>(disliked);
	}

	/**
	 * Toggles this user's subscription to a subreddit.
	 * 
	 * @param subreddit The subreddit to toggle subscription to.
	 */
	public void subscribe(String subreddit) {
		if (subreddit == null)
			throw new IllegalArgumentException("subreddit");
		if (subscribed.contains(subreddit)) {
			unsubscribe(subreddit);
		}
		else {
			subscribed.add(subreddit);
		}

	}

	/**
	 * Unsubscribes this user from the specified subreddit
	 * 
	 * @param subreddit The subreddit to unsubscribe from.
	 */
	public void unsubscribe(String subreddit) {
		if (subreddit == null)
			throw new IllegalArgumentException("subreddit");
		subscribed.remove(subreddit);
	}

	/**
	 * Creates and likes a new post.
	 * 
	 * @param subreddit The subreddit the post is in
	 * @param type The type of post
	 * @param title The post title
	 * @return The new post
	 */
	public Post addPost(String subreddit, PostType type, String title) {
		if (subreddit == null)
			throw new IllegalArgumentException("subreddit");
		if (type == null)
			throw new IllegalArgumentException("type");
		if (title == null)
			throw new IllegalArgumentException("title");
		Post newPost = new Post(this, subreddit, type, title); // New post from
																// parameters
		posted.add(newPost);
		like(newPost);
		return newPost;
	}

	/**
	 * Toggles a like on a post. If this post is disliked, the dislike is
	 * removed before the post is liked.
	 * 
	 * @param post The post to like.
	 */
	public void like(Post post) {
		if (post == null)
			throw new IllegalArgumentException("post");
		if (liked.contains(post)) {
			undoLike(post);
			return;
		}
		else if (disliked.contains(post)) {
			undoDislike(post);
		}
		liked.add(post);
		post.upvote();
	}

	/**
	 * Unlikes a post
	 * 
	 * @param post The post to unlike
	 */
	public void undoLike(Post post) {
		if (post == null)
			throw new IllegalArgumentException("post");
		liked.remove(post);
		post.downvote();
		post.downvote(); // Two downvotes = undoing one upvote
	}

	/**
	 * Toggles a dislike on a post. If this post is liked, the like is removed
	 * before the post is disliked.
	 * 
	 * @param post The post to like.
	 */
	public void dislike(Post post) {
		if (post == null)
			throw new IllegalArgumentException("post");
		if (disliked.contains(post)) {
			undoDislike(post);
			return;
		}
		else if (liked.contains(post)) {
			undoLike(post);
		}
		disliked.add(post);
		post.downvote();
	}

	/**
	 * Undislikes a post
	 * 
	 * @param post The post to undislike
	 */
	public void undoDislike(Post post) {
		if (post == null)
			throw new IllegalArgumentException("post");
		disliked.remove(post);
		post.upvote(); // One upvote + one downvote
		post.downvote(); // == Undoing one downvote
	}
}
