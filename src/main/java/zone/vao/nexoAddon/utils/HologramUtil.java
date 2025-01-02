package zone.vao.nexoAddon.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramUtil {

  private static final Map<UUID, ArmorStand> holograms = new HashMap<>();

  public static void displayProgressBar(Entity entity, double progress, Player player) {
    if (entity == null || progress < 0.0 || progress > 1.0) return;

    World world = entity.getWorld();
    Location entityLocation = entity.getLocation().clone();
    Location hologramLocation = entityLocation.add(0, 0.5, 0);

    String progressBar = getProgressBar(progress, 10);
    String hologramText = ChatColor.GREEN + progressBar;

    if (player != null && holograms.containsKey(player.getUniqueId())) {
      ArmorStand existingHologram = holograms.get(player.getUniqueId());
      existingHologram.remove();
      holograms.remove(player.getUniqueId());
    }

    ArmorStand hologram = world.spawn(hologramLocation, ArmorStand.class, stand -> {
      stand.setCustomName(hologramText);
      stand.setCustomNameVisible(true);
      stand.setGravity(false);
      stand.setInvisible(true);
      stand.setMarker(true);
      stand.setSmall(true);
      if (player != null) {
        stand.setVisibleByDefault(false);
        player.showEntity(NexoAddon.getInstance(), stand);
      }
    });

    holograms.put(player.getUniqueId(), hologram);

    new BukkitRunnable() {
      @Override
      public void run() {
        hologram.remove();
        holograms.remove(player.getUniqueId());
      }
    }.runTaskLater(NexoAddon.getInstance(), 60);
  }

  private static String getProgressBar(double progress, int length) {
    int filledLength = (int) (progress * length);
    int emptyLength = length - filledLength;

    StringBuilder bar = new StringBuilder();
    bar.append(ChatColor.GREEN);
    bar.append("█".repeat(filledLength));
    bar.append(ChatColor.RED);
    bar.append("█".repeat(emptyLength));

    return bar.toString();
  }
}
