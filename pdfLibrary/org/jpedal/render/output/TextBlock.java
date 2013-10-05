package org.jpedal.render.output;

import java.awt.Rectangle;
import org.jpedal.parser.PdfStreamDecoder;

public class TextBlock
{
  private float rotatedXCoord;
  private float rawXCoord;
  private float rotatedYCoord;
  private float rawYCoord;
  private float yAdjust;
  private float lastXCoord;
  private float stringWidth;
  private float lastGlyfWidth;
  private String lastGlyf;
  private Rectangle cropBox;
  private float fontSize;
  private String font = "";
  private String realFont = "";
  private String weight = "normal";
  private String style = "normal";
  private float spaceWidth;
  private float charSpacing;
  private float wordSpacing;
  private float kerning = 0.0F;
  private int numSpaces = 0;
  private int numChars = 0;
  private String text;
  private int textColRGB;
  private float[][] matrix;
  private float[][] Trm;
  private float lastYUsed;
  private float lastX = 0.0F;
  private int maxSpacesAllowed = 3;
  boolean convertSpacesTonbsp;
  int pageRotation;

  public TextBlock()
  {
    this.text = "";
    this.textColRGB = -1;
    this.stringWidth = 0.0F;
  }

  public TextBlock(String paramString, float paramFloat1, FontMapper paramFontMapper, float[][] paramArrayOfFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, float paramFloat5, Rectangle paramRectangle, float[][] paramArrayOfFloat2, float paramFloat6, float paramFloat7, int paramInt2, float paramFloat8, float paramFloat9)
  {
    this.text = paramString;
    this.matrix = paramArrayOfFloat1;
    this.lastXCoord = (paramFloat6 + paramFloat4);
    this.rawXCoord = paramFloat2;
    this.rotatedXCoord = paramFloat6;
    this.rawYCoord = paramFloat3;
    this.rotatedYCoord = paramFloat7;
    this.stringWidth = paramFloat4;
    this.lastGlyfWidth = paramFloat4;
    this.lastGlyf = paramString;
    this.fontSize = paramFloat1;
    this.font = paramFontMapper.getFont(false);
    this.realFont = paramFontMapper.getFont(true);
    this.weight = paramFontMapper.getWeight();
    this.style = paramFontMapper.getStyle();
    this.textColRGB = paramInt1;
    this.spaceWidth = paramFloat5;
    this.cropBox = paramRectangle;
    this.Trm = paramArrayOfFloat2;
    this.pageRotation = paramInt2;
    this.charSpacing = paramFloat8;
    this.wordSpacing = paramFloat9;
    this.lastX = paramArrayOfFloat2[2][0];
    if (!paramRectangle.contains(paramFloat2 + paramFloat4, paramFloat3))
      this.text = "";
    else
      this.numChars += 1;
  }

  public boolean isEmpty()
  {
    return this.text.isEmpty();
  }

  public float getX()
  {
    return this.rawXCoord;
  }

  public float getY()
  {
    return this.rawYCoord + this.yAdjust;
  }

  public void adjustY(float paramFloat)
  {
    this.yAdjust = paramFloat;
  }

  public float getWidth()
  {
    if (this.text.endsWith(" "))
      return this.stringWidth - this.lastGlyfWidth;
    return this.stringWidth;
  }

  public float getFontSize()
  {
    if ((this.matrix != null) && (this.matrix[0][0] * this.matrix[0][1] != 0.0F))
    {
      float f1 = this.matrix[0][0];
      if (f1 < 0.0F)
        f1 = -f1;
      float f2 = this.matrix[0][1];
      if (f2 < 0.0F)
        f2 = -f2;
      float f3 = f1 + this.fontSize * f2;
      float f4 = this.fontSize * f1 + f2;
      float f5 = f3 > f4 ? f3 : f4;
      return Math.abs(f5) / this.fontSize;
    }
    return this.fontSize;
  }

  public String getFont()
  {
    return this.realFont;
  }

  public String getWeight()
  {
    return this.weight;
  }

  public int getColor()
  {
    return this.textColRGB;
  }

  private boolean compareTrm(float[][] paramArrayOfFloat)
  {
    if (this.matrix == null)
      return false;
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 2; j++)
        if (paramArrayOfFloat[j][i] != this.matrix[j][i])
          return false;
    return true;
  }

  public float getRotationAngle()
  {
    float f1 = Math.abs(this.matrix[0][0]) + Math.abs(this.matrix[0][1]);
    float f2 = (float)Math.acos(this.matrix[0][0] / f1);
    if ((f2 != 0.0F) && (this.matrix[1][0] < 0.0F) && (this.matrix[0][1] > 0.0F))
      f2 = -f2;
    return f2;
  }

  public int getRotationAngleInDegrees()
  {
    int i = (int)(getRotationAngle() * 180.0F / 3.141592653589793D);
    while (i < 0)
      i += 360;
    return i;
  }

  private void concat(String paramString)
  {
    this.text += paramString;
  }

  public String getOutputString(boolean paramBoolean)
  {
    String str = this.text;
    if ((paramBoolean) && (OutputDisplay.Helper != null))
    {
      str = OutputDisplay.Helper.tidyText(str);
      if (this.convertSpacesTonbsp)
        str = str.replaceAll(" ", "&nbsp;");
    }
    return str;
  }

  public float getCharSpacing()
  {
    if (this.numChars == 0)
      return 0.0F;
    return this.charSpacing / this.numChars;
  }

  public int getNumSpaces()
  {
    return this.numSpaces;
  }

  public boolean isSameFont(float paramFloat, FontMapper paramFontMapper, float[][] paramArrayOfFloat, int paramInt)
  {
    float f;
    if ((this.fontSize < 18.0F) || (paramFloat < 18.0F))
      f = 0.0F;
    else if (this.fontSize < paramFloat)
      f = this.fontSize / paramFloat;
    else
      f = paramFloat / this.fontSize;
    return ((compareTrm(paramArrayOfFloat)) && (this.fontSize == paramFloat)) || ((f > 0.8F) && (compareFontMapper(paramFontMapper)) && (this.textColRGB == paramInt));
  }

  private boolean compareFontMapper(FontMapper paramFontMapper)
  {
    return (paramFontMapper.getFont(false).equals(this.font)) && (paramFontMapper.getWeight().equals(this.weight));
  }

  public boolean appendText(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean1, boolean paramBoolean2, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    if (!this.cropBox.contains(paramFloat4, paramFloat5))
      return false;
    float f1 = getCharSpacing();
    float f2;
    if (f1 > paramFloat8)
      f2 = f1 - paramFloat8;
    else
      f2 = paramFloat8 - f1;
    int i = (f2 > Math.abs(f1)) || (f2 > Math.abs(paramFloat8)) ? 1 : 0;
    if (Math.abs(f2) < 10.0F)
      i = 0;
    if ((i != 0) || ((this.lastGlyf.equals(" ")) && (paramFloat6 > 5.0F)))
      return false;
    if (this.lastGlyf.equals(" "))
      paramFloat6 = 0.0F;
    float f3 = this.rotatedYCoord - paramFloat3;
    if (Math.abs(f3) < 0.01F)
    {
      f3 = 0.0F;
      this.rotatedYCoord = paramFloat3;
    }
    float f4 = paramFloat2 - this.lastXCoord - paramFloat8;
    if (getRotationAngleInDegrees() == 180)
      f4 -= paramFloat1;
    if ((!paramBoolean1) && (paramString.equals(" ")) && (f3 == 0.0F) && (f4 < -paramFloat1))
      return true;
    if ((!paramBoolean1) && (paramString.equals(" ")))
      return false;
    if ((f4 < 0.0F) && (f4 > -paramFloat1))
      f4 = 0.0F;
    if ((this.Trm[0][0] == 0.0F) && (this.Trm[1][1] == 0.0F) && (((this.Trm[0][1] > 0.0F) && (this.Trm[1][0] < 0.0F)) || ((this.Trm[0][1] < 0.0F) && (this.Trm[1][0] > 0.0F))))
    {
      if ((this.Trm[0][1] > 0.0F) && (this.Trm[1][0] < 0.0F) && (f3 < 0.0F))
        return false;
      float f5 = (this.lastYUsed - paramFloat3) / this.fontSize;
      if (f5 > 1.5D)
        return false;
      if (((this.Trm[0][0] != 0.0F) || (this.Trm[1][1] != 0.0F) || (this.lastX != paramFloat2)) && ((f3 != 0.0F) || (f4 < -1.5F)))
        return false;
    }
    else if ((f3 != 0.0F) || (f4 < -1.5F))
    {
      return false;
    }
    String str = "";
    int j = (int)(f4 / this.spaceWidth);
    float f6 = f4 % this.spaceWidth;
    f6 /= this.spaceWidth;
    if (f6 > PdfStreamDecoder.currentThreshold)
      j++;
    if (j == 0)
      paramFloat8 += paramFloat6;
    if (((paramBoolean2) || (j > this.maxSpacesAllowed)) && ((j > 2) || ((j > 0) && (this.matrix[0][0] == 0.0F) && (this.matrix[1][1] == 0.0F))))
      return false;
    if ((paramString.equals(" ")) || (j > 0))
    {
      this.numSpaces += 1;
      this.wordSpacing += paramFloat7;
    }
    this.kerning += paramFloat6;
    this.charSpacing += paramFloat8;
    this.numChars += 1;
    if ((j > 0) && (!paramString.equals(" ")) && (!this.lastGlyf.equals(" ")))
      str = str + " ";
    str = str + paramString;
    concat(str);
    if ((this.Trm[0][0] == 0.0F) && (this.Trm[1][1] == 0.0F) && (((this.Trm[0][1] > 0.0F) && (this.Trm[1][0] < 0.0F)) || ((this.Trm[0][1] < 0.0F) && (this.Trm[1][0] > 0.0F) && (this.pageRotation == 0))))
    {
      this.stringWidth = (this.rotatedYCoord - paramFloat3 + paramFloat1);
    }
    else
    {
      this.stringWidth = (paramFloat2 - this.rotatedXCoord + paramFloat1);
      this.lastXCoord = (paramFloat2 + paramFloat1);
    }
    this.lastGlyfWidth = paramFloat1;
    this.lastGlyf = paramString;
    this.lastYUsed = paramFloat3;
    return true;
  }

  public String getStyle()
  {
    return this.style;
  }

  public String toString()
  {
    String str = "text[" + this.text + "]\tfontSize[" + this.fontSize + "]\tspaceWidth[" + this.spaceWidth + "]\tcoord[" + this.rawXCoord + ", " + this.rawYCoord + ']';
    return str;
  }

  public static boolean ignoreGlyf(String paramString)
  {
    return paramString.codePointAt(0) == 65533;
  }

  public void convertSpacesTonbsp(boolean paramBoolean)
  {
    this.convertSpacesTonbsp = paramBoolean;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.TextBlock
 * JD-Core Version:    0.6.2
 */