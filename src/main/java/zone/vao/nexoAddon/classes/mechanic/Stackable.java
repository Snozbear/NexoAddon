package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;

@Getter
public class Stackable {
  private final String next;
  private final String group;

  public Stackable(String next, String group) {

    this.next = next;
    this.group = group;
  }

}
