package com.camunda.consulting.simplerestclient.request;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a URI ath.
 * 
 * @author Sebastian Warnke (sebastian.warnke@camunda.com)
 *
 */
public class Path {

  private final List<String> path = new ArrayList<String>();

  /**
   * Constructor
   * 
   * @param elements
   *          set of strings in correct order according to the path being
   *          represented
   */
  public Path(String... elements) {
    for (String element : elements) {
      path.add(element);
    }
  }

  @Override
  public String toString() {
    String result = "";
    for (String element : path) {
      result += "/" + element;
    }

    return result;
  }
}
