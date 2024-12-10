package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import zone.vao.nexoAddon.NexoAddon;

import java.util.List;

public class NexoFurnitureBreakListener implements Listener {

  @EventHandler
  public void onFurnitureBreak(NexoFurnitureBreakEvent event) {
    Player p = event.getPlayer();
    ItemStack item = p.getInventory().getItemInMainHand();

    if(NexoAddon.getInstance().getGlobalConfig().getBoolean("prevent_furniture_break_with_tools", true)){
      if(isTool(item.getType()))
        event.setCancelled(true);
    }

    if(NexoAddon.getInstance().getGlobalConfig().getBoolean("prevent_furniture_break_with_sword", true)){
      if(isSword(item.getType()))
        event.setCancelled(true);
    }
  }

  private boolean isTool(Material item){
    return swords.contains(item) || pickaxes.contains(item) ||
        axes.contains(item) || shovels.contains(item) || hoes.contains(item);
  }

  private boolean isSword(Material item){
    return swords.contains(item);
  }

  List<Material> swords = List.of(
      Material.DIAMOND_SWORD,
      Material.WOODEN_SWORD,
      Material.GOLDEN_SWORD,
      Material.IRON_SWORD,
      Material.STONE_SWORD,
      Material.NETHERITE_SWORD
  );

  List<Material> pickaxes = List.of(
      Material.DIAMOND_PICKAXE,
      Material.WOODEN_PICKAXE,
      Material.GOLDEN_PICKAXE,
      Material.IRON_PICKAXE,
      Material.STONE_PICKAXE,
      Material.NETHERITE_PICKAXE
  );

  List<Material> axes = List.of(
      Material.DIAMOND_AXE,
      Material.WOODEN_AXE,
      Material.GOLDEN_AXE,
      Material.IRON_AXE,
      Material.STONE_AXE,
      Material.NETHERITE_AXE
  );

  List<Material> shovels = List.of(
      Material.DIAMOND_SHOVEL,
      Material.WOODEN_SHOVEL,
      Material.GOLDEN_SHOVEL,
      Material.IRON_SHOVEL,
      Material.STONE_SHOVEL,
      Material.NETHERITE_SHOVEL
  );

  List<Material> hoes = List.of(
      Material.DIAMOND_HOE,
      Material.WOODEN_HOE,
      Material.GOLDEN_HOE,
      Material.IRON_HOE,
      Material.STONE_HOE,
      Material.NETHERITE_HOE
  );
}
