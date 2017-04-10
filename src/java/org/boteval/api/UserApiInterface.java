/**
 * Java interface for scenario authors to use, abstracting and thusly decoupling from a specific bot implementation.
 * This interface should be implemented by a driver per particular bot.
 */

package org.boteval.api;

import java.util.*;

public interface UserApiInterface {

  /* connect to the bot */
  public void connectToBot();

  /* open a session on a bot connection */
  public int openBotSession();

  public void sendToBot(int sessionId, String msg);

  public List<String> getReceived(int sessionId);

}

