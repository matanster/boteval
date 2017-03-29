/**
 * Interface for Java scenario authors - chat idioms
 */

package org.boteval.api;

import java.util.*;

public interface ApiInterface {

  public void initScenario(String name);
  public void sendToBot(String msg);
  public List<Response> getResponses();

}
