package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexoBlocksInteracts.ShiftBlockListener;

public class NexoBlockInteractListener implements Listener {

  @EventHandler
  public void onInteract(final NexoNoteBlockInteractEvent event) {

    ShiftBlockListener.onShiftBlockInteract(event);
  }
}
