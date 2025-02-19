package zone.vao.nexoAddon.events.nexo.blocks.breaks;

import com.nexomc.nexo.api.events.custom_block.NexoBlockBreakEvent;
import org.bukkit.Location;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;
import zone.vao.nexoAddon.utils.BlockUtil;

public class BlockAuraListener {
    public static void onBlockBreak(NexoBlockBreakEvent event) {
        if(NexoAddon.getInstance().getMechanics().isEmpty()) return;

        Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
        if (mechanics == null || mechanics.getBlockAura() == null) return;
        Location location = event.getBlock().getLocation();
        if(!event.isCancelled()) {
            BlockUtil.stopBlockAura(location);
        }
    }
}