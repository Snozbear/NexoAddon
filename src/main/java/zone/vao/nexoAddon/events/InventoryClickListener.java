package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;

public class InventoryClickListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player player) || !event.isLeftClick()) return;

    if (event.getCursor() == null || event.getCurrentItem() == null) return;

    ItemStack cursorItem = event.getCursor().clone();
    ItemStack currentItem = event.getCurrentItem().clone();
    String itemId = NexoItems.idFromItem(cursorItem);
    if (itemId == null
        || !(currentItem.getItemMeta() instanceof Damageable clickedItemMeta)
        || !clickedItemMeta.hasDamage()
        || !NexoAddon.getInstance().getMechanics().containsKey(itemId)
        || NexoAddon.getInstance().getMechanics().get(itemId).getRepair() == null
        || (
            NexoItems.idFromItem(currentItem) != null
            && NexoAddon.getInstance().getMechanics().containsKey(NexoItems.idFromItem(currentItem))
            && NexoAddon.getInstance().getMechanics().get(NexoItems.idFromItem(currentItem)).getRepair() != null
          )
    ) return;

    event.setCancelled(true);

    Mechanics mechanic = NexoAddon.getInstance().getMechanics().get(itemId);
    double ratio = mechanic.getRepair().getRatio();

    int maxDurability = NexoItems.itemFromId(mechanic.getId()).getDurability() != null
        ? NexoItems.itemFromId(mechanic.getId()).getDurability()
        : 0;

    int repairAmount = (int) (clickedItemMeta.getDamage() * ratio);
    int newDamage = Math.max(0, clickedItemMeta.getDamage() - repairAmount);
    clickedItemMeta.setDamage(newDamage);
    currentItem.setItemMeta(clickedItemMeta);

    if (maxDurability != 0) {
      Damageable holdingMeta = (Damageable) cursorItem.getItemMeta();
      int currentDamage = holdingMeta.getDamage();
      int newHoldingDamage = currentDamage + 1;

      if (newHoldingDamage >= maxDurability) {
        cursorItem.setAmount(cursorItem.getAmount() - 1);
        if (cursorItem.getAmount() <= 0) {
          cursorItem = null;
        } else {
          holdingMeta.setDamage(0);
          cursorItem.setItemMeta(holdingMeta);
        }
      } else {
        holdingMeta.setDamage(newHoldingDamage);
        cursorItem.setItemMeta(holdingMeta);
      }
    } else{
      if(cursorItem.getAmount() > 1) {
        cursorItem.setAmount(cursorItem.getAmount() - 1);
      }else{
        cursorItem = null;
      }
    }

    event.getClickedInventory().setItem(event.getSlot(), currentItem);

    player.getInventory().addItem(cursorItem == null ? new ItemStack(Material.AIR) : cursorItem);
    event.getCursor().setType(Material.AIR);
    player.setItemOnCursor(null);

    player.updateInventory();
  }
}
