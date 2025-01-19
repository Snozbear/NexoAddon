package zone.vao.nexoAddon.utils;

import com.google.common.collect.Sets;
import com.nexomc.nexo.api.NexoBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;
import zone.vao.nexoAddon.classes.mechanic.Decay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockUtil {

  public static final Set<Material> UNBREAKABLE_BLOCKS = Sets.newHashSet(Material.BEDROCK, Material.BARRIER, Material.NETHER_PORTAL, Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.END_GATEWAY);
  private static final List<Location> processedChoruses = Collections.synchronizedList(new ArrayList<>());


  public static void startDecay(Location location) {
    int radius = 10;
    World world = location.getWorld();

    if (world == null) {
      return;
    }

    new BukkitRunnable() {
      @Override
      public void run() {
        for (int x = -radius; x <= radius; x++) {
          for (int y = -radius; y <= radius; y++) {
            for (int z = -radius; z <= radius; z++) {
              Location currentLocation = location.clone().add(x, y, z);
              Block block = currentLocation.getBlock();

              if (processedChoruses.contains(currentLocation)) {
                continue;
              }

              if (NexoBlocks.isNexoChorusBlock(block)) {
                String itemId = NexoBlocks.chorusBlockMechanic(block).getItemID();
                Mechanics mechanic = NexoAddon.getInstance().getMechanics().get(itemId);

                if (mechanic != null && mechanic.getDecay() != null) {
                  Decay decay = mechanic.getDecay();

                  processedChoruses.add(currentLocation);
                  startDecayTimer(block, decay);
                }
              }
            }
          }
        }
      }
    }.runTaskAsynchronously(NexoAddon.getInstance());
  }

  private static void startDecayTimer(Block block, Decay decay) {

    new BukkitRunnable() {
      @Override
      public void run() {
        if (block.getType() == Material.AIR && !NexoBlocks.isCustomBlock(block)) {
          processedChoruses.remove(block.getLocation());
          cancel();
          return;
        }

        boolean hasBaseNearby = false;
        for (int x = -decay.radius(); x <= decay.radius(); x++) {
          for (int y = -decay.radius(); y <= decay.radius(); y++) {
            for (int z = -decay.radius(); z <= decay.radius(); z++) {
              Block nearbyBlock = block.getLocation().add(x, y, z).getBlock();
              if (decay.base().contains(nearbyBlock.getType())
                  || NexoBlocks.noteBlockMechanic(nearbyBlock) != null
                  && decay.nexoBase().contains(NexoBlocks.noteBlockMechanic(nearbyBlock).getItemID())
              ) {
                hasBaseNearby = true;
                break;
              }
            }
          }
          if (hasBaseNearby) break;
        }

        if (!hasBaseNearby && Math.random() <= decay.chance()) {
          new BukkitRunnable() {
            @Override
            public void run(){
              NexoBlocks.remove(block.getLocation());
            }
          }.runTask(NexoAddon.getInstance());
          processedChoruses.remove(block.getLocation());
          cancel();
        }
      }
    }.runTaskTimerAsynchronously(NexoAddon.getInstance(), 0, decay.time() * 20L);
  }
}
