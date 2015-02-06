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
		return new ArrayList<String>(subscribed);
	}

	public List<Post> getPosted() {
		return new ArrayList<Post>(posted);
	}

	public List<Post> getLiked() {
		return new ArrayList<Post>(liked);
	}

	public List<Post> getDisliked() {
		return new ArrayList<Post>(disliked);
	}

	public void subscribe(String subreddit) {
		if(subreddit == null)
			throw new IllegalArgumentException("subreddit");
		if(subscribed.contains(subreddit)) {
			unsubscribe(subreddit);
		}
		else {
			subscribed.add(subreddit);
		}
			
	}

	public void unsubscribe(String subreddit) {
		if(subreddit == null)
			throw new IllegalArgumentException("subreddit");
		subscribed.remove(subreddit);
	}

	public Post addPost(String subreddit, PostType type, String title) {
		Post newPost = new Post(this, subreddit, type, title);
		posted.add(newPost);
		like(newPost);
		return newPost;
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
		post.downvote();
		post.downvote(); // Two downvotes = undoing one upvote
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
		disliked.remove(post);
		post.upvote(); // One upvote + one downvote
		post.downvote(); // == Undoing one downvote
	}
}

