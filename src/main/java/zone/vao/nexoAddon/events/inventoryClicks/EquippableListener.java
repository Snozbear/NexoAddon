package zone.vao.nexoAddon.events.inventoryClicks;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.components.Components;
import zone.vao.nexoAddon.utils.VersionUtil;

public class EquippableListener {

  public static void onInventoryClick(InventoryClickEvent event) {
    if (!VersionUtil.isVersionLessThan("1.21.3")) return;

    if(!(event.getWhoClicked() instanceof Player player) || event.getClickedInventory() == null) return;

    if(event.getCursor().isEmpty() || NexoItems.idFromItem(event.getCursor()) == null) return;

    Components components = NexoAddon.getInstance().getComponents().get(NexoItems.idFromItem(event.getCursor()));
    if (components == null || components.getEquippable() == null) return;

    if(!event.getSlotType().equals(InventoryType.SlotType.ARMOR)) return;

    int slot = event.getSlot();
    int equippedSlot = 0;
    switch (components.getEquippable().slot()) {
      case HEAD -> equippedSlot = 39;
      case CHEST -> equippedSlot = 38;
      case LEGS -> equippedSlot = 37;
      case FEET -> equippedSlot = 36;
    }

    if(slot != equippedSlot) return;

    ItemStack currItem = event.getCurrentItem();
    ItemStack cursorItem = event.getCursor();
    if(currItem != null)
      currItem = currItem.clone();

    ItemStack finalCurrItem = currItem;
    new BukkitRunnable() {
      @Override
      public void run() {
        player.getInventory().setItem(components.getEquippable().slot(), cursorItem);
        player.setItemOnCursor(finalCurrItem);
      }
    }.runTaskLater(NexoAddon.getInstance(), 1L);
  }
}