package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.interacts.Stackable;
import zone.vao.nexoAddon.events.nexo.blocks.interacts.Unstackable;

public class NexoStringBlockInteractListener implements Listener {

  @EventHandler
  public void onNexoStringBlockInteract(final NexoStringBlockInteractEvent event) {

    Stackable.onStackable(event);
    Unstackable.onUntackable(event);
  }
}
