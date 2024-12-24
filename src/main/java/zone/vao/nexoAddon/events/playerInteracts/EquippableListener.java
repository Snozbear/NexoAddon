package zone.vao.nexoAddon.events.playerInteracts;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
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

import java.util.Arrays;
import java.util.List;

public class EquippableListener {

  public static void onEquippable(final PlayerInteractEvent event){
    Player player = event.getPlayer();
    if(NexoBlocks.isCustomBlock(event.getClickedBlock())
        && getAllHelmets().contains(player.getInventory().getItemInMainHand().getType())
        && NexoItems.idFromItem(player.getInventory().getHelmet()) != null
    ) {
      return;
    }
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    String itemId = NexoItems.idFromItem(player.getInventory().getItemInMainHand());
    if (itemId == null)
      return;
    if (!NexoAddon.getInstance().isComponentSupport())
      return;

    Components componentItem = NexoAddon.getInstance().getComponents().get(itemId);
    if (componentItem == null || componentItem.getEquippable() == null)
      return;

    event.setCancelled(true);

    ItemStack item = player.getInventory().getItemInMainHand().clone();
    item.setAmount(1);
    InventoryUtil.removePartialStack(player, 1);

    ItemStack helmet = player.getInventory().getHelmet();
    if (helmet != null && helmet.getType() != Material.AIR) {
      helmet = helmet.clone();
    }

    switch (componentItem.getEquippable().getSlot()) {
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

    ItemStack finalHelmet = helmet;
    new BukkitRunnable(){

      @Override
      public void run() {
        if (finalHelmet != null) {
          player.getInventory().addItem(finalHelmet);
        }
      }
    }.runTaskLater(NexoAddon.getInstance(), 2);
  }

  private static List<Material> getAllHelmets() {
    return Arrays.asList(
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
