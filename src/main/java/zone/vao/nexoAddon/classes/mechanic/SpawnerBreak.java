package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;

@Getter
public class SpawnerBreak {
    private final double probability;

    public SpawnerBreak(final double probability) {
        this.probability = probability;
    }
}
