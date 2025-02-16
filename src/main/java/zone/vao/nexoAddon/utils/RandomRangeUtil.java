package zone.vao.nexoAddon.utils;

import java.util.Random;

public class RandomRangeUtil {

    private static final Random random = new Random();

    public static double parseAndGetRandomValue(String input) {
        if (input.contains("-")) {
            String[] parts = input.split("-");
            if (parts.length == 3 && parts[0].isEmpty()) {
                try {
                    double min = Double.parseDouble("-" + parts[1]);
                    double max = Double.parseDouble("-" + parts[2]);
                    return min + (max - min) * random.nextDouble();
                } catch (NumberFormatException e) {
                    return 0.5;
                }
            } else if (parts.length == 3) {
                try {
                    double min = Double.parseDouble(parts[0]);
                    double max = Double.parseDouble("-" + parts[2]);
                    return min + (max - min) * random.nextDouble();
                } catch (NumberFormatException e) {
                    return 0.5;
                }
            } else if (parts.length == 2) {
                try {
                    double min = Double.parseDouble(parts[0]);
                    double max = Double.parseDouble(parts[1]);
                    return min + (max - min) * random.nextDouble();
                } catch (NumberFormatException e) {
                    return 0.5;
                }
            }
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0.5;
        }
    }
}