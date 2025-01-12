package zone.vao.nexoAddon.classes;

import lombok.Getter;
import org.bukkit.Particle;
import zone.vao.nexoAddon.classes.mechanic.*;

@Getter
public class Mechanics {

  private final String id;
  private Repair repair;
  private BigMining bigMining;
  private BedrockBreak bedrockBreak;
  private Aura aura;
  private SpawnerBreak spawnerBreak;

  public Mechanics(String id) {
    this.id = id;
  }

  public void setRepair(double ration, int fixedAmount) {
    this.repair = new Repair(ration, fixedAmount);
  }

  public void setBigMining(int radius, int depth, boolean switchable) {
    this.bigMining = new BigMining(radius, depth, switchable);
  }

  public void setBedrockBreak(int hardness, double probability, int durabilityCost, boolean disableOnFirstLayer) {
    this.bedrockBreak = new BedrockBreak(hardness, probability, durabilityCost, disableOnFirstLayer);
  }

  public void setAura(Particle particle, String type, String formula) {
    this.aura = new Aura(particle, type, formula);
  }

  public void setSpawnerBreak(double probability) {
    this.spawnerBreak = new SpawnerBreak(probability);
  }
}
