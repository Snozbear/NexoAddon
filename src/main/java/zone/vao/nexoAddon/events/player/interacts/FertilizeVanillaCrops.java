package zone.vao.nexoAddon.events.player.interacts;

import com.nexomc.nexo.api.NexoItems;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.Damageable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.components.Components;
import zone.vao.nexoAddon.components.Fertilizer;
import zone.vao.nexoAddon.utils.EventUtil;
import zone.vao.nexoAddon.utils.InventoryUtil;

public class FertilizeVanillaCrops {

  public static void fertilizeVanillaCrops(PlayerInteractEvent event) {
    if (!isValidEvent(event)) return;

    Player player = event.getPlayer();
    Block clickedBlock = event.getClickedBlock();
    String itemId = NexoItems.idFromItem(event.getItem());
    Components component = NexoAddon.getInstance().getComponents().get(itemId);

    if (!canApplyFertilizer(player, clickedBlock, component)) return;

    int cooldown = component.getFertilizer().cooldown;
    if (cooldown > 0) {
      long now = System.currentTimeMillis();
      if (Fertilizer.cooldowns.containsKey(player.getUniqueId()) && Fertilizer.cooldowns.get(player.getUniqueId()) > now) {
        return;
      }
    }

    int growthSpeedup = Math.max(0, component.getFertilizer().growthSpeedup);
    boolean appliedSuccessfully = applyFertilizer(clickedBlock, growthSpeedup, event.getBlockFace());

    if (!appliedSuccessfully) return;

    if (cooldown > 0) {
      Fertilizer.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldown * 1000L);
    }

    event.setCancelled(true);
    if(EventUtil.callEvent(event))
      handleItemDurability(player, component);
  }

  private static boolean isValidEvent(PlayerInteractEvent event) {
    return event.getHand() == EquipmentSlot.HAND
        && (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        && event.getClickedBlock() != null
        && event.getItem() != null
        && NexoItems.idFromItem(event.getItem()) != null;
  }

  private static boolean canApplyFertilizer(Player player, Block block, Components component) {
    if (component == null || component.getFertilizer() == null) return false;

    boolean canInteract = ProtectionLib.canInteract(player, block.getLocation()) &&
        ProtectionLib.canUse(player, block.getLocation());
    boolean isUsableOnBlock = component.getFertilizer()
        .usableOn
        .stream()
        .anyMatch(blockType -> blockType.equals("_MINECRAFT") ||
            blockType.equals(block.getType().toString().toUpperCase()));

    return canInteract && isUsableOnBlock;
  }

  private static boolean applyFertilizer(Block block, int growthSpeedup, org.bukkit.block.BlockFace blockFace) {
    boolean applied = false;
    for (int i = 0; i < growthSpeedup; i++) {
      if (block.applyBoneMeal(blockFace)) {
        applied = true;
      }
    }
    return applied;
  }

  private static void handleItemDurability(Player player, Components component) {
    int maxDurability = NexoItems.itemFromId(component.getId()).getDurability() != null
        ? NexoItems.itemFromId(component.getId()).getDurability()
        : 0;

    Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
    if (maxDurability <= 1 || (itemMeta.hasDamage() && itemMeta.getDamage() >= maxDurability)) {
      InventoryUtil.removePartialStack(player, player.getInventory().getItemInMainHand(), 1);
    } else {
      itemMeta.setDamage(itemMeta.getDamage() + 1);
      player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
    }
  }
}
