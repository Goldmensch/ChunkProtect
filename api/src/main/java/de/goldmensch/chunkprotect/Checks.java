package de.goldmensch.chunkprotect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Checks {

  private Checks() {
  }

  public static <T> T notNull(@Nullable T object, @NotNull String name) {
    if (object == null) {
      throw new NullPointerException(name + " may not be null!");
    }
    return object;
  }

}
