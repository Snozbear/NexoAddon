package zone.vao.nexoAddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.events.blockBreaks.JukeboxSupport;
import zone.vao.nexoAddon.events.blockBreaks.ShearsBreak;

public class BlockBreakListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {

    JukeboxSupport.onBlockBreak(event);
    ShearsBreak.onBlockBreak(event);
    event.setCancelled(NexoAddon.getInstance().getGlobalConfig().getBoolean("double_hit_destroy_mechanic", true));
  }
}
