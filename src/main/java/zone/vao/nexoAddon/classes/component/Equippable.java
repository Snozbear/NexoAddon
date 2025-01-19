package zone.vao.nexoAddon.classes.component;

import lombok.Getter;
import org.bukkit.inventory.EquipmentSlot;

@Getter
public record Equippable(EquipmentSlot slot) {
}
