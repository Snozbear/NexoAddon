package zone.vao.nexoAddon.events.playerInteracts;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.Damageable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.utils.InventoryUtil;

public class FertilizeVanillaCrops {

  public static void fertilizeVanillaCrops(PlayerInteractEvent event) {

    if(event.getHand() != EquipmentSlot.HAND
        || event.getClickedBlock() == null
        || event.getItem() == null
    ) return;

    String itemId = NexoItems.idFromItem(event.getItem());
    Player player = event.getPlayer();
    if(itemId == null
        || NexoAddon.getInstance().getComponents() == null
        || NexoAddon.getInstance().getComponents().get(itemId) == null
        || !(NexoAddon.getInstance().getComponents().get(itemId).getFertilizer().getUsableOn().contains("_MINECRAFT")
              || NexoAddon.getInstance().getComponents().get(itemId).getFertilizer().getUsableOn().contains(event.getClickedBlock().getType().toString().toUpperCase()))
        || !(ProtectionLib.canInteract(player, event.getClickedBlock().getLocation()) && ProtectionLib.canUse(player, event.getClickedBlock().getLocation()))
    ) return;

    NexoAddon.getInstance().getComponents().get(itemId).getFertilizer().getGrowthSpeedup();
    boolean appliedAtLeastOnce = false;
    Block clickedBlock = event.getClickedBlock();
    Components component = NexoAddon.getInstance().getComponents().get(itemId);
    int growthSpeedup = component.getFertilizer().getGrowthSpeedup() < 1 ? 0 : component.getFertilizer().getGrowthSpeedup();

    for (int i = 0; i < growthSpeedup; i++) {
      boolean appliedBoneMeal = clickedBlock.applyBoneMeal(event.getBlockFace());
      if (appliedBoneMeal) {
        appliedAtLeastOnce = true;
      }
    }

    if (!appliedAtLeastOnce) return;

    event.setCancelled(true);

    if(NexoItems.itemFromId(component.getId()).getDurability() == null
        || NexoItems.itemFromId(component.getId()).getDurability() <= 1
        || ((Damageable) player.getInventory().getItemInMainHand().getItemMeta()).hasDamage()
        && ((Damageable) player.getInventory().getItemInMainHand().getItemMeta()).getDamage() >= NexoItems.itemFromId(component.getId()).getDurability()
    ) {
      InventoryUtil.removePartialStack(player, player.getInventory().getItemInMainHand(), 1);
    }else{
      Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
      itemMeta.setDamage(itemMeta.getDamage()+1);
      player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
    }
  }
}
