package zone.vao.nexoAddon.events.playerNexoBlockBreaks;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.utils.drops.Drop;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;

import java.util.*;

public class InfestedListener {
    public static void onBreak(NexoNoteBlockBreakEvent event) {
        if(NexoAddon.getInstance().getMechanics().isEmpty()) return;

        Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
        if (event.isCancelled()
            || mechanics == null
            || mechanics.getInfested() == null
            || !ProtectionLib.canBreak(event.getPlayer(),event.getBlock().getLocation())
            || event.getPlayer().getGameMode() == GameMode.CREATIVE
        ) return;

        double probability = mechanics.getInfested().getProbability();
        if (Math.random() > probability) return;

        List<EntityType> entities = mechanics.getInfested().getEntities();
        List<String> mythicMobs = mechanics.getInfested().getMythicMobs();
        String selector = mechanics.getInfested().getSelector();
        boolean spawnParticles = mechanics.getInfested().isParticles();
        boolean drop = mechanics.getInfested().isDrop();

        if (!drop) event.setDrop(new Drop(Collections.emptyList(), false, false, Objects.requireNonNull(NexoBlocks.customBlockMechanic(event.getBlock().getLocation())).getItemID()));

        if (spawnParticles) spawnParticles(event.getBlock());

        if (selector.equalsIgnoreCase("all")) {
            for (EntityType entityType : entities) {
                spawnEntity(event, entityType);
            }
            for (String mythicMob : mythicMobs) {
                spawnMythicMob(event, mythicMob);
            }
        } else if (selector.equalsIgnoreCase("random")) {
            List<Object> allEntities = new ArrayList<>();
            allEntities.addAll(entities);
            allEntities.addAll(mythicMobs);

            if (!allEntities.isEmpty()) {
                Random random = new Random();
                Object randomEntity = allEntities.get(random.nextInt(allEntities.size()));

                if (randomEntity instanceof EntityType) {
                    spawnEntity(event, (EntityType) randomEntity);
                } else if (randomEntity instanceof String) {
                    spawnMythicMob(event, (String) randomEntity);
                }
            }
        }

    }

    private static void spawnEntity(NexoNoteBlockBreakEvent event, EntityType entityType) {
        event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0, 0.5), entityType);
    }

    private static void spawnMythicMob(NexoNoteBlockBreakEvent event, String mobName) {
        if (MythicBukkit.inst().getMobManager().getMythicMob(mobName).isPresent()) {
            MythicBukkit.inst().getMobManager().spawnMob(
                    mobName,
                    new Location(event.getBlock().getWorld(), event.getBlock().getX() + 0.5, event.getBlock().getY(), event.getBlock().getZ() + 0.5)
            );
        } else {
            NexoAddon.getInstance().getLogger().warning("MythicMob not found: " + mobName);
        }
    }

    private static void spawnParticles(Block block) {
        block.getWorld().spawnParticle(Particle.WHITE_SMOKE, block.getLocation().add(0.5, 0.5, 0.5), 10);
    }
}
