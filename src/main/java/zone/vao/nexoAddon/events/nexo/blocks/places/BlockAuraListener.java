package zone.vao.nexoAddon.events.nexo.blocks.places;

import com.jeff_media.customblockdata.CustomBlockData;
import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.events.custom_block.NexoBlockPlaceEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.mechanics.Mechanics;
import zone.vao.nexoAddon.utils.BlockUtil;

public class BlockAuraListener {
    public static void onBlockPlace(NexoBlockPlaceEvent event) {
        if(NexoAddon.getInstance().getMechanics().isEmpty()) return;

        Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
        if (mechanics == null || mechanics.getBlockAura() == null) return;
        Particle particle = mechanics.getBlockAura().particle();
        Location location = event.getBlock().getLocation();
        String xOffsetRange = mechanics.getBlockAura().xOffset();
        String yOffsetRange = mechanics.getBlockAura().yOffset();
        String zOffsetRange = mechanics.getBlockAura().zOffset();
        int amount = mechanics.getBlockAura().amount();
        double deltaX = mechanics.getBlockAura().deltaX();
        double deltaY = mechanics.getBlockAura().deltaY();
        double deltaZ = mechanics.getBlockAura().deltaZ();
        double speed = mechanics.getBlockAura().speed();
        boolean force = mechanics.getBlockAura().force();

        if (!event.isCancelled()) {
            BlockUtil.startBlockAura(particle, location, xOffsetRange, yOffsetRange, zOffsetRange, amount, deltaX, deltaY, deltaZ, speed, force);
            CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), NexoAddon.getInstance());
            customBlockData.set(new NamespacedKey(NexoAddon.getInstance(), "blockAura"), PersistentDataType.STRING, NexoBlocks.customBlockMechanic(location).getItemID());
        }
    }
}