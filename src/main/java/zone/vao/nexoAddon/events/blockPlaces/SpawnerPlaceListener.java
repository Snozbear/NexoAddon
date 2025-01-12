package zone.vao.nexoAddon.events.blockPlaces;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;

import java.util.logging.Logger;

public class SpawnerPlaceListener {

    private static final String SPAWNER_TYPE_KEY = "spawnerType";
    private static final Logger LOGGER = Logger.getLogger(NexoAddon.class.getName());


    public static void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.SPAWNER) return;

        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;

        EntityType entityType = getEntityTypeFromMeta(meta);
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        spawner.setSpawnedType(entityType);
        spawner.update();
    }

    private static EntityType getEntityTypeFromMeta(ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(NexoAddon.getInstance(), SPAWNER_TYPE_KEY);
        String spawnerType = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        try {
            return spawnerType != null ? EntityType.valueOf(spawnerType) : EntityType.PIG;
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid spawner type found in metadata: " + spawnerType);
            return EntityType.PIG;
        }
    }
}
