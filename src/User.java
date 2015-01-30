import java.util.List;
import java.util.ArrayList;

public class User {
	final private String name;
	final private Karma karma;
	private List<String> subscribed;
	private List<Post> posted;
	private List<Post> liked;
	private List<Post> disliked;

	public User(String name) {
		this.name = name;
		this.karma = new Karma();
		subscribed = new ArrayList<String>();
		posted = new ArrayList<Post>();
		liked = new ArrayList<Post>();
		disliked = new ArrayList<Post>();
	}

	public String getName() {
		return this.name;
	}

	public Karma getKarma() {
		return this.karma;
	}

	public List<String> getSubscribed() {
		//TODO
	}

	public List<Post> getPosted() {
		//TODO
	}

	public List<Post> getLiked() {
		//TODO
	}

	public List<Post> getDisliked() {
		//TODO
	}

	public void subscribe(String subreddit) {
		//TODO
	}

	public void unsubscribe(String subreddit) {
		//TODO
	}

	public Post addPost(String subreddit, PostType type, String title) {
		return new Post(this, subreddit, type, title);
	}

	public void like(Post post) {
		if(post == null)
			throw new IllegalArgumentException("post");
		if(liked.contains(post)) {
			undoLike(post);
			return;
		}
		else if(disliked.contains(post)) {
			undoDislike(post);
		}
		liked.add(post);
		post.upvote();
	}

	public void undoLike(Post post) {
		if(post == null)
			throw new IllegalArgumentException("post");
		liked.remove(post);
	}

	public void dislike(Post post) {
		if(post == null)
			throw new IllegalArgumentException("post");
		if(disliked.contains(post)) {
			undoDislike(post);
			return;
		}
		else if(liked.contains(post)) {
			undoLike(post);
		}
		disliked.add(post);
		post.downvote();
	}

	public void undoDislike(Post post) {
		if(post == null)
			throw new IllegalArgumentException("post");
		//TODO
	}
}

