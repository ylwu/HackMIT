package org.jpedal.objects.acroforms.overridingImplementations;

import java.awt.Image;
import javax.swing.ImageIcon;

public class CustomImageIcon extends ImageIcon
{
  private static final long serialVersionUID = 5003778613900628453L;
  protected static float MAXSCALEFACTOR = 1.5F;
  protected int iconWidth = -1;
  protected int iconHeight = -1;
  protected int iconRotation = 0;
  protected int iconOpp = 180;
  protected int pageRotate = 0;
  protected boolean displaySingle = false;

  public static void setMaxScaleFactor(float paramFloat)
  {
    MAXSCALEFACTOR = paramFloat;
  }

  public CustomImageIcon(int paramInt)
  {
    this.iconRotation = paramInt;
    this.iconOpp = (this.iconRotation - 180);
    if (this.iconOpp < 0)
      this.iconOpp += 360;
  }

  public void setAttributes(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    int i = validateRotationValue(paramInt3 - this.iconRotation);
    this.pageRotate = paramInt3;
    if ((i == this.iconRotation) || (i == this.iconOpp))
    {
      this.iconWidth = paramInt1;
      this.iconHeight = paramInt2;
    }
    else
    {
      this.iconWidth = paramInt2;
      this.iconHeight = paramInt1;
    }
    this.displaySingle = paramBoolean;
  }

  protected static int validateRotationValue(int paramInt)
  {
    paramInt %= 360;
    if (paramInt < 0)
      paramInt += 360;
    return paramInt;
  }

  public int getIconHeight()
  {
    if (this.iconHeight == -1)
    {
      Image localImage = getImage();
      if (localImage == null)
        return -1;
      return localImage.getHeight(null);
    }
    return this.iconHeight;
  }

  public int getIconWidth()
  {
    if (this.iconWidth == -1)
    {
      Image localImage = getImage();
      if (localImage == null)
        return -1;
      return localImage.getWidth(null);
    }
    return this.iconWidth;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.overridingImplementations.CustomImageIcon
 * JD-Core Version:    0.6.2
 */