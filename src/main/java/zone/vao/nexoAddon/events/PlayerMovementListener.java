package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.events.playerMovements.FurnituresRaytrace;
import zone.vao.nexoAddon.utils.BossBarUtil;
import zone.vao.nexoAddon.utils.RayTraceUtil;

public class PlayerMovementListener implements Listener {

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {

    if(NexoAddon.getInstance().getGlobalConfig().getBoolean("boss_bar", true))
      FurnituresRaytrace.onFurnituresRaytrace(event);
  }
}
