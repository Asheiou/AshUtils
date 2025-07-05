package xyz.aeolia.ashutils.serializable

import kotlinx.serialization.Serializable

@Serializable
data class Kit(
  var id: String? = null,
  val displayName: String,
  val permission: String = "ashutils.kit",
  val items: List<Item>,
  val displayItem: Item,
)