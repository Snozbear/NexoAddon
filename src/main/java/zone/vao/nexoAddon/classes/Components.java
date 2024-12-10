package zone.vao.nexoAddon.classes;

import lombok.Getter;

@Getter
public class Components {

  public String id;
  public boolean equippable;

  public Components(String id, boolean equippable) {
    this.id = id;
    this.equippable = equippable;
  }

}
