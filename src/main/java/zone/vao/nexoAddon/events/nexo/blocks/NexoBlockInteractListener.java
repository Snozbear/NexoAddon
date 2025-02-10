package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.nexoBlocksInteracts.ShiftBlockListener;

public class NexoBlockInteractListener implements Listener {

  @EventHandler
  public void onInteract(final NexoNoteBlockInteractEvent event) {

    ShiftBlockListener.onShiftBlockInteract(event);
  }
}
