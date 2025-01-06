package zone.vao.nexoAddon.events.blockBreaks;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import zone.vao.nexoAddon.NexoAddon;

public class JukeboxSupport {

  public static void onBlockBreak(BlockBreakEvent event){
    if(event.getBlock().getType() == Material.JUKEBOX){

      String soundKey = NexoAddon.getInstance().jukeboxLocations.get(event.getBlock().getLocation().toString());
      if(soundKey == null || !ProtectionLib.canBreak(event.getPlayer(), event.getBlock().getLocation())) return;

      Jukebox jukebox = (Jukebox) event.getBlock().getState();

      if(jukebox.getRecord().getItemMeta() == null) return;

      String itemId = jukebox.getRecord().getItemMeta().getDisplayName();
      ItemBuilder itemBuilder = NexoItems.itemFromId(itemId);
      if(itemBuilder == null) return;
      jukebox.setRecord(null);
      jukebox.update();
      jukebox.getWorld().dropItemNaturally(jukebox.getLocation(), itemBuilder.build().clone());

      event.getBlock().getWorld().getNearbyEntities(jukebox.getLocation(), 16,16,16).forEach(entity -> {
        if(entity instanceof Player player){
          player.stopSound(soundKey, SoundCategory.RECORDS);
        }
      });
      NexoAddon.getInstance().jukeboxLocations.remove(event.getBlock().getLocation().toString());
    }
  }
}
