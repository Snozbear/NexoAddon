package zone.vao.nexoAddon.events.playerInteracts;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.utils.InventoryUtil;
import zone.vao.nexoAddon.utils.VersionUtil;


public class JukeboxPlayableListener {

  public static void onJukeboxPlayable(final PlayerInteractEvent event) {
    if(!VersionUtil.isVersionLessThan("1.21")) return;
    Player player = event.getPlayer();
    if (event.getHand() != EquipmentSlot.HAND || event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.JUKEBOX) {
      return;
    }

    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    if (!(event.getClickedBlock().getState() instanceof Jukebox jukebox)) {
      return;
    }

    if (jukebox.hasRecord() && NexoItems.idFromItem(jukebox.getRecord()) != null) {
      Components componentItem = NexoAddon.getInstance().getComponents().get(NexoItems.idFromItem(jukebox.getRecord()));
      if (componentItem != null && componentItem.getPlayable() != null) {
        for (Player p : Bukkit.getOnlinePlayers()) {
          p.stopSound(componentItem.getPlayable().getSongKey(), SoundCategory.RECORDS);
        }
      }

      jukebox.eject();
      NexoAddon.getInstance().jukeboxLocations.remove(jukebox.getLocation().toString());
      return;
    }

    String itemId = NexoItems.idFromItem(player.getInventory().getItemInMainHand());
    if (itemId == null || !NexoAddon.getInstance().isComponentSupport()) {
      return;
    }

    Components componentItem = NexoAddon.getInstance().getComponents().get(itemId);
    if (componentItem == null || componentItem.getPlayable() == null) {
      return;
    }

    event.setCancelled(true);

    ItemStack item = player.getInventory().getItemInMainHand().clone();
    item.setAmount(1);
    jukebox.setRecord(item);
    jukebox.update();

    jukebox.getWorld().playSound(
        jukebox.getLocation(),
        componentItem.getPlayable().getSongKey(),
        SoundCategory.RECORDS,
        1f,
        1f
    );

    NexoAddon.getInstance().jukeboxLocations.put(jukebox.getLocation().toString(), componentItem.getPlayable().getSongKey());

    InventoryUtil.removePartialStack(player, 1);
  }

}
