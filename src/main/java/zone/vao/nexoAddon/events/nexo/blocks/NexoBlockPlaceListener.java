package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.places.ShiftBlockListener;

public class NexoBlockPlaceListener implements Listener {

  @EventHandler
  public void onBlockPlace(NexoNoteBlockPlaceEvent event) {
    ShiftBlockListener.onShiftBlockPlace(event);
  }
}
