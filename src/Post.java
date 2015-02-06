///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             Post.java
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky <jeremy.koritzinsky@wisc.edu>
// CS Login:         koritzinsky
// Lecturer's Name:  Jim Skrentny
//

/**
 * A post in our Reddit simulator
 * @author Jeremy Koritzinsky
 *
 */
public class Post {
	final private User user;
	final private String subreddit;
	final private PostType type;
	final private String title;
	private int karma;

	/**
	 * Constructs a post with an author, subreddit, post type, and title
	 * @param user The author
	 * @param subreddit The subreddit the post is in
	 * @param type The type of post
	 * @param title The title of the post
	 */
	public Post(User user, String subreddit, PostType type, String title) {
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		karma = 0;
	}

	/**
	 * Upvotes this post.
	 * Increases the karma of this post and of the author (depending on type) by 2.
	 */
	public void upvote() {
		karma += 2;
		user.getKarma().upvote(type);
	}

	/**
	 * Downvotes this post.
	 * Decreases the karma of this post and of the author (depending on type) by 1.
	 */
	public void downvote() {
		karma -= 1;
		user.getKarma().downvote(type);
	}

	/**
	 * Gets the author of the post.
	 * @return The author of the post
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Gets the subreddit the post is in.
	 * @return The subreddit the post is in
	 */
	public String getSubreddit() {
		return this.subreddit;
	}

	/**
	 * Gets the type of post.
	 * @return The type of post
	 */
	public PostType getType() {
		return this.type;
	}

	/**
	 * Gets the title of the post.
	 * @return The title of the post
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Gets the current karma of the post.
	 * @return The current karma of the post
	 */
	public int getKarma() {
		return this.karma;
	}
}

