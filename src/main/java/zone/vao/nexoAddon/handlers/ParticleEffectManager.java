package zone.vao.nexoAddon.handlers;

import com.nexomc.nexo.api.NexoItems;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.mechanic.Aura;

public class ParticleEffectManager {

  private final NexoAddon plugin;
  private final double MATH_PI = Math.PI;

  public ParticleEffectManager(NexoAddon plugin) {
    this.plugin = plugin;
  }

  public void startAuraEffectTask() {
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        applyAuraEffect(player);
      }
    }, 0L, 5L);
  }

  private Aura getAuraFromTool(Player player) {
    ItemStack heldItem = player.getInventory().getItemInMainHand();
    if (heldItem == null || heldItem.getType().isAir()) return null;

    String toolId = NexoItems.idFromItem(heldItem);
    if (toolId == null) return null;

    if (NexoAddon.getInstance().getMechanics().get(toolId) == null) return null;
    return NexoAddon.getInstance().getMechanics().get(toolId).getAura();
  }

  private void applyAuraEffect(Player player) {
    Aura aura = getAuraFromTool(player);
    if (aura == null) return;

    String formula = aura.getFormula();
    Particle particle = aura.getParticle();

    if ("custom".equalsIgnoreCase(aura.getType())) {
      spawnCustomParticles(player, particle, formula);
    } else if ("simple".equalsIgnoreCase(aura.getType())) {
      spawnSimpleParticles(player, particle);
    } else if ("ring".equalsIgnoreCase(aura.getType())) {
      spawnRingParticles(player, particle);
    } else if ("helix".equalsIgnoreCase(aura.getType())) {
      spawnHelixParticles(player, particle);
    } else if ("heart".equalsIgnoreCase(aura.getType())) {
      spawnHeartParticles(player, particle);
    }
  }

  private void spawnCustomParticles(Player player, Particle particle, String formula) {
    int particlesCount = 20;

    String[] components = extractFormulaComponents(formula);
    if (components.length != 3) {
      throw new IllegalArgumentException("Custom formula must define x, y, and z components, separated by commas.");
    }
    double x = player.getLocation().getX();
    double y = player.getLocation().getY();
    double z = player.getLocation().getZ();
    float yaw = player.getLocation().getYaw();
    float pitch = player.getLocation().getPitch();
    double angle = 0.0;
    double angle2 = -Math.PI / 2;
    String xFormula = components[0];
    String yFormula = components[1];
    String zFormula = components[2];

    for (int i = 0; i < particlesCount; i++) {
      for (int j = 0; j < particlesCount; j++) {
        double posX = evaluateFormula(xFormula.replace("Math_PI", Double.toString(MATH_PI)).replace("x", Double.toString(x)).replace("yaw", Float.toString(yaw)).replace("y", Double.toString(y)).replace("z", Double.toString(z)).replace("pitch", Float.toString(pitch)).replace("angle2", Double.toString(angle2)).replace("angle", Double.toString(angle)));

        double posY = evaluateFormula(yFormula.replace("Math_PI", Double.toString(MATH_PI)).replace("x", Double.toString(x)).replace("yaw", Float.toString(yaw)).replace("y", Double.toString(y)).replace("z", Double.toString(z)).replace("pitch", Float.toString(pitch)).replace("angle2", Double.toString(angle2)).replace("angle", Double.toString(angle)));

        double posZ = evaluateFormula(zFormula.replace("Math_PI", Double.toString(MATH_PI)).replace("x", Double.toString(x)).replace("yaw", Float.toString(yaw)).replace("y", Double.toString(y)).replace("z", Double.toString(z)).replace("pitch", Float.toString(pitch)).replace("angle2", Double.toString(angle2)).replace("angle", Double.toString(angle)));

            player.getWorld().spawnParticle(particle, posX, posY, posZ, 1, 0, 0, 0, 0);

        angle += Math.PI * 2 / particlesCount;
      }
      angle2 += Math.PI / particlesCount;
    }
  }

  private void spawnHeartParticles(Player player, Particle particle) {
    int particlesCount = 100;
    double angle = 0.0;

    double x = player.getLocation().getX();
    double y = player.getLocation().getY();
    double z = player.getLocation().getZ();
    float yaw = player.getLocation().getYaw();

    double yawRadians = Math.toRadians(yaw);

    for (int i = 0; i < particlesCount; i++) {
      double heartX = evaluateFormula("(4*pow(sin(angle),3))".replace("angle", Double.toString(angle)));
      double heartY = 0;
      double heartZ = evaluateFormula("(3*cos(angle)-1.25*cos(2*angle)-0.75*cos(3*angle)-0.25*cos(4*angle))".replace("angle", Double.toString(angle)));

      double rotatedX = x + heartX * Math.cos(yawRadians) - heartZ * Math.sin(yawRadians);
      double rotatedZ = z + heartX * Math.sin(yawRadians) + heartZ * Math.cos(yawRadians);

      player.getWorld().spawnParticle(particle, rotatedX, y, rotatedZ, 1, 0, 0, 0, 0);

      angle += 0.1;
    }
  }


  private void spawnSimpleParticles(Player player, Particle particle) {
    player.getWorld().spawnParticle(particle, player.getLocation(), 10, 0.5, 0.5, 0.5, 0.01);
  }

  private void spawnRingParticles(Player player, Particle particle) {
    double radius = 2.0;
    int points = 20;

    for (int i = 0; i < points; i++) {
      double angle = 2 * Math.PI * i / points;
      double x = player.getLocation().getX() + radius * Math.cos(angle);
      double z = player.getLocation().getZ() + radius * Math.sin(angle);
      double y = player.getLocation().getY();

      player.getWorld().spawnParticle(particle, x, y, z, 0, 0, 0, 0);
    }
  }


  private void spawnHelixParticles(Player player, Particle particle) {
    double radius = 1.5;
    double height = 3.0;
    double turns = 2.0;
    int points = 50;

    for (int i = 0; i < points; i++) {
      double angle = 2 * Math.PI * turns * i / points;
      double x = player.getLocation().getX() + radius * Math.cos(angle);
      double z = player.getLocation().getZ() + radius * Math.sin(angle);
      double y = player.getLocation().getY() + height * i / points;

      player.getWorld().spawnParticle(particle, x, y, z, 1, 0, 0, 0, 0);
    }
  }

  private double evaluateFormula(String formula) {
    try {
      return new ExpressionBuilder(formula).build().evaluate();
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  private String[] extractFormulaComponents(String formula) {
    String[] components = new String[3];
    int firstComma = -1;
    int secondComma = -1;
    int openParenthesesCount = 0;

    for (int i = 0; i < formula.length(); i++) {
      char c = formula.charAt(i);
      if (c == '(') openParenthesesCount++;
      else if (c == ')') openParenthesesCount--;
      else if (c == ',' && openParenthesesCount == 0) {
        if (firstComma == -1) {
          firstComma = i;
        } else if (secondComma == -1) {
          secondComma = i;
        }
      }
    }

    if (firstComma != -1 && secondComma != -1) {
      components[0] = formula.substring(0, firstComma);
      components[1] = formula.substring(firstComma + 1, secondComma);
      components[2] = formula.substring(secondComma + 1);
    }

    return components;
  }
}
