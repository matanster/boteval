/**
 * Java interface for scenario authors - user interaction idioms
 */

package org.boteval.api;

import java.util.*;

public interface ApiInterface {

  public void sendToBot(String msg);
  public List<Response> getResponses();

}

