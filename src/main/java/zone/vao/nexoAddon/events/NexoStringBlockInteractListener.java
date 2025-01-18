package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;

public class NexoStringBlockInteractListener implements Listener {

  @EventHandler
  public void onNexoStringBlockInteract(final NexoStringBlockInteractEvent event) {
    if(NexoAddon.getInstance().getMechanics().isEmpty()) return;

    String itemId = NexoItems.idFromItem(event.getItemInHand());
    if(itemId == null) return;
    String StringBlockId = event.getMechanic().getItemID();

    Mechanics mechanicsItem = NexoAddon.getInstance().getMechanics().get(itemId);
    Mechanics mechanicsStringBlock = NexoAddon.getInstance().getMechanics().get(StringBlockId);
    if(mechanicsStringBlock == null || mechanicsStringBlock.getStackable() == null
        || mechanicsItem == null || mechanicsItem.getStackable() == null
    ) return;

    if(!mechanicsStringBlock.getStackable().getGroup().equalsIgnoreCase(mechanicsItem.getStackable().getGroup())) return;


  }
}
