package zone.vao.nexoAddon.classes.mechanic;

import org.bukkit.Material;

import java.util.List;

public record MiningTools(List<Material> materials, List<String> nexoIds, String type) {
}
