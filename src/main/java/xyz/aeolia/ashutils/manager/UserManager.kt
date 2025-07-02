package xyz.aeolia.ashutils.manager

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.ashutils.instance.User
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture

class UserManager {

  companion object {
    private val gson = Gson()
    private val users: HashMap<UUID, User> = HashMap()
    private lateinit var plugin: JavaPlugin
    private lateinit var folder: File

    @JvmStatic
    fun init(plugin: JavaPlugin) {
      this.plugin = plugin
      folder = File(plugin.dataFolder, "/users/")
      if (!folder.exists()) {
        if (folder.mkdir()) {
          plugin.logger.severe("Could not create directory ${folder.absolutePath}.")
          Bukkit.getPluginManager().disablePlugin(plugin)
        }
      }
    }

    @JvmStatic
    fun getUser(player: OfflinePlayer): User {
      var user: User?
      val uuid = player.uniqueId
      user = users[uuid]
      if (user != null) return user
      val file = File(folder, "${uuid}.json")
      if (!file.exists()) {
        user = User()
        user.uuid = uuid
        user.online = player.isOnline
        putUser(user)
        return user
      }
      try {
        user = gson.fromJson(FileReader(file), User::class.java)
        putUser(user)
        return user
      } catch (_: FileNotFoundException) {
        plugin.logger.severe("Could not read file ${file.absolutePath}.")
        return User()
      }
    }

    @JvmStatic
    fun putUser(user: User) {
      users.put(user.uuid!!, user)
    }

    @JvmStatic
    fun removeUser(player: OfflinePlayer) {
      val uuid = player.uniqueId
      saveUser(getUser(player)) // save user to prevent data loss from prune
      users.remove(uuid)
    }

    @JvmStatic
    fun saveUser(user: User) {
      val file = File(folder, user.uuid.toString() + ".json")
      try {
        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(user))
        fileWriter.close()
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }

    @JvmStatic
    fun saveUsers() {
      if (users.isEmpty()) {
        return
      }
      CompletableFuture.supplyAsync<Any?> {
        for (user in users.values) {
          saveUser(user)
        }
        plugin.logger.info("Saved " + users.size + " users!")
        null
      }
    }
  }
}