package zone.vao.nexoAddon.classes;

import lombok.Getter;
import zone.vao.nexoAddon.classes.mechanic.BigMining;
import zone.vao.nexoAddon.classes.mechanic.Repair;

@Getter
public class Mechanics {

  private final String id;
  private Repair repair;
  private BigMining bigMining;

  public Mechanics(String id) {
    this.id = id;
  }

  public void setRepair(double ration) {
    this.repair = new Repair(ration);
  }

  public void setBigMining(int radius, int depth) {
    this.bigMining = new BigMining(radius, depth);
  }

}

