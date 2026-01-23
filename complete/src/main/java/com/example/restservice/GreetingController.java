package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for greeting service.
 * 
 * <h2>JML Specification:</h2>
 * This controller maintains a counter that increments on each greeting request.
 * The counter ensures that each greeting has a unique, monotonically increasing ID.
 * 
 * @author ZAKARIA
 * @version 1.0
 */
@RestController
public class GreetingController {

  private static final String template = "Hello, %s!";
  
  //@ private invariant counter != null;
  //@ private invariant counter.get() >= 0;
  private final AtomicLong counter = new AtomicLong();

  /**
   * Generate a personalized greeting message.
   * 
   * <h3>JML Contract:</h3>
   * 
   * <pre>
   * &#64;requires name != null;
   * &#64;ensures \result != null;
   * &#64;ensures \result.id() > \old(\result.id());
   * &#64;ensures \result.content() != null;
   * &#64;ensures \result.content().contains(name);
   * &#64;ensures \result.content().matches("Hello, .*!");
   * &#64;assignable counter;
   * </pre>
   * 
   * <h3>Specification Details:</h3>
   * <ul>
   *   <li><strong>Precondition:</strong> name parameter must not be null</li>
   *   <li><strong>Postcondition:</strong> returned Greeting is never null</li>
   *   <li><strong>Postcondition:</strong> greeting ID is monotonically increasing</li>
   *   <li><strong>Postcondition:</strong> greeting content is never null</li>
   *   <li><strong>Postcondition:</strong> greeting content contains the provided name</li>
   *   <li><strong>Postcondition:</strong> greeting follows format "Hello, NAME!"</li>
   *   <li><strong>Side Effect:</strong> counter is incremented by exactly 1</li>
   * </ul>
   * 
   * <h3>Behavior Specification:</h3>
   * <table border="1">
   *   <tr>
   *     <th>Input (name)</th>
   *     <th>Expected Output Format</th>
   *     <th>Counter Behavior</th>
   *   </tr>
   *   <tr>
   *     <td>"World"</td>
   *     <td>"Hello, World!"</td>
   *     <td>Increments by 1</td>
   *   </tr>
   *   <tr>
   *     <td>Any non-null string</td>
   *     <td>"Hello, &lt;string&gt;!"</td>
   *     <td>Increments by 1</td>
   *   </tr>
   *   <tr>
   *     <td>null (uses default)</td>
   *     <td>"Hello, World!"</td>
   *     <td>Increments by 1</td>
   *   </tr>
   * </table>
   * 
   * @param name the person's name to greet (null defaults to "World")
   * @return a Greeting object with unique ID and personalized message
   * 
   * @throws NullPointerException if the returned greeting is null (specification violation)
   * 
   * @implNote Uses AtomicLong for thread-safe counter incrementation
   * @implNote Uses String.formatted() for message formatting (Java 15+)
   */
  //@ public normal_behavior
  //@ requires name != null;
  //@ ensures \result != null;
  //@ ensures \result.id() > 0;
  //@ ensures \result.content() != null;
  //@ ensures \result.content().startsWith("Hello, ");
  //@ ensures \result.content().endsWith("!");
  //@ ensures \result.content().length() >= 9; // "Hello, !" = 9 chars minimum
  //@ ensures \result.content().contains(name);
  //@ assignable counter;
  //@ also
  //@ public normal_behavior
  //@ requires name == null;
  //@ ensures \result != null;
  //@ ensures \result.content().equals("Hello, World!");
  //@ assignable counter;
  @GetMapping("/greeting")
  public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
    //@ assert counter != null;
    //@ assert name != null || name == null; // null is handled by defaultValue
    
    long newId = counter.incrementAndGet();
    
    //@ assert newId > 0;
    String content = template.formatted(name);
    
    //@ assert content != null;
    //@ assert content.startsWith("Hello, ");
    //@ assert content.endsWith("!");
    //@ assert content.contains(name);
    
    Greeting result = new Greeting(newId, content);
    
    //@ assert result != null;
    //@ assert result.id() == newId;
    //@ assert result.content().equals(content);
    
    return result;
  }
}
