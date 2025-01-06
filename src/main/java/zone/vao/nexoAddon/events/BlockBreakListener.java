package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoFurniture;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import zone.vao.nexoAddon.events.blockBreaks.BigMiningListener;
import zone.vao.nexoAddon.events.blockBreaks.JukeboxSupport;
import zone.vao.nexoAddon.events.blockBreaks.ShearsBreak;

public class BlockBreakListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {

    JukeboxSupport.onBlockBreak(event);
    ShearsBreak.onBlockBreak(event);
    BigMiningListener.handleBlockBreak(event);
  }
}
