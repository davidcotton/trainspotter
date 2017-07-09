package utilities;

public class TextHelper {

  /**
   * Truncate text.
   *
   * @param text   The text to truncate.
   * @param length The maximum number of characters.
   * @return A truncated text block.
   */
  public static String truncate(String text, int length) {
    if (text == null) {
      return null;
    } else if (text.length() < length) {
      return text;
    } else {
      return text.substring(0, length) + "...";
    }
  }
}
