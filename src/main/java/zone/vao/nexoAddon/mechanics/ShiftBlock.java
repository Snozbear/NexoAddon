package zone.vao.nexoAddon.mechanics;

import org.bukkit.Material;

import java.util.List;

public record ShiftBlock(String replaceTo, int time, List<Material> materials, List<String> nexoIds, Boolean onInteract, Boolean onBreak, Boolean onPlace) {
}
