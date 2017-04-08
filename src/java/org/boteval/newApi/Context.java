package org.boteval.java.api;

import java.util.ArrayList;
import java.util.List;

public class Context {

  private List<String> lineage = new ArrayList<String>();

  private String asString() {
    return String.join(",", lineage);
  }

  public String add(String name) {
    System.out.println(name);
    lineage.add(name);
    System.out.println("after add");
    return this.asString();
  }

}
