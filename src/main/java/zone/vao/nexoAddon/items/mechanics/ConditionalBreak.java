package zone.vao.nexoAddon.items.mechanics;

import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.items.Mechanics;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;

public record ConditionalBreak(String compare, String value1, String value2, String method, String message) {

    public static class ConditionalBreakListener implements Listener {

        @EventHandler
        public void onBreak(NexoFurnitureBreakEvent event) {
            Player player = event.getPlayer();
            if (player == null) return;

            String furnitureId = event.getMechanic().getItemID();

            Mechanics mechanics = NexoAddon.getInstance()
                    .getMechanics()
                    .get(furnitureId);
            if (mechanics == null) return;

            ConditionalBreak mechanic = mechanics.getConditionalBreak();
            if (mechanic == null) return;

            String compareOp = mechanic.compare();
            String val1 = mechanic.value1();
            String val2 = mechanic.value2();
            String method = mechanic.method();
            String message = mechanic.message();

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                val1 = PlaceholderAPI.setPlaceholders(player, val1);
                val2 = PlaceholderAPI.setPlaceholders(player, val2);
            }

// 6. Perform the comparison
            boolean conditionMet = false;
            switch (compareOp) {
                case "==":
                    conditionMet = val1.equals(val2);
                    break;
                case "!=":
                    conditionMet = !val1.equals(val2);
                    break;
                case ">":
                    try {
                        conditionMet = Double.parseDouble(val1) > Double.parseDouble(val2);
                    } catch (NumberFormatException e) {
                        conditionMet = val1.compareTo(val2) > 0;
                    }
                    break;
                case "<":
                    try {
                        conditionMet = Double.parseDouble(val1) < Double.parseDouble(val2);
                    } catch (NumberFormatException e) {
                        conditionMet = val1.compareTo(val2) < 0;
                    }
                    break;
                case ">=":
                    try {
                        conditionMet = Double.parseDouble(val1) >= Double.parseDouble(val2);
                    } catch (NumberFormatException e) {
                        conditionMet = val1.compareTo(val2) >= 0;
                    }
                    break;
                case "<=":
                    try {
                        conditionMet = Double.parseDouble(val1) <= Double.parseDouble(val2);
                    } catch (NumberFormatException e) {
                        conditionMet = val1.compareTo(val2) <= 0;
                    }
                    break;
                case "contains":
                    conditionMet = val1.contains(val2);
                    break;
                case "startsWith":
                    conditionMet = val1.startsWith(val2);
                    break;
                case "endsWith":
                    conditionMet = val1.endsWith(val2);
                    break;
            }

            // If method is ALLOW but condition not met ⇒ disallowed break
            if ("ALLOW".equalsIgnoreCase(method) && !conditionMet) {
                event.setCancelled(true);
                sendDisallowedMessage(player, mechanic.message);
            }
            // If method is DENY and condition met ⇒ disallowed break
            else if ("DENY".equalsIgnoreCase(method) && conditionMet) {
                event.setCancelled(true);
                sendDisallowedMessage(player, mechanic.message);
            }
        }

        private void sendDisallowedMessage(Player player, String rawMessage) {
            if (rawMessage == null || rawMessage.isBlank()) return;
            Audience.audience(player).sendMessage(
                    MiniMessage.miniMessage().deserialize(rawMessage)
            );
        }
    }
}
