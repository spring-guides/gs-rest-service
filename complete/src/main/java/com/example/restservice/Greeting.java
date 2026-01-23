package com.example.restservice;

/**
 * Immutable data record representing a greeting response.
 * 
 * <h2>JML Specification:</h2>
 * This record encapsulates the response of a greeting request, containing
 * a unique identifier and a personalized message.
 * 
 * <h3>Invariants:</h3>
 * <ul>
 *   <li>id must be positive (> 0)</li>
 *   <li>content must never be null</li>
 *   <li>content must follow the format "Hello, NAME!"</li>
 * </ul>
 * 
 * @param id unique, monotonically increasing greeting identifier
 * @param content personalized greeting message
 * 
 * @author ZAKARIA
 * @version 1.0
 */
//@ public class Greeting {
//@   invariant id > 0;
//@   invariant content != null;
//@   invariant content.startsWith("Hello, ");
//@   invariant content.endsWith("!");
//@ }
public record Greeting(
  //@ spec_public
  long id, 
  
  //@ spec_public
  String content
) {
  
  /**
   * Compact constructor for Greeting record with validation.
   * 
   * <h3>JML Contract:</h3>
   * <pre>
   * &#64;requires id > 0;
   * &#64;requires content != null;
   * &#64;requires content.startsWith("Hello, ");
   * &#64;requires content.endsWith("!");
   * &#64;ensures this.id == id;
   * &#64;ensures this.content.equals(content);
   * </pre>
   * 
   * @param id the greeting identifier (must be positive)
   * @param content the greeting message (must be non-null and properly formatted)
   * @throws IllegalArgumentException if id <= 0
   * @throws NullPointerException if content is null
   * @throws IllegalArgumentException if content doesn't follow expected format
   */
  //@ public normal_behavior
  //@ requires id > 0;
  //@ requires content != null;
  //@ ensures this.id == id;
  //@ ensures this.content == content;
  public Greeting {
    if (id <= 0) {
      throw new IllegalArgumentException("Greeting ID must be positive, got: " + id);
    }
    if (content == null) {
      throw new NullPointerException("Greeting content cannot be null");
    }
    if (!content.startsWith("Hello, ")) {
      throw new IllegalArgumentException("Greeting content must start with 'Hello, ', got: " + content);
    }
    if (!content.endsWith("!")) {
      throw new IllegalArgumentException("Greeting content must end with '!', got: " + content);
    }
  }
}
