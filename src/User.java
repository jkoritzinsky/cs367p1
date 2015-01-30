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
		//TODO
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
		//TODO
	}

	public void like(Post post) {
		//TODO
	}

	public void undoLike(Post post) {
		//TODO
	}

	public void dislike(Post post) {
		//TODO
	}

	public void undoDislike(Post post) {
		//TODO
	}
}

