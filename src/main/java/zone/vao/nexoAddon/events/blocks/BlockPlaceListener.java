package zone.vao.nexoAddon.events.blocks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import zone.vao.nexoAddon.events.blocks.blockPlaces.SpawnerPlaceListener;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void on(BlockPlaceEvent event) {
        SpawnerPlaceListener.onBlockPlace(event);
    }

}
