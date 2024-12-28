package zone.vao.nexoAddon.events.blockBreaks;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.utils.drops.Drop;
import com.nexomc.nexo.utils.drops.Loot;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import zone.vao.nexoAddon.NexoAddon;

import java.util.ArrayList;
import java.util.List;

public class ShearsBreak {

  public static void onBlockBreak(BlockBreakEvent event) {

    if(!NexoAddon.getInstance().getGlobalConfig().getBoolean("count_shears_as_silktouch", false)
        || !NexoBlocks.isNexoStringBlock(event.getBlock())
        || !event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SHEARS)
        || !NexoAddon.getInstance().getIdsWithSilktouch().contains(NexoBlocks.stringMechanic(event.getBlock()).getItemID())
    ) return;


    event.setCancelled(true);
    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), NexoItems.itemFromId(NexoBlocks.stringMechanic(event.getBlock()).getItemID()).build());

    List<Loot> loots = new ArrayList<>();
    Drop drop = new Drop(loots, false, false, NexoBlocks.stringMechanic(event.getBlock()).getItemID());
    NexoBlocks.remove(event.getBlock().getLocation(), null, drop);
  }
}
