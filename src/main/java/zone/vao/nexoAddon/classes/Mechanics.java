package zone.vao.nexoAddon.classes;

import lombok.Getter;
import zone.vao.nexoAddon.classes.mechanic.Repair;

@Getter
public class Mechanics {

  private final String id;
  private Repair repair;

  public Mechanics(String id) {
    this.id = id;
  }

  public void setRepair(double ration) {
    this.repair = new Repair(ration);
  }

}

