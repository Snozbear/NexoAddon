package zone.vao.nexoAddon.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.populators.orePopulator.ChunkPosition;
import zone.vao.nexoAddon.classes.populators.orePopulator.CustomOrePopulator;
import zone.vao.nexoAddon.classes.populators.orePopulator.Ore;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChunkLoadListener implements Listener {

  private static final Map<ChunkPosition, Map<Ore, List<CustomOrePopulator.PlacementPosition>>> queuedPlacements = new HashMap<>();

  public static void queuePlacementsForPos(ChunkPosition chunkPosition, CustomOrePopulator.PlacementPosition positions, Ore ore) {
    queuedPlacements.computeIfAbsent(chunkPosition, k -> new HashMap<>())
        .computeIfAbsent(ore, k -> new ArrayList<>())
        .add(positions);
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    Chunk chunk = event.getChunk();
    World world = chunk.getWorld();
    ChunkPosition chunkPosition = new ChunkPosition(chunk.getX(), chunk.getZ(), world.getUID());

    Map<Ore, List<CustomOrePopulator.PlacementPosition>> placements = queuedPlacements.remove(chunkPosition);
    if (placements != null) {
      placements.forEach((ore, positions) -> {
        schedulePlacement(world, positions, ore);
      });
    }
  }

  private void schedulePlacement(World world, List<CustomOrePopulator.PlacementPosition> positions, Ore ore) {
    AtomicInteger oreIndex = new AtomicInteger(1);
    positions.forEach(position -> {
      int index = oreIndex.incrementAndGet();
      Bukkit.getScheduler().runTask(NexoAddon.getInstance(), () -> {

        Location loc = new Location(world, position.x(), position.y(), position.z());
        ore.getNexoFurnitures().place(loc);
      });
    });
  }
}
