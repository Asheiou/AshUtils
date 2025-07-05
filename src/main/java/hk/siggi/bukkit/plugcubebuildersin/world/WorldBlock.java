package hk.siggi.bukkit.plugcubebuildersin.world;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class WorldBlock {

  public final String world;
  public final int x, y, z;

  public WorldBlock(Block block) {
    this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
  }

  public WorldBlock(String world, int x, int y, int z) {
    if (world == null) {
      throw new NullPointerException();
    }
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public WorldChunk getChunk() {
    return new WorldChunk(this);
  }

  public Chunk getBukkitChunk() {
    return Bukkit.getWorld(world).getChunkAt(x >> 4, z >> 4);
  }

  public Block getBukkitBlock() {
    return Bukkit.getWorld(world).getBlockAt(x, y, z);
  }

  public Location getBukkitLocation(boolean middle) {
    double w = middle ? 0.5 : 0.0;
    return new Location(Bukkit.getWorld(world), ((double) x) + w, ((double) y) + w, ((double) z) + w);
  }

  public boolean isChunkLoaded() {
    try {
      World w = Bukkit.getWorld(world);
      if (w == null) {
        return false;
      }
      return w.isChunkLoaded(x >> 4, z >> 4);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof WorldBlock) {
      return equals((WorldBlock) other);
    }
    return false;
  }

  public boolean equals(WorldBlock other) {
    if (other == null) {
      return false;
    }
    return world.equals(other.world) && x == other.x && y == other.y && z == other.z;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + this.world.hashCode();
    hash = 89 * hash + this.x;
    hash = 89 * hash + this.y;
    hash = 89 * hash + this.z;
    return hash;
  }
}