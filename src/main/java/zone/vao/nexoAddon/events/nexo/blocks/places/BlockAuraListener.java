package zone.vao.nexoAddon.events.nexo.blocks.places;

import com.nexomc.nexo.api.events.custom_block.NexoBlockPlaceEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;
import zone.vao.nexoAddon.utils.BlockUtil;

public class BlockAuraListener {
    public static void onBlockPlace(NexoBlockPlaceEvent event) {
        if(NexoAddon.getInstance().getMechanics().isEmpty()) return;

        Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
        if (mechanics == null || mechanics.getBlockAura() == null) return;
        Particle particle = mechanics.getBlockAura().particle();
        Location location = event.getBlock().getLocation();
        double xOffset = mechanics.getBlockAura().xOffset();
        double yOffset = mechanics.getBlockAura().yOffset();
        double zOffset = mechanics.getBlockAura().zOffset();
        int amount = mechanics.getBlockAura().amount();
        double deltaX = mechanics.getBlockAura().deltaX();
        double deltaY = mechanics.getBlockAura().deltaY();
        double deltaZ = mechanics.getBlockAura().deltaZ();
        double speed = mechanics.getBlockAura().speed();

        if(!event.isCancelled()) {
            BlockUtil.startBlockAura(particle, location, xOffset, yOffset, zOffset, amount, deltaX, deltaY, deltaZ, speed);
        }
    }
}