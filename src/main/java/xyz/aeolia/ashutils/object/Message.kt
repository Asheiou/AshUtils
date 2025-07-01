package xyz.aeolia.ashutils.`object`

object Message {
  object econ {
    const val tooMany = "You don't have that many to sell!"
  }
  object error {
    const val generic = "An internal error occurred. Please contact an administrator."
  }
  object generic {
    const val commandUsage = "Unrecognised usage. Usage:"
    const val notPlayer = "You must be a player to execute this command."
    const val notPlayerNoArgs = "You must be a player to execute this command without arguments."
    const val tooManyArgs = "Too many arguments! Usage:"
  }
  object player {
    const val notFound = "Player not found. Please check your spelling and try again."
  }
}