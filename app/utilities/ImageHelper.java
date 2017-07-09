package utilities;

public class ImageHelper {

  private static final String IMAGE_DIR = "images";

  /**
   * Upload an image.
   *
   * @param file  The name of the image.
   * @param niche The niche the image belongs to.
   * @return The filename of the saved image.
   */
  public static String imagify(String file, String niche) {
    if (file.equals("")) {
      return null;
    } else {
      return file;
    }
  }

  /**
   * Get a image's URL.
   *
   * @param fileName  The name of the file.
   * @param niche     The niche the image belongs to.
   * @return A relative image path.
   */
  public static String getLink(String fileName, String niche) {
    if (fileName == null) {
      return null;
    } else {
      return String.format("%s/%s/%s", IMAGE_DIR, niche, fileName);
    }
  }
}
