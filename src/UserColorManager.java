import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserColorManager {
  private final Map<String, Color> userColors = new HashMap<>();
  private final Color[] palette = {
      Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE,
      new Color(0x8A2BE2), // BlueViolet
      new Color(0x20B2AA), // LightSeaGreen
      new Color(0xDA70D6),
  };
  private final Random random = new Random();

  public Color getColorForUser(String username) {
    return userColors.computeIfAbsent(username, k -> palette[random.nextInt(palette.length)]);
  }
}
