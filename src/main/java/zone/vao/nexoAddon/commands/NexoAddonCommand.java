package zone.vao.nexoAddon.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.utils.TotemUtil;


@CommandAlias("nexoaddon")
@CommandPermission("nexoaddon.admin")
public class NexoAddonCommand extends BaseCommand {

  @Subcommand("reload")
  public void onReload(CommandSender sender) {
    NexoAddon.getInstance().reload();
    sender.sendMessage("Reloaded " + NexoAddon.getInstance().getName());
  }

  @Subcommand("totem")
  @Syntax("<player> <customModelData|nexoID>")
  @CommandCompletion("@players @nexoItems")
  public void onTotem(CommandSender sender, String playerName, String input) {
    Player target = Bukkit.getPlayer(playerName);
    if (target == null) {
      sender.sendMessage("§cPlayer not found.");
      return;
    }

    try {
      int customModelData = Integer.parseInt(input);
      TotemUtil.playTotemAnimation(target, customModelData);
      sender.sendMessage("§aPlayed totem animation with custom model data: " + customModelData);
    } catch (NumberFormatException e) {
      TotemUtil.playTotemAnimation(target, input);
      sender.sendMessage("§aPlayed totem animation with Nexo item: " + input);
    }
  }
}