package zone.vao.nexoAddon.events.nexo.furnitures.breaks;

import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import org.bukkit.inventory.ItemStack;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;

import static zone.vao.nexoAddon.utils.BlockUtil.startShiftBlock;

public class ShiftBlockListener {

  public static void onShiftBlockBreak(final NexoFurnitureBreakEvent event) {

    Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
    if(mechanics == null || mechanics.getShiftBlock() == null || !mechanics.getShiftBlock().onBreak()) return;

    FurnitureMechanic furnitureMechanic = NexoFurniture.furnitureMechanic(mechanics.getShiftBlock().replaceTo());
    if(furnitureMechanic == null) return;
    ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
    if (
        (!mechanics.getShiftBlock().materials().isEmpty() && (mechanics.getShiftBlock().materials().contains(itemStack.getType())))
            && (!mechanics.getShiftBlock().nexoIds().isEmpty() && (!mechanics.getShiftBlock().nexoIds().contains(NexoItems.idFromItem(itemStack))))
    ) {
      return;
    }

    event.setCancelled(true);
    startShiftBlock(event.getBaseEntity().getLocation(), furnitureMechanic, event.getMechanic(), mechanics.getShiftBlock().time());
  }
}
