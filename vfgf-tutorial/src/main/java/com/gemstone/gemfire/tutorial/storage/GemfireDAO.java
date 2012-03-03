package com.gemstone.gemfire.tutorial.storage;

import static com.gemstone.gemfire.cache.client.ClientRegionShortcut.CACHING_PROXY;
import static com.gemstone.gemfire.cache.client.ClientRegionShortcut.PROXY;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.tutorial.model.PostID;
import com.gemstone.gemfire.tutorial.model.Profile;

/**
 * Data Access Object to store people and posts using GemFire. Calling the
 * {@link #initPeer()} method will connect this object to the GemFire
 * distributed system as a GemFire peer. Calling the {@link #initClient()} will
 * connect to the distributed system as a GemFire client.
 * 
 * Once one of the init method is called, the rest of the methods in this DAO
 * can be used to query and update the store.
 * 
 * @author GemStone Systems, Inc.
 */
@Component
public class GemfireDAO implements IGemfireDAO {
	// inject the region people region. After this call completes,
	// this member may have copied the contents of the region
	// from a peer
	@Resource(name = "people")
	private Region<String, Profile> people;

	@Resource(name = "posts")
	private Region<PostID, String> posts;
	private boolean isClient = false;


	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.tutorial.storage.IGemfireDAO#addPerson(java.lang.String, com.gemstone.gemfire.tutorial.model.Profile)
	 */
	public void addPerson(String name, Profile profile) {
		people.put(name, profile);
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.tutorial.storage.IGemfireDAO#addPost(java.lang.String, java.lang.String)
	 */
	public void addPost(String author, String text) {
		PostID id = new PostID(author, System.currentTimeMillis());
		posts.put(id, text);
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.tutorial.storage.IGemfireDAO#getPeople()
	 */
	public Set<String> getPeople() {
		return people.keySet();
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.tutorial.storage.IGemfireDAO#getProfile(java.lang.String)
	 */
	public Profile getProfile(String person) {
		return people.get(person);
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.tutorial.storage.IGemfireDAO#getPosts()
	 */
	public Set<PostID> getPosts() {
		if (isClient) {
			return posts.keySetOnServer();
		} else {
			return posts.keySet();
		}
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.tutorial.storage.IGemfireDAO#getPost(com.gemstone.gemfire.tutorial.model.PostID)
	 */
	public String getPost(PostID post) {
		return posts.get(post);
	}
}
