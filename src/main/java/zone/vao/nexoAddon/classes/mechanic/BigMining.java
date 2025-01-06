package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;

@Getter
public class BigMining {
  private final int radius;
  private final int depth;

  public BigMining(final int radius, final int depth) {
    this.radius = radius;
    this.depth = depth;
  }
}
