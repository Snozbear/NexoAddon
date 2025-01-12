package zone.vao.nexoAddon.events.blockBreaks;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;


public class SpawnerListener {

    private static final String SPAWNER_TYPE_KEY = "spawnerType";

    public static void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.SPAWNER) return;

        Block block = e.getBlock();
        Player player = e.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        String nexoItemId = NexoItems.idFromItem(tool);

        Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(nexoItemId);
        if (mechanics == null) return;

        double probability = mechanics.getSpawnerBreak().getProbability();
        handleBreakingSpawner(e, block, probability);
    }

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

    private static void handleBreakingSpawner(BlockBreakEvent event, Block block, double probability) {
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        EntityType entityType = getValidEntityType(spawner.getSpawnedType());

        ItemStack spawnerItem = createSpawnerItem(entityType);

        if (Math.random() <= probability) {
            block.getWorld().dropItemNaturally(block.getLocation(), spawnerItem);
        }

        event.setExpToDrop(0);
    }

    private static ItemStack createSpawnerItem(EntityType entityType) {
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        ItemMeta spawnerMeta = spawnerItem.getItemMeta();

        if (spawnerMeta != null) {
            spawnerMeta.setDisplayName(entityType.name() + " Spawner");
            spawnerMeta.getPersistentDataContainer()
                    .set(new NamespacedKey(NexoAddon.getInstance(), SPAWNER_TYPE_KEY), PersistentDataType.STRING, entityType.name());
            spawnerItem.setItemMeta(spawnerMeta);
        }

        return spawnerItem;
    }

    private static EntityType getValidEntityType(EntityType type) {
        return (type == null || type == EntityType.UNKNOWN) ? EntityType.PIG : type;
    }

    private static EntityType getEntityTypeFromMeta(ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(NexoAddon.getInstance(), SPAWNER_TYPE_KEY);
        String spawnerType = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        try {
            return spawnerType != null ? EntityType.valueOf(spawnerType) : EntityType.PIG;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return EntityType.PIG;
        }
    }
}
