package zone.vao.nexoAddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.populators.orePopulator.CustomOrePopulator;

import java.util.ArrayList;

public class WorldLoadListener implements Listener {

  @EventHandler
  public void onWorldLoad(WorldLoadEvent event) {
    NexoAddon.getInstance().getOrePopulator().getOres().forEach(ore -> {
      if(ore.getNexoFurniture() != null) return;
      for (String worldName : ore.getWorldNames()) {
        if(!worldName.equals(event.getWorld().getName())) return;
        CustomOrePopulator customOrePopulator = new CustomOrePopulator(NexoAddon.getInstance().getOrePopulator());
        if(!NexoAddon.getInstance().getWorldPopulators().containsKey(worldName)) {
          NexoAddon.getInstance().getWorldPopulators().put(worldName, new ArrayList<>());
        }
        NexoAddon.getInstance().addPopulatorToWorld(event.getWorld(), customOrePopulator);
        NexoAddon.getInstance().getWorldPopulators().get(event.getWorld()).add(customOrePopulator);
        NexoAddon.getInstance().logPopulatorAdded("BlockPopulator", ore.getId(), event.getWorld());
      }
    });
  }
}
