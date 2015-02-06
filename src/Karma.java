///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             Karma.java
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky <jeremy.koritzinsky@wisc.edu>
// CS Login:         koritzinsky
// Lecturer's Name:  Jim Skrentny
//

/**
 * Tracks a user's karma, separated for links and comments.
 * @author Jeremy Koritzinsky
 *
 */
public class Karma {
	private int linkKarma;
	private int commentKarma;

	/**
	 * Constructs a new Karma object.
	 */
	public Karma() {
		this.linkKarma = 0;
		this.commentKarma = 0;
	}

	/**
	 * Increases the correct karma type by 2.
	 * {@link PostType.SELF} has no karma, so it does not increase karma.
	 * @param type The type of karma to increase.
	 */
	public void upvote(PostType type) {
		switch(type) {
		case COMMENT:
			commentKarma += 2;
			break;
		case LINK:
			linkKarma += 2;
			break;
		default:
			break;
		}
	}

	/**
	 * Decreases the correct karma type by 1.
	 * {@link PostType.SELF} has no karma, so it does not decrease karma.
	 * @param type The type of karma to increase.
	 */
	public void downvote(PostType type) {
		switch(type) {
		case COMMENT:
			commentKarma -= 1;
			break;
		case LINK:
			linkKarma -= 1;
			break;
		default:
			break;
		}
	}

	/**
	 * Gets the current link karma.
	 * @return The current link karma.
	 */
	public int getLinkKarma() {
		return this.linkKarma;
	}

	/**
	 * Gets the current comment karma.
	 * @return The current comment karma.
	 */
	public int getCommentKarma() {
		return this.commentKarma;
	}
}

