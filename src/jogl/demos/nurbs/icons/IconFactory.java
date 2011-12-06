package demos.nurbs.icons;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

import com.jogamp.opengl.util.StreamUtil;

public class IconFactory {
  private IconFactory() {}

  public static ImageIcon getIcon(String resourceName) {
    try {
      InputStream input = IconFactory.class.getClassLoader().getResourceAsStream(resourceName);
      byte[] data = StreamUtil.readAll2Array(input);
      return new ImageIcon(data, resourceName);
    } catch (IOException e) {
      return new ImageIcon();
    }
  }
}
