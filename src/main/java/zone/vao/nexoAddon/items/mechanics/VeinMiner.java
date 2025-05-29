package zone.vao.nexoAddon.items.mechanics;

import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import com.nexomc.protectionlib.ProtectionLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.utils.BlockUtil;
import zone.vao.nexoAddon.utils.EventUtil;

import java.util.*;

public record VeinMiner(int distance, boolean toggleable, boolean sameMaterial, List<Material> materials, List<String> nexoIds) {

    public static boolean isVeinMinerTool(String toolId) {
        return toolId != null && NexoAddon.getInstance().getMechanics().containsKey(toolId) && NexoAddon.getInstance().getMechanics().get(toolId).getVeinMiner() != null;
    }

    public static class VeinMinerListener implements Listener {
        private static int activeBlockBreaks = 0;
        private static final NamespacedKey key = new NamespacedKey(NexoAddon.getInstance(), "veinMinerToggleable");

        @EventHandler
        public static void onBreak(BlockBreakEvent event) {
            Player player = event.getPlayer();
            ItemStack tool = player.getInventory().getItemInMainHand();

            String toolId = NexoItems.idFromItem(tool);
            if (!VeinMiner.isVeinMinerTool(toolId)) return;

            if (activeBlockBreaks > 0) {
                activeBlockBreaks--;
                return;
            }

            VeinMiner veinMinerMechanic = NexoAddon.getInstance()
                    .getMechanics()
                    .get(toolId)
                    .getVeinMiner();
            if (veinMinerMechanic == null) return;

            PersistentDataContainer pdc = tool.getItemMeta().getPersistentDataContainer();

            if (veinMinerMechanic.toggleable()
                    && pdc.has(key, PersistentDataType.BOOLEAN)
                    && Boolean.FALSE.equals(pdc.get(key, PersistentDataType.BOOLEAN))) {
                return;
            }

            Block originBlock = event.getBlock();
            if (!isValidBlock(veinMinerMechanic, originBlock)) return;

            mineVein(player, originBlock, veinMinerMechanic, tool);
            activeBlockBreaks = 0;
        }

        private static boolean isValidBlock(VeinMiner veinMiner, Block block) {
            Material material = block.getType();

            if (veinMiner.materials().contains(material)) {
                return true;
            }
            CustomBlockMechanic mechanic = NexoBlocks.customBlockMechanic(block);
            if (mechanic != null) {
                String nexoId = mechanic.getItemID();
                return nexoId != null && veinMiner.nexoIds().contains(nexoId);
            }

            return false;
        }

        private static void mineVein(Player player, Block origin, VeinMiner mechanic, ItemStack tool) {
            Set<Block> veinBlocks = new HashSet<>();
            Queue<Block> blocksToCheck = new LinkedList<>();
            Location originLoc = origin.getLocation();
            int maxDistanceSquared = mechanic.distance() * mechanic.distance();
            Material originMaterial = origin.getType();
            String originNexoId = NexoBlocks.customBlockMechanic(origin) != null ?
                    NexoBlocks.customBlockMechanic(origin).getItemID() : null;

            blocksToCheck.add(origin);
            veinBlocks.add(origin);

            while (!blocksToCheck.isEmpty()) {
                Block current = blocksToCheck.poll();

                for (Block relative : getAdjacentBlocks(current)) {
                    if (relative.getLocation().distanceSquared(originLoc) <= maxDistanceSquared
                            && !veinBlocks.contains(relative)
                            && isValidBlock(mechanic, relative)) {

                        if (mechanic.sameMaterial()) {
                            Material relativeMaterial = relative.getType();
                            String relativeNexoId = NexoBlocks.customBlockMechanic(relative) != null ?
                                    NexoBlocks.customBlockMechanic(relative).getItemID() : null;

                            if ((originNexoId != null && originNexoId.equals(relativeNexoId)) ||
                                    (originMaterial == relativeMaterial && originNexoId == null)) {
                                veinBlocks.add(relative);
                                blocksToCheck.add(relative);
                            }
                        } else {
                            veinBlocks.add(relative);
                            blocksToCheck.add(relative);
                        }
                    }
                }
            }

            for (Block block : veinBlocks) {
                if (block.equals(origin)) continue;
                attemptBlockBreak(player, block, tool);
            }
        }

        private static void attemptBlockBreak(Player player, Block block, ItemStack tool) {
            if (isUnbreakableBlock(player, block)) return;

            activeBlockBreaks++;
            BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);

            if (!EventUtil.callEvent(blockBreakEvent)) return;

            if (blockBreakEvent.isDropItems()) {
                block.breakNaturally(tool, true, true);
            } else {
                block.setType(Material.AIR);
            }
        }

        private static List<Block> getAdjacentBlocks(Block block) {
            List<Block> adjacent = new ArrayList<>();
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        adjacent.add(block.getRelative(x, y, z));
                    }
                }
            }
            return adjacent;
        }

        private static boolean isUnbreakableBlock(Player player, Block block) {
            return block.isLiquid()
                    || BlockUtil.UNBREAKABLE_BLOCKS.contains(block.getType())
                    || !ProtectionLib.canBreak(player, block.getLocation());
        }

        @EventHandler
        public static void onToggle(final PlayerInteractEvent event) {
            Player player = event.getPlayer();
            ItemStack tool = player.getInventory().getItemInMainHand();

            String toolId = NexoItems.idFromItem(tool);
            if (!VeinMiner.isVeinMinerTool(toolId) || event.getHand() != EquipmentSlot.HAND) return;
            if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            VeinMiner veinMinerMechanic = NexoAddon.getInstance()
                    .getMechanics()
                    .get(toolId)
                    .getVeinMiner();

            if (!veinMinerMechanic.toggleable() || tool.getItemMeta() == null) return;

            var meta = tool.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();

            if (!pdc.has(key, PersistentDataType.BOOLEAN)) {
                pdc.set(key, PersistentDataType.BOOLEAN, true);
                tool.setItemMeta(meta);
                turnOn(player, pdc);
                return;
            }

            boolean isOn = pdc.get(key, PersistentDataType.BOOLEAN);
            if (isOn) {
                turnOff(player, pdc);
            } else {
                turnOn(player, pdc);
            }

            tool.setItemMeta(meta);
        }

        private static void turnOff(final Player player, PersistentDataContainer pdc) {
            pdc.set(key, PersistentDataType.BOOLEAN, false);
            Audience.audience(player)
                    .sendActionBar(MiniMessage.miniMessage().deserialize(NexoAddon.getInstance().getGlobalConfig().getString("messages.veinminer.off", "<red>VeinMiner off")));
        }

        private static void turnOn(final Player player, PersistentDataContainer pdc) {
            pdc.set(key, PersistentDataType.BOOLEAN, true);
            Audience.audience(player)
                    .sendActionBar(MiniMessage.miniMessage().deserialize(NexoAddon.getInstance().getGlobalConfig().getString("messages.veinminer.on", "<green>VeinMiner on")));
        }
    }
}