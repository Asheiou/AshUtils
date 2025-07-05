package xyz.aeolia.ashutils.serializable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import xyz.aeolia.ashutils.utils.UUIDSerializer
import java.util.*

@Serializable
data class User(
  @Transient var online: Boolean = false,
  @SerialName("lastpvppayout") var lastPvpPayout: Long = 0,
  @SerialName("modmode") var modMode: Boolean = false,
  @Serializable(with = UUIDSerializer::class)
  var uuid: UUID? = null,
  var vanish: Boolean = false,
 )