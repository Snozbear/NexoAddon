package zone.vao.nexoAddon.events.playerInteracts;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.utils.InventoryUtil;

import java.util.List;

public class EquippableListener {

  public static void onEquippable(final PlayerInteractEvent event) {
    Player player = event.getPlayer();


    if (!isValidInteraction(event, player)) {
      event.setCancelled(true);
      return;
    }
    String itemId = NexoItems.idFromItem(player.getInventory().getItemInMainHand());
    if (itemId == null || !NexoAddon.getInstance().isComponentSupport()) return;

    Components componentItem = NexoAddon.getInstance().getComponents().get(itemId);
    if (componentItem == null || componentItem.getEquippable() == null) return;

    equipItem(event, player, componentItem);
  }

  private static boolean isValidInteraction(PlayerInteractEvent event, Player player) {
    return event.getHand() == EquipmentSlot.HAND
        && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        && (event.getClickedBlock() != null && !NexoBlocks.isNexoNoteBlock(event.getClickedBlock()) || event.getClickedBlock() == null)
        && isWearingorHoldCustomHelmet(player);
  }

  private static boolean isWearingorHoldCustomHelmet(Player player) {
    ItemStack helmet = player.getInventory().getHelmet();
    return helmet != null
        && (NexoItems.idFromItem(helmet) != null
        || NexoItems.idFromItem(player.getInventory().getItemInMainHand()) != null);
  }

  private static void equipItem(PlayerInteractEvent event, Player player, Components componentItem) {
    event.setCancelled(true);

    ItemStack itemToEquip = player.getInventory().getItemInMainHand().clone();
    itemToEquip.setAmount(1);
    InventoryUtil.removePartialStack(player, player.getInventory().getItemInMainHand(), 1);

    ItemStack previousItem = getPreviouslyEquippedItem(player, componentItem.getEquippable().getSlot());

    equipToSlot(player, componentItem, itemToEquip);

    returnPreviousItemToInventory(player, previousItem);
  }

  private static ItemStack getPreviouslyEquippedItem(Player player, EquipmentSlot slot) {
    return player.getInventory().getItem(slot);
  }

  private static void equipToSlot(Player player, Components componentItem, ItemStack itemToEquip) {
    player.getInventory().setItem(componentItem.getEquippable().getSlot(), itemToEquip);
  }

  private static void returnPreviousItemToInventory(Player player, ItemStack previousItem) {
    if (previousItem != null && previousItem.getType() != Material.AIR) {
      new BukkitRunnable() {
        @Override
        public void run() {
          player.getInventory().addItem(previousItem);
        }
      }.runTaskLater(NexoAddon.getInstance(), 1);
    }
  }

  private static List<Material> getAllHelmets() {
    return List.of(
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_HELMET,
        Material.IRON_HELMET,
        Material.GOLDEN_HELMET,
        Material.DIAMOND_HELMET,
        Material.NETHERITE_HELMET,
        Material.TURTLE_HELMET,
        Material.CARVED_PUMPKIN,
        Material.PLAYER_HEAD,
        Material.CREEPER_HEAD,
        Material.ZOMBIE_HEAD,
        Material.SKELETON_SKULL,
        Material.WITHER_SKELETON_SKULL,
        Material.DRAGON_HEAD
    );
  }
}
