package xyz.aeolia.ashutils.instance

data class Kit(
  var id: String? = null,
  val displayName: String,
  val permission: String = "ashutils.kit",
  val items: List<Item>
)