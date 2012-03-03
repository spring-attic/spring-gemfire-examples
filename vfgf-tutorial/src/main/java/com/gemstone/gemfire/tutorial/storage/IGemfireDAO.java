package com.gemstone.gemfire.tutorial.storage;

import java.util.Set;

import com.gemstone.gemfire.tutorial.model.PostID;
import com.gemstone.gemfire.tutorial.model.Profile;

public interface IGemfireDAO {

	/**
	 * Add a new person
	 * 
	 * @param name
	 *            The name of the person to add.
	 * @param profile
	 *            The profile for this person
	 */
	public abstract void addPerson(String name, Profile profile);

	/**
	 * Add a new post from a particular user.
	 * 
	 * @param author
	 *            the author of the post
	 * @param text
	 *            the text of the post
	 */
	public abstract void addPost(String author, String text);

	/**
	 * Get this list of people in the system
	 */
	public abstract Set<String> getPeople();

	/**
	 * Get the profile of a specific person
	 * 
	 * @param person
	 *            the persons name
	 * @return the profile for the person, or null if the person is not in the
	 *         system
	 */
	public abstract Profile getProfile(String person);

	/**
	 * Get this list of all posts in the system
	 * 
	 * @return A list of the IDs of all of the posts in the system
	 */
	public abstract Set<PostID> getPosts();

	/**
	 * Get a post, given the post ID.
	 * 
	 * @param post
	 *            the post ID
	 * @return the text of the post, or null if the post is not in found
	 */
	public abstract String getPost(PostID post);

}