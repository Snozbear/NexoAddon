package zone.vao.nexoAddon.mechanics;

import org.bukkit.Material;

import java.util.List;

public record Repair(double ratio, int fixedAmount, List<Material> materials, List<String> nexoIds, List<Material> materialsBlacklist, List<String> nexoIdsBlacklist) {
}
