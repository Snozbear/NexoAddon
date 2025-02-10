package zone.vao.nexoAddon.classes.mechanic;

public record BottledExp(Double ratio, int cost) {

  private static int calculateExperienceForLevel(int level) {
    if (level <= 15) {
      return level * level + 6 * level;
    } else if (level <= 30) {
      return (int) (2.5 * level * level - 40.5 * level + 360);
    } else {
      return (int) (4.5 * level * level - 162.5 * level + 2220);
    }
  }

  public static int convertXpToBottles(int level, float xp, double ratio) {
    int baseExp = calculateExperienceForLevel(level);
    float totalExp = xp + baseExp;
    return (int) Math.ceil(totalExp * ratio / 10.0f);
  }
}
