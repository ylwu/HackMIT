package org.jpedal.examples.viewer.gui.swing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.jpedal.examples.viewer.gui.generic.GUIButton;

public class SwingButton extends JButton
  implements GUIButton
{
  private int ID;

  public SwingButton()
  {
  }

  public SwingButton(String paramString)
  {
    super(paramString);
  }

  public void init(URL paramURL, int paramInt, String paramString)
  {
    this.ID = paramInt;
    setToolTipText(paramString);
    setBorderPainted(false);
    if (paramURL != null)
    {
      ImageIcon localImageIcon = new ImageIcon(paramURL);
      setIcon(localImageIcon);
      createPressedLook(this, localImageIcon);
    }
    setFocusable(false);
  }

  private static void createPressedLook(AbstractButton paramAbstractButton, ImageIcon paramImageIcon)
  {
    BufferedImage localBufferedImage = new BufferedImage(paramImageIcon.getIconWidth() + 2, paramImageIcon.getIconHeight() + 2, 2);
    Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
    localGraphics2D.drawImage(paramImageIcon.getImage(), 1, 1, null);
    localGraphics2D.dispose();
    ImageIcon localImageIcon = new ImageIcon(localBufferedImage);
    paramAbstractButton.setPressedIcon(localImageIcon);
  }

  public void setIcon(ImageIcon paramImageIcon)
  {
    super.setIcon(paramImageIcon);
  }

  public boolean isEnabled()
  {
    return super.isEnabled();
  }

  public int getID()
  {
    return this.ID;
  }

  public void setName(String paramString)
  {
    super.setName(paramString);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingButton
 * JD-Core Version:    0.6.2
 */