package xyz.aeolia.ashutils.instance

object Message {
  object Econ {
    const val TOO_MANY = "You don't have that many to sell!"
  }
  object Error {
    const val CONFIG = "The config is not correctly configured - it is missing field %s."
    const val GENERIC = "An internal error occurred. Please contact an administrator."
  }
  object Generic {
    const val COMMAND_USAGE = "Unrecognised usage. Usage:"
    const val NOT_PLAYER = "You must be a player to execute this command."
    const val NOT_PLAYER_ARGS = "You must be a player to execute this command without arguments."
    const val TOO_MANY_ARGS = "Too many arguments! Usage:"
  }
  object Player {
    const val NOT_FOUND = "Player not found. Please check your spelling and try again."
    const val OFFLINE = "This command cannot accept an offline player."
  }
}