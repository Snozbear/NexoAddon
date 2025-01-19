package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;

public record BedrockBreak(int hardness, double probability, int durabilityCost, boolean disableOnFirstLayer) {
}
