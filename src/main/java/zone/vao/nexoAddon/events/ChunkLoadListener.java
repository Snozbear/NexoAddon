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

    List<Ore> furniturePopulators = NexoAddon.getInstance()
        .getOrePopulator()
        .getOres()
        .stream()
        .filter(ore -> ore.getNexoFurniture() != null)
        .toList();

    if (furniturePopulators.isEmpty() || !event.isNewChunk()) return;

    furniturePopulators.forEach(ore -> processOre(world, chunk, ore));
  }

  private void processOre(World world, Chunk chunk, Ore ore) {
    if (!ore.getWorlds().contains(world)) return;

    Bukkit.getScheduler().runTaskAsynchronously(NexoAddon.getInstance(), () -> {
      Random random = new Random();
      if (random.nextDouble() > ore.getChance()) return;

      int successfulPlacements = 0;
      int maxRetries = ore.getIterations() * 20;

      for (int attempt = 0; attempt < maxRetries && successfulPlacements < ore.getIterations(); attempt++) {
        Location loc = getRandomLocation(chunk, random, ore);

        if (!isValidBiome(loc, ore)) continue;

        if (canPlaceOre(loc, ore)) {
          successfulPlacements++;
          scheduleOrePlacement(loc, ore, successfulPlacements);
        }
      }
    });
  }

  private Location getRandomLocation(Chunk chunk, Random random, Ore ore) {
    int x = (chunk.getX() << 4) + random.nextInt(16);
    int z = (chunk.getZ() << 4) + random.nextInt(16);
    int y = ore.getMinLevel() + random.nextInt(ore.getMaxLevel() - ore.getMinLevel() + 1);
    return new Location(chunk.getWorld(), x, y, z);
  }

  private boolean isValidBiome(Location loc, Ore ore) {
    return ore.getBiomes().contains(loc.getBlock().getBiome());
  }

  private boolean canPlaceOre(Location loc, Ore ore) {
    Material targetBlock = loc.getBlock().getType();
    Material belowBlock = loc.clone().add(0, -1, 0).getBlock().getType();
    Material aboveBlock = loc.clone().add(0, 1, 0).getBlock().getType();

    boolean canReplace = ore.getReplace() != null && ore.getReplace().contains(targetBlock);
    boolean canPlaceOn = ore.getPlaceOn() != null && ore.getPlaceOn().contains(belowBlock) && targetBlock.isAir();
    boolean canPlaceBelow = ore.getPlaceBelow() != null && ore.getPlaceBelow().contains(aboveBlock)
        && (!ore.isOnlyAir() || targetBlock.isAir());

    return canReplace || canPlaceOn || canPlaceBelow;
  }

  private void scheduleOrePlacement(Location loc, Ore ore, int placementIndex) {
    Bukkit.getScheduler().runTaskLater(NexoAddon.getInstance(), () -> {
      if (ore.getReplace() != null && ore.getReplace().contains(loc.getBlock().getType())) {
        loc.getBlock().setType(Material.AIR);
      }
      ore.getNexoFurniture().place(loc);
    }, placementIndex * 5L);
  }
}
