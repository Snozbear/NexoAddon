package zone.vao.nexoAddon.classes.component;

import lombok.Getter;

import java.util.List;

public record Fertilizer(int growthSpeedup, List<String> usableOn) {
}
