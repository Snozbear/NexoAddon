package zone.vao.nexoAddon.events.nexo.furnitures;

import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.furnitures.places.ShiftBlockListener;

import static zone.vao.nexoAddon.events.nexo.furnitures.interacts.ShiftBlockListener.toCancelation;

public class NexoFurniturePlaceListener implements Listener {

  @EventHandler
  public void onFurniturePlace(final NexoFurniturePlaceEvent event) {

    if(toCancelation.contains(event.getPlayer().getUniqueId())){
      event.setCancelled(true);
      toCancelation.remove(event.getPlayer().getUniqueId());
      return;
    }
    ShiftBlockListener.onShiftBlockPlace(event);
  }
}
