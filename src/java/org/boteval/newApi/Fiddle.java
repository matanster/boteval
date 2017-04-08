/*

  we can probably/hopefully pass an type-free arguments list to a clojure function as an array of objects
  need to check that out. To pass a function by name (a.k.a by reference) we probably need to wrap it in a class and pass
  an instance of the class, or a lambda or something.

*/

package org.boteval.java.api;

public class Fiddle {
  Boolean arg0 = true;
  String arg1 = "aaa";
  int arg2 = 3;
  public Object[] arglist = new Object[]{arg0, arg1, arg2};
}
