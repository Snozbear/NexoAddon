package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import zone.vao.nexoAddon.events.nexoBlockPlaces.ShiftBlockListener;

public class NexoBlockPlaceListener implements Listener {

  @EventHandler
  public void onBlockPlace(NexoNoteBlockPlaceEvent event) {
    ShiftBlockListener.onShiftBlockPlace(event);
  }
}
