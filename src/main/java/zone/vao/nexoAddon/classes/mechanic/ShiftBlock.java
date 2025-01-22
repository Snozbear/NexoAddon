package zone.vao.nexoAddon.classes.mechanic;

import org.bukkit.Material;

import java.util.List;

public record ShiftBlock(String replaceTo, int time, List<Material> materials, List<String> nexoIds) {
}
