package org.boteval.java.api.test;

import org.boteval.java.api.*;
import org.boteval.java.api.samples.*;

public class JavaUsageTest {
    public static void main(String[] args) {
      System.out.println("java sample scenario started");
      SampleScenario s = new SampleScenario("sample");
      Fiddle fiddle = new Fiddle();
      System.out.println(fiddle.arglist[0]);
      System.out.println(fiddle.arglist[1]);
    }
}


