import java.util.Iterator;
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
		return true;
	}

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

