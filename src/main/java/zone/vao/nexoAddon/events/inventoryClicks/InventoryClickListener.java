package zone.vao.nexoAddon.events.inventoryClicks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    RepairListener.onInventoryClick(event);
    EquippableListener.onInventoryClick(event);
  }
}
