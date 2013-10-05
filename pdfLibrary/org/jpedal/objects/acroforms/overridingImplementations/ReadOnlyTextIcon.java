package org.jpedal.objects.acroforms.overridingImplementations;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.FormStream;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.raw.XObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class ReadOnlyTextIcon extends CustomImageIcon
  implements Icon, SwingConstants
{
  private boolean currentlyPrinting = false;
  private int printMultiplier = 1;
  private int alignment = -1;
  private static final long serialVersionUID = 8946195842453749725L;
  private BufferedImage rootImage = null;
  private BufferedImage finalImage = null;
  private PdfObject fakeObj = null;
  private boolean textChanged = false;
  private String preFontStream = "";
  private String betweenFontAndTextStream = "";
  private String afterTextStream = "";
  private String text = "";
  private String fontName = "";
  private String fontSize = "";
  private String fontCommand = "";
  private String fullCommandString;
  private PdfObjectReader currentpdffile = null;
  private int subtype = -1;
  private PdfObject resources;

  public ReadOnlyTextIcon(int paramInt, PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    super(paramInt);
    this.currentpdffile = paramPdfObjectReader;
    this.resources = paramPdfObject;
  }

  public Image getImage()
  {
    checkAndCreateimage();
    BufferedImage localBufferedImage = this.finalImage;
    return localBufferedImage;
  }

  public BufferedImage drawToBufferedImage()
  {
    BufferedImage localBufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), 2);
    Graphics localGraphics = localBufferedImage.getGraphics();
    paintIcon(null, localGraphics, 0, 0);
    localGraphics.dispose();
    return localBufferedImage;
  }

  public synchronized void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = (BufferedImage)getImage();
    if (localBufferedImage == null)
      return;
    if ((paramComponent != null) && (paramComponent.isEnabled()))
      paramGraphics.setColor(paramComponent.getBackground());
    else
      paramGraphics.setColor(Color.gray);
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    if ((this.iconWidth > 0) && (this.iconHeight > 0))
    {
      int i = this.iconWidth;
      int j = this.iconHeight;
      if ((this.displaySingle) && ((this.iconRotation == 270) || (this.iconRotation == 90)))
      {
        i = this.iconHeight;
        j = this.iconWidth;
      }
      if (this.currentpdffile != null)
      {
        float f1 = i / localBufferedImage.getWidth(null);
        float f2 = j / localBufferedImage.getHeight(null);
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
      int k = 0;
      int m = 0;
      if (this.currentpdffile != null)
        if ((this.displaySingle) && ((this.iconRotation == 270) || (this.iconRotation == 90)))
        {
          k = (this.iconHeight - i) / 2;
          m = (this.iconWidth - j) / 2;
        }
        else
        {
          k = (this.iconWidth - i) / 2;
          m = (this.iconHeight - j) / 2;
        }
      if (this.alignment == 2)
        k = 0;
      int n;
      if (this.displaySingle)
        n = validateRotationValue(this.pageRotate - this.iconRotation);
      else
        n = this.pageRotate;
      if (n == 270)
      {
        localGraphics2D.rotate(-1.570796326794897D);
        localGraphics2D.translate(-i, 0);
        localGraphics2D.drawImage(localBufferedImage, -k, m, i, j, null);
      }
      else if (n == 90)
      {
        localGraphics2D.rotate(1.570796326794897D);
        localGraphics2D.translate(0, -j);
        localGraphics2D.drawImage(localBufferedImage, k, -m, i, j, null);
      }
      else if (n == 180)
      {
        localGraphics2D.rotate(3.141592653589793D);
        localGraphics2D.translate(-i, -j);
        localGraphics2D.drawImage(localBufferedImage, -k, -m, i, j, null);
      }
      else
      {
        localGraphics2D.drawImage(localBufferedImage, k, m, i, j, null);
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
    if ((this.textChanged) || (this.rootImage == null) || (i > this.rootImage.getWidth(null)) || (j > this.rootImage.getHeight(null)) || (i < this.rootImage.getWidth(null) / MAXSCALEFACTOR) || (j < this.rootImage.getHeight(null) / MAXSCALEFACTOR))
    {
      this.rootImage = FormStream.decode(this.currentpdffile, this.fakeObj, this.subtype, i, j, 0, 1.0F);
      this.finalImage = FormStream.rotate(this.rootImage, this.iconRotation);
      this.textChanged = false;
    }
  }

  public void setText(String paramString)
  {
    if (paramString == null)
      paramString = "";
    if (paramString.equals(this.text))
      return;
    this.textChanged = true;
    this.text = paramString;
    PdfObject localPdfObject = new PdfObject("1 10 X");
    while (true)
    {
      localPdfObject.setDecodedStream(StringUtils.toBytes(this.afterTextStream));
      String str = FormStream.decipherTextFromAP(this.currentpdffile, localPdfObject);
      if ((str != null) && (this.text.contains(str)))
      {
        int i = this.afterTextStream.indexOf(" Tj", this.afterTextStream.indexOf(str)) + 3;
        this.afterTextStream = this.afterTextStream.substring(i);
        if (this.afterTextStream.isEmpty())
          break;
      }
    }
    try
    {
      this.fullCommandString = (this.preFontStream + this.fontName + this.fontSize + this.fontCommand + this.betweenFontAndTextStream + '(' + this.text + ")Tj " + this.afterTextStream);
      this.fakeObj.setDecodedStream(this.fullCommandString.getBytes("Cp1252"));
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
  }

  public boolean decipherAppObject(FormObject paramFormObject)
  {
    String str1 = "";
    PdfObject localPdfObject = paramFormObject.getDictionary(4384).getDictionary(30);
    int i2;
    if (localPdfObject != null)
    {
      localObject1 = localPdfObject.getDecodedStream();
      if (localObject1 != null)
      {
        int i = -1;
        int j = -1;
        n = -1;
        i1 = localObject1.length;
        for (i2 = 0; i2 < i1 - 1; i2++)
          if (((char)localObject1[i2] == 'T') && ((char)localObject1[(i2 + 1)] == 'f') && ((i2 + 2 >= i1) || (localObject1[(i2 + 2)] == 10) || (localObject1[(i2 + 2)] == 13) || (localObject1[(i2 + 2)] == 32)))
          {
            j = i2 + 2;
            break;
          }
        if (j == -1)
        {
          i = 0;
          j = 0;
        }
        else
        {
          for (i2 = j - 3; i2 > i; i2--)
            if (localObject1[i2] == 47)
            {
              i = i2;
              break;
            }
        }
        for (i2 = j; i2 < i1 - 1; i2++)
          if (((char)localObject1[i2] == 'T') && ((char)localObject1[(i2 + 1)] == 'j') && ((i2 + 2 >= i1) || (localObject1[(i2 + 2)] == 10) || (localObject1[(i2 + 2)] == 13) || (localObject1[(i2 + 2)] == 32)))
          {
            n = i2 + 2;
            break;
          }
        if (n == -1)
        {
          m = j;
          n = j;
        }
        else
        {
          m = j;
          i2 = 0;
          int i3 = 0;
          for (int i4 = n - 3; i4 > m; i4--)
            if ((localObject1[i4] == 32) || (localObject1[i4] == 10) || (localObject1[i4] == 13))
            {
              if ((i3 != 0) && (i2 == 0))
              {
                m = i4 + 1;
                break;
              }
            }
            else if (localObject1[i4] == 41)
            {
              i2++;
            }
            else if (localObject1[i4] == 40)
            {
              i2--;
              if ((i2 == 0) && (i3 != 0))
              {
                m = i4;
                break;
              }
            }
            else
            {
              i3 = 1;
            }
        }
        for (i2 = j; i2 < m; i2++)
          if ((localObject1[i2] != 32) && (localObject1[i2] != 10) && (localObject1[i2] != 13) && ((localObject1[i2] <= 47) || (localObject1[i2] >= 58)))
          {
            if ((localObject1[i2] == 103) && (i2 + 1 < m) && ((localObject1[(i2 + 1)] == 32) || (localObject1[(i2 + 1)] == 10) || (localObject1[(i2 + 1)] == 13)))
            {
              j = i2 + 1;
              break;
            }
            if ((localObject1[i2] != 114) || (i2 + 2 >= m) || (localObject1[(i2 + 1)] != 103) || ((localObject1[(i2 + 2)] != 32) && (localObject1[(i2 + 2)] != 10) && (localObject1[(i2 + 2)] != 13)))
              break;
            j = i2 + 2;
            break;
          }
        if (n != j)
        {
          if (j == 0)
          {
            this.preFontStream = new String((byte[])localObject1, 0, m);
            this.betweenFontAndTextStream = " ";
          }
          else
          {
            this.preFontStream = new String((byte[])localObject1, 0, i);
            str1 = new String((byte[])localObject1, i, j - i);
            this.betweenFontAndTextStream = new String((byte[])localObject1, j, m - j);
          }
          this.text = new String((byte[])localObject1, m, n - 3 - m);
          this.afterTextStream = new String((byte[])localObject1, n, localObject1.length - n);
        }
        else if (j == 0)
        {
          this.preFontStream = new String((byte[])localObject1);
        }
        else
        {
          this.preFontStream = new String((byte[])localObject1, 0, i);
          str1 = new String((byte[])localObject1, i, j - i);
          this.betweenFontAndTextStream = new String((byte[])localObject1, j, localObject1.length - j);
        }
      }
    }
    Object localObject1 = paramFormObject.getTextStreamValue(5137);
    this.fakeObj = new XObject(paramFormObject.getObjectRefAsString());
    if ((localObject1 == null) || (((String)localObject1).isEmpty()))
    {
      if (!str1.isEmpty())
      {
        paramFormObject.setTextStreamValue(5137, StringUtils.toBytes(str1));
        FormStream.decodeFontCommandObj(str1, paramFormObject);
      }
      return false;
    }
    if (str1.isEmpty())
    {
      str1 = ((String)localObject1).trim();
    }
    else
    {
      localObject2 = ((String)localObject1).substring(0, ((String)localObject1).indexOf(32));
      String str2 = str1.substring(str1.indexOf(32), str1.length());
      str1 = (String)localObject2 + str2;
      str1.trim();
    }
    if (this.resources != null)
      this.fakeObj.setDictionary(2004251818, this.resources);
    Object localObject2 = paramFormObject.getBoundingRectangle();
    if (localObject2 != null)
      this.fakeObj.setFloatArray(303185736, new float[] { ((Rectangle)localObject2).width, 0.0F, 0.0F, ((Rectangle)localObject2).height, 0.0F, 0.0F });
    this.subtype = -1;
    if ((this.preFontStream.isEmpty()) || (!this.preFontStream.contains("BT")))
    {
      this.preFontStream = "BT 0 0 0 RG 1 TFS ";
      this.betweenFontAndTextStream = " 1 0 0 1 0 0 Tm ";
      this.afterTextStream = "";
    }
    int k = str1.indexOf(32);
    int m = -1;
    int n = 0;
    for (int i1 = k; i1 < str1.length(); i1++)
    {
      i2 = str1.charAt(i1);
      if ((i2 == 32) || (i2 == 10) || (i2 == 13))
      {
        if (n != 0)
        {
          m = i1;
          break;
        }
      }
      else
        n = 1;
    }
    float f = 12.0F;
    if (m != -1)
    {
      this.fontName = str1.substring(0, k);
      this.fontCommand = str1.substring(m);
      f = Float.parseFloat(str1.substring(k, m));
    }
    if (this.fontName.isEmpty())
    {
      Font localFont = paramFormObject.getTextFont();
      this.fontName = ('/' + localFont.getFontName());
      this.fontCommand = "Tf ";
    }
    if ((f == 0.0F) || (f == -1.0F))
      f = GUIData.calculateFontSize(((Rectangle)localObject2).height, ((Rectangle)localObject2).width, false, this.text);
    this.fontSize = (" " + f + ' ');
    return true;
  }

  public void setAlignment(int paramInt)
  {
    this.alignment = paramInt;
  }

  public void setPrinting(boolean paramBoolean, int paramInt)
  {
    this.currentlyPrinting = paramBoolean;
    this.printMultiplier = paramInt;
    checkAndCreateimage();
  }

  public PdfObject getFakeObject()
  {
    return this.fakeObj;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.overridingImplementations.ReadOnlyTextIcon
 * JD-Core Version:    0.6.2
 */