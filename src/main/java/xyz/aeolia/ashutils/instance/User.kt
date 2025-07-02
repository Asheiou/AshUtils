package xyz.aeolia.ashutils.instance

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class User(
  @Transient var online: Boolean = false,
  @SerializedName("lastpvppayout") var lastPvpPayout: Long? = 0,
  @SerializedName("modmode") var modMode: Boolean? = false,
  @SerializedName("uuid") var uuid: UUID? = null,
  @SerializedName("vanish") var vanish: Boolean? = false,
 )