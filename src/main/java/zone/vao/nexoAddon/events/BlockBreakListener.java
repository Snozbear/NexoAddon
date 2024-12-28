package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
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
  }
}
