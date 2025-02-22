package zone.vao.nexoAddon.events.nexo.furnitures;

import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.furnitures.places.ShiftBlockListener;

public class NexoFurniturePlaceListener implements Listener {

  @EventHandler
  public void onFurniturePlace(final NexoFurniturePlaceEvent event) {

    ShiftBlockListener.onShiftBlockPlace(event);
  }
}
