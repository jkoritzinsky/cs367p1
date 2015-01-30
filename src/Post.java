public class Post {
	final private User user;
	final private String subreddit;
	final private PostType type;
	final private String title;
	private int karma;

	public Post(User user, String subreddit, PostType type, String title) {
		//TODO
	}

	public void upvote() {
		//TODO
	}

	public void downvote() {
		//TODO
	}

	public User getUser() {
		return this.user;
	}

	public String getSubreddit() {
		return this.subreddit;
	}

	public PostType getType() {
		return this.type;
	}

	public String getTitle() {
		return this.title;
	}

	public int getKarma() {
		return this.karma;
	}
}

