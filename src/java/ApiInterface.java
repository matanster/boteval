/**
 * Interface for Java scenario authors
 */

package org.boteval.api;

import java.util.*;

public interface ApiInterface {

  public void initScenario(String name);
  public void send(String msg);
  public List<Response> getResponses();

}
