public class Karma {
	private int linkKarma;
	private int commentKarma;

	public Karma() {
		this.linkKarma = 0;
		this.commentKarma = 0;
	}

	public void upvote(PostType type) {
		//TODO
	}

	public void downvote(PostType type) {
		//TODO
	}

	public int getLinkKarma() {
		return this.linkKarma;
	}

	public int getCommentKarma() {
		return this.commentKarma;
	}
}

