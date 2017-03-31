/**
 * Java interface for scenario authors - control idioms
 */

package org.boteval.api;

import java.util.*;

public interface ControlApiInterface {

  public Boolean asUser(String userId);
  public Boolean listUsers();

}
