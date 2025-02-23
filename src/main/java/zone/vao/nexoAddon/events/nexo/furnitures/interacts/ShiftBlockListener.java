package zone.vao.nexoAddon.events.nexo.furnitures.interacts;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.api.events.custom_block.NexoBlockInteractEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static zone.vao.nexoAddon.utils.BlockUtil.startShiftBlock;

public class ShiftBlockListener {
  public static List<UUID> toCancelation = Collections.synchronizedList(new ArrayList<>());
  public static void onShiftBlockInteract(final NexoFurnitureInteractEvent event) {

    if(event.getHand() != EquipmentSlot.HAND) return;

    Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
    if(mechanics == null || mechanics.getShiftBlock() == null || !mechanics.getShiftBlock().onInteract()) return;

    FurnitureMechanic furnitureMechanic = NexoFurniture.furnitureMechanic(mechanics.getShiftBlock().replaceTo());
    if(furnitureMechanic == null) return;
    ItemStack itemStack = event.getItemInHand();
    if (
        (!mechanics.getShiftBlock().materials().isEmpty() && (itemStack == null || !mechanics.getShiftBlock().materials().contains(itemStack.getType())))
            && (!mechanics.getShiftBlock().nexoIds().isEmpty() && (itemStack == null || !mechanics.getShiftBlock().nexoIds().contains(NexoItems.idFromItem(itemStack))))
    ) {
      return;
    }

    event.setCancelled(true);
    toCancelation.add(event.getPlayer().getUniqueId());

    startShiftBlock(event.getBaseEntity().getLocation(), furnitureMechanic, event.getMechanic(), mechanics.getShiftBlock().time());
  }
}
