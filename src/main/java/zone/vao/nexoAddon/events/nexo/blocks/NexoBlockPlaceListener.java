package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.NexoBlockPlaceEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.places.*;

public class NexoBlockPlaceListener implements Listener {

  @EventHandler
  public void onBlockPlace(NexoNoteBlockPlaceEvent event) {
    ShiftBlockListener.onShiftBlockPlace(event);
  }

  @EventHandler
  public void onNexoBlockPlace(NexoBlockPlaceEvent event) {
    BlockAuraListener.onBlockPlace(event);
  }
}
