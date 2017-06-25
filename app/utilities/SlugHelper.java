package utilities;

public class SlugHelper {

  /**
   * @param name The name you want a slug for.
   * @return A valid slug.
   */
  public static String slugify(String name) {
    return name.replaceAll("\\s+", "-").toLowerCase();
  }
}
