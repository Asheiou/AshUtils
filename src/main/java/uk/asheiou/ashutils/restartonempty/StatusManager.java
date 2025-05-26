package uk.asheiou.ashutils.restartonempty;

public final class StatusManager {
  private static boolean status;

  public static boolean getStatus() {
    return status;
  }

  public static void setStatus(boolean toSet) {
    status = toSet;
  }
}
