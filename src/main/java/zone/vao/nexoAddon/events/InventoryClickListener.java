package zone.vao.nexoAddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import zone.vao.nexoAddon.events.inventoryClicks.EquippableListener;
import zone.vao.nexoAddon.events.inventoryClicks.RepairListener;

public class InventoryClickListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    RepairListener.onInventoryClick(event);
    EquippableListener.onInventoryClick(event);
  }
}
