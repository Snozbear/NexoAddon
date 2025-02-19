package zone.vao.nexoAddon.events.chunk;

import org.bukkit.event.world.ChunkLoadEvent;
import zone.vao.nexoAddon.utils.BlockUtil;

public class BlockAura {

  public static void onLoad(ChunkLoadEvent event){

    BlockUtil.restartBlockAura(event.getChunk());
  }
}
