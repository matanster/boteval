package org.boteval.java.api;

public abstract class Scenario {

  private Context context = new Context();
  private String name;

  public Scenario(String name) {

    this.name = name;

    String contextAsString = context.add(name);
    System.out.println(contextAsString);

  }

}
