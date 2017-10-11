package com.camunda.consulting.simplerestclient.request;

import java.util.ArrayList;
import java.util.List;

public class Path {

  private final List<String> path = new ArrayList<String>();

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
