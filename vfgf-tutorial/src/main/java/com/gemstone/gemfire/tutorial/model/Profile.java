package com.gemstone.gemfire.tutorial.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gemstone.bp.edu.emory.mathcs.backport.java.util.Collections;

/**
 * A profile for a user of the gemstone social networking application. The
 * profile contains the list of friends of this person.
 * 
 * Profiles are used as the value in the people region. They will be distributed
 * to other VMs, so they must be serializable.
 * 
 * @author GemStone Systems, Inc.
 */
@Component
public class Profile implements Serializable {
  private static final long serialVersionUID = -3569243165627161127L;
  
  private final Set<String> friends = new HashSet<String>();
  
  public boolean addFriend(String name) {
    return this.friends.add(name);
  }
  
  public boolean removeFriend(String name) {
    return this.friends.remove(name);
  }
  
  public Set<String> getFriends() {
    return Collections.unmodifiableSet(friends);
  }

  @Override
  public String toString() {
    return "Profile [friends=" + friends + "]";
  }
  
  
}
