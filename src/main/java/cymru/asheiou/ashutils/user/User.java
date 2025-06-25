package cymru.asheiou.ashutils.user;

import cymru.asheiou.ashutils.manager.UserManager;

import java.util.UUID;

public class User {
  private UUID uuid;
  private Boolean alertstatus;

  public UUID getUuid() {
    if (uuid == null) { throw new RuntimeException(); }
      return uuid;
  }
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
    UserManager.saveUser(this);
  }

  public boolean getAlertStatus() {
    if (alertstatus == null) {
      this.alertstatus = UserManager.getDefaultUser().getAlertStatus();
      UserManager.saveUser(this);
    }
    return alertstatus;
  }

  public void setAlertStatus(Boolean alertstatus) {
    this.alertstatus = alertstatus;
    UserManager.saveUser(this);
  }
}
