import java.util.List;
import java.util.ArrayList;

public class RedditDB {
	private List<User> users;

	public RedditDB() {
		this.users = new ArrayList<User>();
	}

	public List<User> getUsers() {
		return new ArrayList<User>(users);
	}

	public User addUser(String name) {
		//TODO
	}

	public User findUser(String name) {
		//TODO
	}

	public boolean delUser(String name) {
		//TODO
	}

	public List<Post> getFrontpage(User user) {
		//TODO
	}

	public List<Post> getFrontpage(User user, String subreddit) {
		//TODO
	}
}

