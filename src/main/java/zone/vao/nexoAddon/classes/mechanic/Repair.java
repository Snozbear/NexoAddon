package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;

@Getter
public class Repair {
  private final double ratio;

  public Repair(final double ratio) {
    this.ratio = ratio;
  }
}
