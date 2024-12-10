package zone.vao.nexoAddon.classes.component;

import lombok.Getter;

@Getter
public class Equippable{
  public String slot;

  public Equippable(String slot){
    this.slot = slot;
  }
}
