package zone.vao.nexoAddon.hooks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.handlers.BlockHardnessHandler;

public class PacketEventsHook {

  public static void registerListener(){
    NexoAddon.getInstance().blockHardnessHandler = new BlockHardnessHandler();
    NexoAddon.getInstance().packetListenerCommon = PacketEvents.getAPI().getEventManager().registerListener(
        NexoAddon.getInstance().getBlockHardnessHandler(), PacketListenerPriority.NORMAL);
  }
}
