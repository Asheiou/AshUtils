package cymru.asheiou.ashutils.manager;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
  static Map<String, Boolean> statusMap = new HashMap<>();

  public static void setStatus(String instance, boolean status) {
    StatusManager.statusMap.put(instance, status);
  }

  public static boolean getStatus(String instance) {
    return statusMap.get(instance);
  }
}
