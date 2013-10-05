package org.jpedal.objects.acroforms.overridingImplementations;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.FormStream;
import org.jpedal.objects.raw.PdfObject;

public class FixImageIcon extends CustomImageIcon
  implements Icon, SwingConstants
{
  private static final long serialVersionUID = 8946195842453749725L;
  private boolean currentlyPrinting = false;
  private int printMultiplier = 1;
  private int selected = -1;
  private static final int UNSELECTEDICON = 0;
  private static final int SELECTEDICON = 1;
  private BufferedImage rootImageSelected = null;
  private BufferedImage rootImageUnselected = null;
  private BufferedImage imageSelected = null;
  private BufferedImage imageUnselected = null;
  private PdfObject selObj = null;
  private PdfObject unSelObj = null;
  private PdfObjectReader currentpdffile = null;
  private int subtype;
  private int offsetImage;

  public FixImageIcon(BufferedImage paramBufferedImage, int paramInt)
  {
    super(paramInt);
    if (paramBufferedImage != null)
      this.imageSelected = paramBufferedImage;
    else
      this.imageSelected = FormObject.getOpaqueImage();
    this.selected = -1;
  }

  public FixImageIcon(PdfObject paramPdfObject, int paramInt1, PdfObjectReader paramPdfObjectReader, int paramInt2, int paramInt3)
  {
    super(paramInt1);
    this.selObj = paramPdfObject;
    this.selected = -1;
    this.currentpdffile = paramPdfObjectReader;
    this.subtype = paramInt2;
    this.offsetImage = paramInt3;
  }

  public FixImageIcon(PdfObject paramPdfObject1, PdfObject paramPdfObject2, int paramInt1, int paramInt2, PdfObjectReader paramPdfObjectReader, int paramInt3, int paramInt4)
  {
    super(paramInt1);
    this.selObj = paramPdfObject1;
    this.unSelObj = paramPdfObject2;
    this.selected = paramInt2;
    this.currentpdffile = paramPdfObjectReader;
    this.subtype = paramInt3;
    this.offsetImage = paramInt4;
  }

  public Image getImage()
  {
    checkAndCreateimage();
    BufferedImage localBufferedImage;
    switch (this.selected)
    {
    case 0:
      localBufferedImage = this.imageUnselected;
      break;
    default:
      localBufferedImage = this.imageSelected;
    }
    return localBufferedImage;
  }

  public synchronized void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = (BufferedImage)getImage();
    if (localBufferedImage == null)
      return;
    if (paramComponent.isEnabled())
      paramGraphics.setColor(paramComponent.getBackground());
    else
      paramGraphics.setColor(Color.gray);
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    if ((this.iconWidth > 0) && (this.iconHeight > 0))
    {
      int i = this.iconWidth;
      int j = this.iconHeight;
      int k = 0;
      if ((this.iconRotation == 270) || (this.iconRotation == 90))
      {
        k = 1;
        i = this.iconHeight;
        j = this.iconWidth;
      }
      if (this.currentpdffile != null)
      {
        float f1 = i / localBufferedImage.getWidth(null);
        float f2 = j / localBufferedImage.getHeight(null);
        float f3 = f1 - f2;
        int i2 = (int)f3;
        if (i2 != 0)
        {
          k = k == 0 ? 1 : 0;
          int i3 = i;
          i = j;
          j = i3;
          f1 = i / localBufferedImage.getWidth(null);
          f2 = j / localBufferedImage.getHeight(null);
        }
        if (f1 < f2)
        {
          i = (int)(f1 * localBufferedImage.getWidth(null));
          j = (int)(f1 * localBufferedImage.getHeight(null));
        }
        else
        {
          i = (int)(f2 * localBufferedImage.getWidth(null));
          j = (int)(f2 * localBufferedImage.getHeight(null));
        }
      }
      int m = 0;
      int n = 0;
      if (this.currentpdffile != null)
        if (k != 0)
        {
          m = (this.iconHeight - i) / 2;
          n = (this.iconWidth - j) / 2;
        }
        else
        {
          m = (this.iconWidth - i) / 2;
          n = (this.iconHeight - j) / 2;
        }
      int i1;
      if (this.displaySingle)
        i1 = validateRotationValue(this.pageRotate - this.iconRotation);
      else
        i1 = this.pageRotate;
      if (i1 == 270)
      {
        localGraphics2D.rotate(-1.570796326794897D);
        localGraphics2D.translate(-i, 0);
        localGraphics2D.drawImage(localBufferedImage, -m, n, i, j, null);
      }
      else if (i1 == 90)
      {
        localGraphics2D.rotate(1.570796326794897D);
        localGraphics2D.translate(0, -j);
        localGraphics2D.drawImage(localBufferedImage, m, -n, i, j, null);
      }
      else if (i1 == 180)
      {
        localGraphics2D.rotate(3.141592653589793D);
        localGraphics2D.translate(-i, -j);
        localGraphics2D.drawImage(localBufferedImage, -m, -n, i, j, null);
      }
      else
      {
        localGraphics2D.drawImage(localBufferedImage, m, n, i, j, null);
      }
    }
    else
    {
      localGraphics2D.drawImage(localBufferedImage, 0, 0, null);
    }
    localGraphics2D.translate(-paramInt1, -paramInt2);
  }

  private void checkAndCreateimage()
  {
    if (this.currentpdffile == null)
      return;
    int i = this.iconWidth;
    int j = this.iconHeight;
    if (this.currentlyPrinting)
    {
      i = this.iconWidth * this.printMultiplier;
      j = this.iconHeight * this.printMultiplier;
    }
    switch (this.selected)
    {
    case 0:
      if ((this.rootImageUnselected == null) || (i > this.rootImageUnselected.getWidth(null)) || (j > this.rootImageUnselected.getHeight(null)) || (i < this.rootImageUnselected.getWidth(null) / MAXSCALEFACTOR) || (j < this.rootImageUnselected.getHeight(null) / MAXSCALEFACTOR))
      {
        this.rootImageUnselected = FormStream.decode(this.currentpdffile, this.unSelObj, this.subtype, i, j, this.offsetImage, 1.0F);
        this.imageUnselected = FormStream.rotate(this.rootImageUnselected, this.iconRotation);
      }
      break;
    default:
      if ((this.rootImageSelected == null) || (i > this.rootImageSelected.getWidth(null)) || (j > this.rootImageSelected.getHeight(null)) || (i < this.rootImageSelected.getWidth(null) / MAXSCALEFACTOR) || (j < this.rootImageSelected.getHeight(null) / MAXSCALEFACTOR))
      {
        this.rootImageSelected = FormStream.decode(this.currentpdffile, this.selObj, this.subtype, i, j, this.offsetImage, 1.0F);
        this.imageSelected = FormStream.rotate(this.rootImageSelected, this.iconRotation);
      }
      break;
    }
  }

  public void swapImage(boolean paramBoolean)
  {
    if (this.selected == -1)
      return;
    if (paramBoolean)
      this.selected = 1;
    else
      this.selected = 0;
  }

  public void setPrinting(boolean paramBoolean, int paramInt)
  {
    this.currentlyPrinting = paramBoolean;
    this.printMultiplier = paramInt;
    checkAndCreateimage();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.overridingImplementations.FixImageIcon
 * JD-Core Version:    0.6.2
 */