package com.gemstone.gemfire.tutorial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.tutorial.model.PostID;
import com.gemstone.gemfire.tutorial.model.Profile;
import com.gemstone.gemfire.tutorial.storage.IGemfireDAO;

/**
 * A command line user interface that manipulates users and posts. This interface
 * reads commands from a given input stream, performs the update or query, and reports
 * the results to a given output stream.
 * 
 * The supported commands are:
 * <ul>
 * <li> add person
 * <li> add post
 * <li> list people
 * <li> list posts
 * <li> quit
 * </ul> 
 * 
 * @author GemStone Systems, Inc.
 */

public class TextUI {
  private final IGemfireDAO dao;
  private final BufferedReader in;
  private final PrintStream out;
  
  public TextUI(IGemfireDAO dao, InputStream in, PrintStream out) {
    this.dao = dao;
    this.in = new BufferedReader(new InputStreamReader(in));
    this.out = out;
  }

  /**
   * Loop, reading commands from the input and processing them,
   * until an exit command is received.
   */
  public void processCommands() throws IOException {
    boolean keepRunning = true;

    usage();
    
    while(keepRunning) {
      System.out.print("> ");
      out.flush();
      String line = in.readLine();
      if(line == null) {
        break;
      }
      
      keepRunning = processLine(line);
    }
  }

  /**
   * Process a single line of input
   * 
   * @param line the line to process.
   * @return true if we should keep reading more commands, false if we should
   *         stop
   */
  private boolean processLine(String line) {
    Scanner scanner = new Scanner(line);

    if(readCommand(scanner, "person") && scanner.hasNext()) {
      String name = scanner.next();
      Profile profile = new Profile();
      while(scanner.hasNext()) {
        String friend = scanner.next();
        profile.addFriend(friend);
      }

      dao.addPerson(name, profile);
    }
    
    else if(readCommand(scanner, "post") && scanner.hasNext()) {
      String author= scanner.next();
      String text = scanner.findInLine(".*");
      if(text != null) {
        dao.addPost(author, text);
      }
    }
    
    else if(readCommand(scanner, "people")) {
      Set<String> people = dao.getPeople();
      for(String person : people) {
        out.println(person);
      }
    }
    
    else if(readCommand(scanner, "posts")) {
      Set<PostID> posts = dao.getPosts();
      for(PostID postID: posts) {
        String text = dao.getPost(postID);
        String author = postID.getAuthor();
        out.println(author + ": " + text);
      }
    }
    else if (readCommand(scanner, "exit|quit|q")) {
      return false;
    }
    else {
      usage();
    }
    
    return true;
  }

  /**
   * Display a usage message.
   */
  private void usage() {
    out.println("Commands:");
    out.println("person [name] [friend] [friend] ...");
    out.println("     Add a person. The person's name and friends cannot contain spaces.");
    out.println("post [author] [text]");
    out.println("     Add a post.");
    out.println("people");
    out.println("     List all people.");
    out.println("posts");
    out.println("     List all posts.");
    out.println("quit");
    out.println("     Exit the shell.");
  }

  /**
   * Check to see if the given command is available in the input, and if
   * so, read it from the scanner. 
   */
  private boolean readCommand(Scanner scanner, String command) {
    if(scanner.hasNext(command)) {
      scanner.next(command);
      return true;
    }
    return false;
  }

}
