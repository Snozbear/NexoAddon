package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import zone.vao.nexoAddon.NexoAddon;

public class BlockBreakListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if(event.getBlock().getType() == Material.JUKEBOX){

      String soundKey = NexoAddon.getInstance().jukeboxLocations.get(event.getBlock().getLocation().toString());
      if(soundKey == null) return;

      Jukebox jukebox = (Jukebox) event.getBlock().getState();

      String itemId = jukebox.getRecord().getItemMeta().getDisplayName();
      ItemBuilder itemBuilder = NexoItems.itemFromId(itemId);
      if(itemBuilder == null) return;
      jukebox.setRecord(null);
      jukebox.update();
      jukebox.getWorld().dropItemNaturally(jukebox.getLocation(), itemBuilder.build().clone());

      event.getBlock().getWorld().getPlayers().forEach(player -> player.stopSound(soundKey, SoundCategory.RECORDS));
      NexoAddon.getInstance().jukeboxLocations.remove(event.getBlock().getLocation().toString());
    }
  }
}
