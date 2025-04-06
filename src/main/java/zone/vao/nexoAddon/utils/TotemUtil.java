package zone.vao.nexoAddon.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.nexomc.nexo.api.NexoItems;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class TotemUtil {

    public static void playTotemAnimation(Player player, int customModelData) {
        org.bukkit.inventory.ItemStack bukkitItem = new org.bukkit.inventory.ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = bukkitItem.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(customModelData);
            bukkitItem.setItemMeta(meta);
        }
        sendCustomTotemAnimation(player, bukkitItem);
    }

    public static void playTotemAnimation(Player player, String nexoID) {
        if (NexoItems.itemFromId(nexoID) == null) return;
        org.bukkit.inventory.ItemStack totem = NexoItems.itemFromId(nexoID).build();
        sendCustomTotemAnimation(player, totem);
    }

    private static void sendCustomTotemAnimation(Player player, org.bukkit.inventory.ItemStack bukkitItem) {
        if (bukkitItem.getType() != Material.TOTEM_OF_UNDYING)
            throw new IllegalArgumentException("ItemStack " + bukkitItem + " isn't a Totem of Undying!");

        ItemStack packetItem = SpigotConversionUtil.fromBukkitItemStack(bukkitItem);

        WrapperPlayServerSetSlot setSlotPacket = new WrapperPlayServerSetSlot(
                0,
                0,
                45,
                packetItem
        );

        WrapperPlayServerEntityStatus entityStatusPacket = new WrapperPlayServerEntityStatus(
                player.getEntityId(),
                (byte) 35
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, setSlotPacket);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, entityStatusPacket);

        player.updateInventory();
    }
}