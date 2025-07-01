package xyz.aeolia.ashutils.`object`

import com.google.gson.annotations.SerializedName
import xyz.aeolia.ashutils.manager.UserManager
import java.util.UUID

data class User(
  @SerializedName("lastpvppayout") var lastPvpPayout: Long? = 0,
  @SerializedName("modmode") var modMode: Boolean? = false,
  @SerializedName("uuid") var uuid: UUID? = null,
  @SerializedName("vanish") var vanish: Boolean? = false,
 )