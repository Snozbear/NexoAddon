package zone.vao.nexoAddon.classes;

import lombok.Getter;
import zone.vao.nexoAddon.classes.component.Equippable;

@Getter
public class Components {

  public String id;
  public Equippable equippable;

  public Components(String id) {
    this.id = id;
  }

  public void setEquippable(String slot) {
    this.equippable = new Equippable(slot);
  }

}

