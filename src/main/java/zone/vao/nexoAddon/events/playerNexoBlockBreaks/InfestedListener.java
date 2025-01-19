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
import zone.vao.nexoAddon.classes.mechanic.Infested;

import java.util.*;

public class InfestedListener {
    public static void onBreak(NexoNoteBlockBreakEvent event) {
        if (NexoAddon.getInstance().getMechanics().isEmpty()) return;

        Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
        if (shouldCancelEvent(event, mechanics)) return;

        Infested infested = mechanics.getInfested();
        if (Math.random() > infested.probability()) return;

        handleDrop(event, infested);
        handleParticles(event, infested);

        List<Object> allEntities = collectEntities(infested);

        if (infested.selector().equalsIgnoreCase("all")) {
            spawnAllEntities(event, allEntities);
        } else if (infested.selector().equalsIgnoreCase("random")) {
            spawnRandomEntity(event, allEntities);
        }
    }

    private static boolean shouldCancelEvent(NexoNoteBlockBreakEvent event, Mechanics mechanics) {
        return event.isCancelled()
                || mechanics == null
                || mechanics.getInfested() == null
                || !ProtectionLib.canBreak(event.getPlayer(), event.getBlock().getLocation())
                || event.getPlayer().getGameMode() == GameMode.CREATIVE;
    }

    private static void handleDrop(NexoNoteBlockBreakEvent event, Infested infested) {
        if (!infested.drop()) {
            event.setDrop(new Drop(
                    Collections.emptyList(),
                    false,
                    false,
                    Objects.requireNonNull(NexoBlocks.customBlockMechanic(event.getBlock().getLocation())).getItemID()
            ));
        }
    }

    private static void handleParticles(NexoNoteBlockBreakEvent event, Infested infested) {
        if (infested.particles()) {
            spawnParticles(event.getBlock());
        }
    }

    private static List<Object> collectEntities(Infested infested) {
        List<Object> allEntities = new ArrayList<>();
        allEntities.addAll(infested.entities());
        allEntities.addAll(infested.mythicMobs());
        return allEntities;
    }

    private static void spawnAllEntities(NexoNoteBlockBreakEvent event, List<Object> entities) {
        for (Object entity : entities) {
            if (entity instanceof EntityType) {
                spawnEntity(event, (EntityType) entity);
            } else if (entity instanceof String) {
                spawnMythicMob(event, (String) entity);
            }
        }
    }

    private static void spawnRandomEntity(NexoNoteBlockBreakEvent event, List<Object> entities) {
        if (!entities.isEmpty()) {
            Object randomEntity = entities.get(new Random().nextInt(entities.size()));
            if (randomEntity instanceof EntityType) {
                spawnEntity(event, (EntityType) randomEntity);
            } else if (randomEntity instanceof String) {
                spawnMythicMob(event, (String) randomEntity);
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
