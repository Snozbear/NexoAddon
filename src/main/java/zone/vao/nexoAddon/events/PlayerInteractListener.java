package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.utils.InventoryUtil;

public class PlayerInteractListener implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    String itemId = NexoItems.idFromItem(player.getInventory().getItemInMainHand());
    if(itemId == null) return;
    if(!NexoAddon.getInstance().isComponentSupport()) return;

    Components componentItem = NexoAddon.getInstance().getComponents().get(itemId);

    if(componentItem == null) return;

    if(!componentItem.isEquippable()) return;

    ItemStack item = player.getInventory().getItemInMainHand().clone();
    item.setAmount(1);
    InventoryUtil.removePartialStack(player, 1);
    player.getInventory().setHelmet(item);
  }
}
