package zone.vao.nexoAddon.events;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.populators.orePopulator.Ore;

import java.util.List;
import java.util.Random;

public class ChunkLoadListener implements Listener {

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    World world = event.getWorld();
    Chunk chunk = event.getChunk();

    List<Ore> furniturePopulators =  NexoAddon.getInstance().getOrePopulator().getOres().stream().filter(ore -> ore.getNexoFurniture()!=null).toList();

    if(furniturePopulators.isEmpty()) return;

    furniturePopulators.forEach(ore -> {
      if (ore.getNexoFurniture() == null || !ore.getWorlds().contains(world) || !event.isNewChunk()) return;

      Bukkit.getScheduler().runTaskAsynchronously(NexoAddon.getInstance(), () -> {
        Random random = new Random();
        if (random.nextDouble() > ore.getChance()) return;
        final int attempts = ore.getIterations();
        final int maxRetries = attempts * 20;
        int successfulPlacements = 0;

        for (int attempt = 0; attempt < maxRetries && successfulPlacements < attempts; attempt++) {
          int x = (chunk.getX() << 4) + random.nextInt(16);
          int z = (chunk.getZ() << 4) + random.nextInt(16);
          int y = ore.getMinLevel() + random.nextInt(ore.getMaxLevel() - ore.getMinLevel() + 1);

          Location loc = new Location(world, x, y, z);
          Material targetBlock = loc.getBlock().getType();
          Material belowBlock = loc.clone().add(0, -1, 0).getBlock().getType();
          Material abovelock = loc.clone().add(0, 1, 0).getBlock().getType();

          if (!ore.getBiomes().contains(loc.getBlock().getBiome())) continue;

          boolean canReplace = ore.getReplace() != null
              && ore.getReplace().contains(targetBlock);
          boolean canPlaceOn = ore.getPlaceOn() != null
              && ore.getPlaceOn().contains(belowBlock)
              && targetBlock.isAir();
          boolean canPlaceBelow = ore.getPlaceBelow() != null
              && ore.getPlaceBelow().contains(abovelock)
              && (!ore.isOnlyAir() || targetBlock.isAir());

          if (canReplace || canPlaceOn || canPlaceBelow) {
            successfulPlacements++;

            Location finalLoc = loc.clone();
            Bukkit.getScheduler().runTaskLater(NexoAddon.getInstance(), () -> {
              if(canReplace)
                finalLoc.getBlock().setType(Material.AIR);
              ore.getNexoFurniture().place(finalLoc);
            }, successfulPlacements* 5L);
          }
        }
      });
    });
  }
}
