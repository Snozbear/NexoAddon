package zone.vao.nexoAddon.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;

@CommandAlias("nexoaddon")
@CommandPermission("nexoaddon.admin")
public class NexoAddonCommand extends BaseCommand {

  @Subcommand("reload")
  public void onReload(CommandSender sender) {
    NexoAddon.getInstance().reload();
    sender.sendMessage("Reloaded "+ NexoAddon.getInstance().getName());
  }

  @Subcommand("test")
  @Syntax("<name>")
  public void onTest(CommandSender sender, String name) {
    if(!(sender instanceof Player player)) return;

    player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().set(
        new NamespacedKey(NexoAddon.getInstance(), "song_key"),
        PersistentDataType.STRING,
        name
    );
    
    player.sendMessage("set to "+name);
  }
}