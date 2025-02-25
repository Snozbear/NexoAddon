package zone.vao.nexoAddon.events.player;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import zone.vao.nexoAddon.events.player.interacts.*;


public class PlayerInteractListener implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {

    EquippableListener.onEquippable(event);
    JukeboxPlayableListener.onJukeboxPlayable(event);
    FertilizeVanillaCrops.fertilizeVanillaCrops(event);
    BottledExpListener.onBottledExp(event);
  }
}
