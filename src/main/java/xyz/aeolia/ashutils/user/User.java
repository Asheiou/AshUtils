package xyz.aeolia.ashutils.user;

import java.util.UUID;

public class User {
  private UUID uuid;
  private Boolean modmode;
  private Boolean vanish;
  private Long lastpvppayout;

  public UUID getUuid() {
    if (uuid == null) {
      throw new RuntimeException();
    }
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
    UserHelper.putUser(this);
  }

  public boolean getModMode() {
    if (modmode == null) {
      this.modmode = UserHelper.getDefaultUser().getModMode();
      UserHelper.putUser(this);
    }
    return modmode;
  }

  public void setModMode(Boolean modmode) {
    this.modmode = modmode;
    UserHelper.putUser(this);
  }

  public boolean getVanish() {
    if (vanish == null) {
      this.vanish = UserHelper.getDefaultUser().getVanish();
      UserHelper.putUser(this);
    }
    return vanish;
  }

  public boolean setVanish(Boolean vanish) {
    /*
    This function only sets the status of vanish in the user object.
     */
    this.vanish = vanish;
    UserHelper.putUser(this);
    return vanish;
  }

  public Long getLastPvpPayout() {
    if (lastpvppayout == null) {
      this.lastpvppayout = UserHelper.getDefaultUser().getLastPvpPayout();
    }
    return lastpvppayout;
  }

  public void setLastPvpPayout(long lastpvppayout) {
    this.lastpvppayout = lastpvppayout;
    UserHelper.putUser(this);
  }
}
