package zone.vao.nexoAddon.utils;

import com.google.common.collect.Sets;
import com.jeff_media.customblockdata.CustomBlockData;
import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;
import zone.vao.nexoAddon.classes.mechanic.Decay;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BlockUtil {

  public static final Set<Material> UNBREAKABLE_BLOCKS = Sets.newHashSet(Material.BEDROCK, Material.BARRIER, Material.NETHER_PORTAL, Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.END_GATEWAY);
  private static final Set<Location> processedCustomBlocks = ConcurrentHashMap.newKeySet();
  public static final Set<Location> processedShiftblocks = ConcurrentHashMap.newKeySet();

  public static void startShiftBlock(Location location, CustomBlockMechanic to, CustomBlockMechanic target, int time) {
    World world = location.getWorld();
    if(world == null || processedShiftblocks.contains(location)) return;

    Location finalLocation = location.clone();

    processedShiftblocks.add(location);
    PersistentDataContainer pdc = new CustomBlockData(location.getBlock(), NexoAddon.getInstance());
    pdc.set(new NamespacedKey(NexoAddon.getInstance(), "shiftblock_target"), PersistentDataType.STRING, target.getItemID());
    finalLocation.getBlock().setType(Material.AIR);
    new BukkitRunnable() {
      @Override
      public void run() {
        NexoBlocks.place(to.getItemID(), finalLocation);
      }
    }.runTaskLater(NexoAddon.getInstance(), 1);

    new BukkitRunnable() {
      @Override
      public void run() {
        if(!NexoBlocks.isCustomBlock(finalLocation.getBlock()) ||
            !NexoBlocks.customBlockMechanic(finalLocation).getItemID().equalsIgnoreCase(to.getItemID())
        ) {
          cancel();
          processedShiftblocks.remove(finalLocation);
          pdc.remove(new NamespacedKey(NexoAddon.getInstance(), "shiftblock_target"));
          return;
        }

        new BukkitRunnable() {
          @Override
          public void run() {
            finalLocation.getBlock().setType(Material.AIR);
          }
        }.runTask(NexoAddon.getInstance());
        new BukkitRunnable() {
          @Override
          public void run() {
            NexoBlocks.place(target.getItemID(), finalLocation);
          }
        }.runTaskLater(NexoAddon.getInstance(), 1);
        processedShiftblocks.remove(finalLocation);
        pdc.remove(new NamespacedKey(NexoAddon.getInstance(), "shiftblock_target"));
      }
    }.runTaskLaterAsynchronously(NexoAddon.getInstance(), time*20L);
  }

  public static void startDecay(Location location) {
    int radius = 10;
    World world = location.getWorld();

    if (world == null || VersionUtil.nexoVersionLessThan("0.10.0")) {
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

              if (processedCustomBlocks.contains(currentLocation)) {
                continue;
              }

              if (NexoBlocks.isCustomBlock(block)) {
                String itemId = NexoBlocks.customBlockMechanic(block.getLocation()).getItemID();
                Mechanics mechanic = NexoAddon.getInstance().getMechanics().get(itemId);

                if (mechanic != null && mechanic.getDecay() != null) {
                  Decay decay = mechanic.getDecay();

                  processedCustomBlocks.add(currentLocation);
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
          processedCustomBlocks.remove(block.getLocation());
          cancel();
          return;
        }

        boolean hasBaseNearby = false;
        for (int x = -decay.radius(); x <= decay.radius(); x++) {
          for (int y = -decay.radius(); y <= decay.radius(); y++) {
            for (int z = -decay.radius(); z <= decay.radius(); z++) {
              Block nearbyBlock = block.getLocation().add(x, y, z).getBlock();
              if (decay.base().contains(nearbyBlock.getType())
                  || NexoBlocks.customBlockMechanic(nearbyBlock.getLocation()) != null
                  && decay.nexoBase().contains(NexoBlocks.customBlockMechanic(nearbyBlock.getLocation()).getItemID())
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
          processedCustomBlocks.remove(block.getLocation());
          cancel();
        }
      }
    }.runTaskTimerAsynchronously(NexoAddon.getInstance(), 0, decay.time() * 20L);
  }

  public static void startBlockAura(Particle particle, Location location, double xOffset, double yOffset, double zOffset, int amount, double deltaX, double deltaY, double deltaZ, double speed) {
    int taskId = new BukkitRunnable() {
      @Override
      public void run() {
        World world = location.getWorld();
        if (world != null) {
          world.spawnParticle(
                  particle,
                  location.clone().add(xOffset, yOffset, zOffset),
                  amount,
                  deltaX, deltaY, deltaZ,
                  speed
          );
        }
      }
    }.runTaskTimer(NexoAddon.getInstance(), 0L, 10L).getTaskId();

    NexoAddon.getInstance().getParticleTasks().put(location, taskId);
  }

  public static void stopBlockAura(Location location) {
    Integer taskId = NexoAddon.getInstance().getParticleTasks().remove(location);
    if (taskId != null) {
      Bukkit.getScheduler().cancelTask(taskId);
    }
  }
}
