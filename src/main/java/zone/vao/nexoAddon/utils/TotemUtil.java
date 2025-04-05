package zone.vao.nexoAddon.utils;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TotemUtil {
    public static void playTotemAnimation(Player player, int customModelData) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = totem.getItemMeta();
        if (meta == null) return;
        meta.setCustomModelData(customModelData);
        totem.setItemMeta(meta);

        ItemStack previous = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(totem);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        player.getInventory().setItemInMainHand(previous);
    }
    public static void playTotemAnimation(Player player, String nexoID) {
        if (NexoItems.itemFromId(nexoID) == null) return;
        ItemStack totem = NexoItems.itemFromId(nexoID).build();
        ItemStack previous = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(totem);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        player.getInventory().setItemInMainHand(previous);
    }
}
