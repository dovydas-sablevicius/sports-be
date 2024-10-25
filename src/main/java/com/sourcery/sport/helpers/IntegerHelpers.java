package com.sourcery.sport.helpers;

public class IntegerHelpers {
    public static int returnNumberOrZero(Integer number)
    {
      return number == null ? 0 : number;
    }

  private IntegerHelpers() {}
}
