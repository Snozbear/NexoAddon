package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.chorusblock.NexoChorusBlockPlaceEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.places.ShiftBlockListener;
import zone.vao.nexoAddon.mechanics.BlockAura;

public class NexoBlockPlaceListener implements Listener {

  @EventHandler
  public void onNoteBlockPlace(NexoNoteBlockPlaceEvent event) {
    ShiftBlockListener.onShiftBlockPlace(event);
    BlockAura.BlockAuraListener.onBlockPlace(event);
  }

  @EventHandler
  public void onChorusBlockPlace(NexoChorusBlockPlaceEvent event) {
    BlockAura.BlockAuraListener.onBlockPlace(event);
  }

  @EventHandler
  public void onStringBlockPlace(NexoStringBlockPlaceEvent event) {
    BlockAura.BlockAuraListener.onBlockPlace(event);
  }
}
