package zone.vao.nexoAddon.events.playerFurnitureInteracts;

import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.utils.HologramUtil;

import static zone.vao.nexoAddon.events.playerFurnitureInteracts.Fertilize.EVOLUTION_KEY;

public class DisplayCropsHologram {

  public static void onInteract(NexoFurnitureInteractEvent event){

    double progress = (double) event.getBaseEntity().getPersistentDataContainer().get(EVOLUTION_KEY, PersistentDataType.INTEGER)
        / NexoFurniture.furnitureMechanic(event.getBaseEntity()).getEvolution().getDelay();;
    HologramUtil.displayProgressBar(event.getBaseEntity(), Math.max(0.0, Math.min(1.0, progress)), event.getPlayer());
  }
}
