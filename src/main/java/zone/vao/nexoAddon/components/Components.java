package zone.vao.nexoAddon.components;

import lombok.Getter;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

@Getter
public class Components {

  private final String id;
  private Equippable equippable;
  private JukeboxPlayable playable;
  private Fertilizer fertilizer;
  private SkullValue skullValue;

  public Components(String id) {
    this.id = id;
  }

  public void setEquippable(EquipmentSlot slot) {
    this.equippable = new Equippable(slot);
  }

  public void setPlayable(String songKey) {
    this.playable = new JukeboxPlayable(songKey);
  }

  public void setFertilizer(int growthSpeedup, List<String> usableOn, int cooldown) {this.fertilizer = new Fertilizer(growthSpeedup, usableOn,cooldown);}

  public void setSkullValue(String value){this.skullValue = new SkullValue(value);}
}

