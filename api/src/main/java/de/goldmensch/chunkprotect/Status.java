package de.goldmensch.chunkprotect;

public enum Status {
  POSITIVE,
  NEGATIVE,
  CANCELLED;

  public boolean toBoolean() {
    return switch (this) {
      case NEGATIVE, CANCELLED -> false;
      case POSITIVE -> true;
    };
  }
}
