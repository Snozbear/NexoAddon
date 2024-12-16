package zone.vao.nexoAddon.classes.populators.orePopulator;

import org.bukkit.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import zone.vao.nexoAddon.NexoAddon;

import java.util.Random;

public class CustomOrePopulator extends BlockPopulator {

  private final OrePopulator orePopulator;

  public CustomOrePopulator(OrePopulator orePopulator) {
    this.orePopulator = orePopulator;
  }

  @Override
  public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
    for (Ore ore : orePopulator.getOres()) {
      generateOre(worldInfo, random, chunkX, chunkZ, limitedRegion, ore);
    }
  }

  private void generateOre(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion, Ore ore) {
    if (random.nextDouble() > ore.getChance()) return;
    int attempts = ore.getIterations();
    int maxRetriesPerIteration = 80;

    int successfulPlacements = 0;
    int totalAttempts = 0;

    while (successfulPlacements < attempts && totalAttempts < attempts * maxRetriesPerIteration) {
      int x = (chunkX << 4) + random.nextInt(16);
      int z = (chunkZ << 4) + random.nextInt(16);
      int y = ore.getMinLevel() + random.nextInt(ore.getMaxLevel() - ore.getMinLevel() + 1);

      totalAttempts++;

      if (!limitedRegion.isInRegion(x, y, z)) {
        continue;
      }

      Material blockType = limitedRegion.getType(x, y, z);

      if (ore.getReplace() != null) {
        if (ore.getReplace().contains(blockType) && ore.getBiomes().contains(limitedRegion.getBiome(x, y, z))) {
          if (ore.getNexoBlocks() != null && ore.getNexoBlocks().getBlockData() != null)
            limitedRegion.setBlockData(x, y, z, ore.getNexoBlocks().getBlockData());
          else {
            Bukkit.getScheduler().runTaskLater(NexoAddon.getInstance(), () -> {
              World world = Bukkit.getWorld(worldInfo.getUID());
              if (world != null) {
                Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
                if (!chunk.isLoaded()) {
                  chunk.load();
                }
                Location loc = new Location(world, x, y, z);
                ore.getNexoFurnitures().place(loc);
              }
            }, 1L);
          }
          successfulPlacements++;
        }
      }
      else if (ore.getPlaceOn() != null) {
        if (ore.getPlaceOn().contains(blockType) && ore.getBiomes().contains(limitedRegion.getBiome(x, y, z))) {

          if (!limitedRegion.getType(x, y + 1, z).isAir()) {
            continue;
          }

          if (ore.getNexoBlocks() != null && ore.getNexoBlocks().getBlockData() != null)
            limitedRegion.setBlockData(x, y + 1, z, ore.getNexoBlocks().getBlockData());
          else {
            Bukkit.getScheduler().runTaskLater(NexoAddon.getInstance(), () -> {
              World world = Bukkit.getWorld(worldInfo.getUID());
              if (world != null) {
                Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
                if (!chunk.isLoaded()) {
                  chunk.load();
                }
                Location loc = new Location(world, x, y + 1, z);
                ore.getNexoFurnitures().place(loc);
              }
            }, 1L);
          }

          successfulPlacements++;
        }
      }
    }

//    if (successfulPlacements < attempts) {
//      Bukkit.broadcastMessage("Only placed " + successfulPlacements + " out of " + attempts + " ores in chunk " + chunkX + ", " + chunkZ);
//    }
  }
}
