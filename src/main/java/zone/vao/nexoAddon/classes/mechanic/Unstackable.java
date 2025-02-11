package zone.vao.nexoAddon.classes.mechanic;

import org.bukkit.Material;

import java.util.List;

public record Unstackable(String next, String give, List<Material> materials, List<String> nexoIds) {
}
