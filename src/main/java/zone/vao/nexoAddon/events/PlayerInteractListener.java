package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Material;
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

    if(componentItem.getEquippable() == null) return;

    ItemStack item = player.getInventory().getItemInMainHand().clone();
    item.setAmount(1);
    InventoryUtil.removePartialStack(player, 1);

    ItemStack helmet = player.getInventory().getHelmet();
    if(helmet != null && helmet.getType() != Material.AIR){
      helmet = helmet.clone();
    }

    switch(componentItem.getEquippable().getSlot()){
      case "CHESTPLATE":
        player.getInventory().setChestplate(item);
        break;
      case "LEGGINGS":
        player.getInventory().setLeggings(item);
        break;
      case "BOOTS":
        player.getInventory().setBoots(item);
        break;
      default:
        player.getInventory().setHelmet(item);
        break;
    }

    player.getInventory().addItem(helmet);
  }
}
