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
		if(findUser(name) == null) {
			User newUser = new User(name);
			users.add(newUser);
			return newUser;
		}
		return null;
	}

	public User findUser(String name) {
		if(name == null)
			throw new IllegalArgumentException("name");
		for(int i = 0; i < users.size(); ++i){
			if(users.get(i).getName().equals(name))
				return users.get(i);
		}
		return null;
	}

	public boolean delUser(String name) {
		User userToDelete = findUser(name);
		if(userToDelete == null) {
			return false; //TODO: Post on Piazza and figure out exactly what to return
		}
		//TODO: Delete all user data recursively
	}

	public List<Post> getFrontpage(User user) {
		if(user == null) {
			//TODO: Get all posts
		}
		List<String> subreddits = user.getSubscribed();
		List<Post> frontPagePosts = new ArrayList<Post>();
		for (int i = 0; i < subreddits.size(); ++i) {
			frontPagePosts.addAll(getFrontpage(user, subreddits.get(i)));
		}
		return frontPagePosts;
	}

	public List<Post> getFrontpage(User user, String subreddit) {
		if(user == null) {
			//TODO: Get all posts for subreddit
		}
		//TODO: Get frontpage for user for subreddit
	}
}

