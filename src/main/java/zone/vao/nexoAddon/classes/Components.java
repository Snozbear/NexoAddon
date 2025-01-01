package zone.vao.nexoAddon.classes;

import lombok.Getter;
import org.bukkit.inventory.EquipmentSlot;
import zone.vao.nexoAddon.classes.component.Equippable;
import zone.vao.nexoAddon.classes.component.JukeboxPlayable;

@Getter
public class Components {

  public String id;
  public Equippable equippable;
  public JukeboxPlayable playable;

  public Components(String id) {
    this.id = id;
  }

  public void setEquippable(EquipmentSlot slot) {
    this.equippable = new Equippable(slot);
  }

  public void setPlayable(String songKey) {
    this.playable = new JukeboxPlayable(songKey);
  }

}

