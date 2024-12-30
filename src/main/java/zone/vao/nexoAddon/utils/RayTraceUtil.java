package zone.vao.nexoAddon.utils;

import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import zone.vao.nexoAddon.NexoAddon;


public class RayTraceUtil {

  public static FurnitureMechanic ray(Player player){

    final RayTraceResult[] result = {null};
    new BukkitRunnable() {

      @Override
      public void run() {
        result[0] = player.getWorld().rayTrace(
            player.getEyeLocation(),
            player.getEyeLocation().getDirection(),
            5,
            FluidCollisionMode.NEVER,
            true,
            0.5,
            NexoFurniture::isFurniture
        );
      }
    }.runTask(NexoAddon.getInstance());
    if (result[0] == null || result[0].getHitEntity() == null) return null;

    return NexoFurniture.furnitureMechanic(result[0].getHitEntity());
  }
}
