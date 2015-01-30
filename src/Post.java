public class Post {
	final private User user;
	final private String subreddit;
	final private PostType type;
	final private String title;
	private int karma;

	public Post(User user, String subreddit, PostType type, String title) {
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		karma = 0;
	}

	public void upvote() {
		karma += 2;
		user.getKarma().upvote(type);
	}

	public void downvote() {
		karma -= 1;
		user.getKarma().downvote(type);
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

