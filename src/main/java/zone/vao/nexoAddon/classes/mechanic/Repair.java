package zone.vao.nexoAddon.classes.mechanic;

import org.bukkit.Material;

import java.util.List;

public record Repair(double ratio, int fixedAmount, List<Material> materials, List<String> nexoIds) {
}
