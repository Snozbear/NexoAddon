package zone.vao.nexoAddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import zone.vao.nexoAddon.events.blockBreaks.BigMiningListener;
import zone.vao.nexoAddon.events.blockBreaks.JukeboxSupport;
import zone.vao.nexoAddon.events.blockBreaks.ShearsBreak;
import zone.vao.nexoAddon.events.blockBreaks.SpawnerBreakListener;
import zone.vao.nexoAddon.utils.BlockUtil;

public class BlockBreakListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {

    BlockUtil.startDecay(event.getBlock().getLocation());
    JukeboxSupport.onBlockBreak(event);
    ShearsBreak.onBlockBreak(event);
    BigMiningListener.handleBlockBreak(event);
    SpawnerBreakListener.onBlockBreak(event);
  }
}
