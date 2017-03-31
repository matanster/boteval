package org.boteval.java.api;

import java.util.ArrayList;

public class Context {

  private ArrayList<String> lineage = new ArrayList<String>();

  private String asString() {
    return String.join(",", lineage);
  }

  public String add(String name) {
    lineage.add(name);
    return this.asString();
  }

}
