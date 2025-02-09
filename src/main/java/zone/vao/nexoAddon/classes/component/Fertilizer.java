package zone.vao.nexoAddon.classes.component;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Fertilizer {
  @Getter
  public static final Map<UUID, Long> cooldowns = new HashMap<>();
  public int growthSpeedup;
  public List<String> usableOn;
  public int cooldown;

  public Fertilizer(int growthSpeedup, List<String> usableOn, int cooldown) {
    this.growthSpeedup = growthSpeedup;
    this.usableOn = usableOn;
    this.cooldown = cooldown;
  }
}
