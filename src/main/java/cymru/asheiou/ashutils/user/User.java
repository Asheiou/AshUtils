package cymru.asheiou.ashutils.user;

import java.util.UUID;

public class User {
  private UUID uuid;
  private Boolean modmode;

  public UUID getUuid() {
    if (uuid == null) { throw new RuntimeException(); }
      return uuid;
  }
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
    UserHelper.saveUser(this);
  }

  public boolean getModMode() {
    if (modmode == null) {
      this.modmode = UserHelper.getDefaultUser().getModMode();
      UserHelper.saveUser(this);
    }
    return modmode;
  }

  public void setModMode(Boolean modmode) {
    this.modmode = modmode;
    UserHelper.saveUser(this);
  }
}
