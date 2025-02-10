package zone.vao.nexoAddon.events.player;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import zone.vao.nexoAddon.events.player.interacts.BigMiningToggle;
import zone.vao.nexoAddon.events.player.interacts.EquippableListener;
import zone.vao.nexoAddon.events.player.interacts.FertilizeVanillaCrops;
import zone.vao.nexoAddon.events.player.interacts.JukeboxPlayableListener;


public class PlayerInteractListener implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {

    EquippableListener.onEquippable(event);
    JukeboxPlayableListener.onJukeboxPlayable(event);
    FertilizeVanillaCrops.fertilizeVanillaCrops(event);
    BigMiningToggle.onToggle(event);
  }
}
