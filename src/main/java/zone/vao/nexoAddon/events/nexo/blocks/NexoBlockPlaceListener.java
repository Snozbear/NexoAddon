package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.chorusblock.NexoChorusBlockPlaceEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.places.BlockAuraListener;
import zone.vao.nexoAddon.events.nexo.blocks.places.ShiftBlockListener;

public class NexoBlockPlaceListener implements Listener {

  @EventHandler
  public void onNoteBlockPlace(NexoNoteBlockPlaceEvent event) {
    ShiftBlockListener.onShiftBlockPlace(event);
    BlockAuraListener.onBlockPlace(event);
  }

  @EventHandler
  public void onChorusBlockPlace(NexoChorusBlockPlaceEvent event) {
    BlockAuraListener.onBlockPlace(event);
  }

  @EventHandler
  public void onStringBlockPlace(NexoStringBlockPlaceEvent event) {
    BlockAuraListener.onBlockPlace(event);
  }
}
