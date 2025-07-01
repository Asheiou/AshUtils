package xyz.aeolia.ashutils.`object`

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class User(
  @SerializedName("lastpvppayout") var lastPvpPayout: Long? = null,
  @SerializedName("modmode") var modMode: Boolean? = null,
  @SerializedName("uuid") var uuid: UUID? = null,
  @SerializedName("vanish") var vanish: Boolean? = null,
 )